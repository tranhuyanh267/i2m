package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.entities.MailBox;
import web.entities.Influencer;
import web.entities.Message;
import web.exceptions.WebAppException;
import web.model.ReceiveMessageModel;
import web.repositories.InfluencerRepository;
import web.repositories.MessageRepository;
import web.repositories.UserRepository;
import web.util.GoogleCloudHelper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@EnableScheduling
public class EmailService {
    //This email must be enable 'Less Secure Apps' allow
    private final String MY_EMAIL_ADDRESS = "flowershower282@gmail.com";
    private final String MY_EMAIL_PASSWORD = "vannguyen2802";

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    ConfessionService confessionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InfluencerRepository influencerRepository;

    @Autowired
    MessageRepository messageRepository;


    public void sendMessageWithAttachment(String userId, String influencerId,
                                          String subject,
                                          String text,
                                          @Nullable MultipartFile attachement
                                          ) {
        Influencer currentInfluencer = influencerRepository.findById(influencerId).orElse(null);
        String uploadedFileLink = "";
        try {

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(currentInfluencer.getEmail());

            helper.setSubject(subject);
            helper.setText(text, true);
            helper.setFrom("flowershower282@gmail.com", "I2M");
            if(attachement != null) {
                File convFile = new File(attachement.getOriginalFilename());
                FileOutputStream fos = new FileOutputStream(convFile);
                fos.write(attachement.getBytes());
                fos.close();
                helper.addAttachment(attachement.getOriginalFilename(), convFile);
            }

            emailSender.send(message);
            if(attachement != null) {
                String uuid = UUID.randomUUID().toString().replace("-", "");
                uploadedFileLink = GoogleCloudHelper.uploadFile(attachement, uuid);
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }

        try {
            MailBox confession = null;
            try {
                confession = confessionService.findConfession(userId, influencerId);
            } catch (Exception e) {

            }

            if (confession == null) {
                MailBox newConfession = new MailBox();
                newConfession.setUser(userRepository.findUserByIdCustom(userId));
                newConfession.setInfluencer(currentInfluencer);
                confession = confessionService.createConfession(newConfession);
            }
            Message newMessage;
            if(attachement == null) {
               newMessage = new Message(subject, text, new Date(), true, uploadedFileLink, "", confession);
            } else {
                newMessage = new Message(subject, text, new Date(), true, uploadedFileLink, attachement.getOriginalFilename(), confession);

            }
            messageRepository.save(newMessage);
        } catch (Exception e) {
            e.printStackTrace();
            //Log
            throw new WebAppException("Your email have been sent successfully. But there is an Error when we try to store it!");
        }
    }



    public void downloadEmailsFromInbox(String userId) {
        // for IMAP
        String protocol = "imap";
        String host = "imap.gmail.com";
        String port = "993";

        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
        try {
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(MY_EMAIL_ADDRESS, MY_EMAIL_PASSWORD);

            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_WRITE);
            javax.mail.Message[] messages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (int i = 0; i < messages.length; i++) {
                javax.mail.Message msg = messages[i];
                folderInbox.setFlags(new javax.mail.Message[]{msg}, new Flags(Flags.Flag.SEEN), true);
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                Date sentDate = msg.getSentDate();

                String contentType = msg.getContentType();
                String messageContent = "";
                String filename = "";
                String mediaLink = "";
                if (contentType.contains("multipart")) {

                    try {
                        Multipart content = (Multipart) msg.getContent();
                        for (int j = 0; j < content.getCount(); j++) {
                                MimeBodyPart part = (MimeBodyPart) content.getBodyPart(j);
                                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                    InputStream input = part.getInputStream();

                                    filename = part.getFileName();
                                    mediaLink = GoogleCloudHelper.uploadFileFromInputStream(input, filename);
                                }
                            }

                    } catch (Exception ex) {
                        messageContent = "[Error downloading content]";
                        ex.printStackTrace();
                    }

                }
                MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                messageContent = getTextFromMimeMultipart(mimeMultipart);
                String influencerId = influencerRepository.findByEmail(findEmail(from)).getId();

                MailBox mailBox = confessionService.findConfession(userId, influencerId);
                Message newMessage = new Message(subject, messageContent, sentDate, false, mediaLink, filename, mailBox);
                messageRepository.save(newMessage);
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (Exception e) {
            //Log
            System.out.println(e.getMessage());
        }
    }


    private Properties getServerProperties(String protocol, String host, String port) {
        Properties properties = new Properties();
        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);
        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));
        return properties;
    }

    private String parseAddresses(Address[] address) {
        String listAddress = "";

        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                listAddress += address[i].toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }

        return listAddress;
    }

    public void detectConfession(javax.mail.Message msg, String fileName, String mediaLink) throws Exception {
        MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
        String allMailContain = getTextFromMimeMultipart(mimeMultipart);

        //Reply Email Body with three parts: sender message, email information, old message
        ReceiveMessageModel receiveMessage = replyEmailContainHandle(allMailContain);

        if (receiveMessage.getReceiveMessage().size() < 1 || receiveMessage.getSentMessage().size() < 1) {
            throw new Exception("This is not a reply Email");
        }

        //String sentDate = msg.getSentDate().toString();

        String subject;
        try {
            subject = (msg.getSubject()).split("Re\\:")[1].trim();
        } catch (Exception e) {
            throw new Exception("This is not a reply Email");
        }

        String from;
        try {
            Address[] fromAddress = msg.getFrom();
            from = fromAddress[0].toString();

            //Address Example: Trần Ân Vũ K11 FUG <vutase62473@fpt.edu.vn>
            //Remove Sender Name, redundancy characters
            from = from.split("\\<")[1];
            from = from.split("\\>")[0];

        } catch (Exception e) {
            throw new Exception("Can't find sender");
        }

        List<Message> messageList = messageRepository.findBySubject(subject);

        List<Message> filterMessageByEmail = new ArrayList<>();
        for (int i = 0; i < messageList.size(); i++) {
            if ((messageList.get(i).getMailBox().getInfluencer().getEmail()).equals(from)) {
                filterMessageByEmail.add(messageList.get(i));
            }
        }
        Message oldMessage = null;
        if (filterMessageByEmail.size() == 1) {
            oldMessage = filterMessageByEmail.get(0);
        } else {
            for (int i = 0; i < filterMessageByEmail.size(); i++) {
                boolean checkEmail = true;
                for (String line : receiveMessage.getSentMessage()) {
                    //remove /r
                    if (line.charAt(line.length() - 1) == '\r') {
                        line = line.substring(0, line.length() - 1);
                    }
                    //Remove ' '
                    if (line.length() > 0) {
                        if (line.charAt(0) == ' ') {
                            line = line.substring(1);
                        }
                    }

                    if (!((messageList.get(i).getBody()).contains(line))) {
                        checkEmail = false;
                    }
                }
                if (checkEmail == true) {
                    oldMessage = messageList.get(i);
                }
            }
        }

        //String mySentMessage = String.join("", receiveMessage.getSentMessage());
        //My SQl stored "\n" replace "\r ".
        //mySentMessage = mySentMessage.replaceAll("\r ", "\n");
        //GMAIL sometimes add '\r' or ' ' to end of email.
        //if (mySentMessage.charAt((mySentMessage.length() - 1)) == ' '
        //        || mySentMessage.charAt((mySentMessage.length() - 1)) == '\r'
        //        || mySentMessage.charAt((mySentMessage.length() - 1)) == '\n') {
        //   mySentMessage = mySentMessage.substring(0, mySentMessage.length() - 1);
        //}

        String myReceiveMessage = String.join("", receiveMessage.getReceiveMessage());
        //oldMessage = messageRepository.findByDetail(mySentMessage, subject);
        Message newMessage = new Message("Re: " + subject, myReceiveMessage, new Date(), false,mediaLink, fileName, oldMessage.getMailBox());
        messageRepository.save(newMessage);
    }

    public ReceiveMessageModel replyEmailContainHandle(String content) {
        List<String> reviveMessage = new ArrayList<>();
        List<String> sentMessage = new ArrayList<>();

//        int line = 0;


        //Split email contain to each line
        String[] lineSplit = content.split("\n");

        for (int j = 0; j < lineSplit.length; j++) {
            boolean receive = true;
            //Track "Information line". Kind of : "Vào Th 3, 1 thg 10, 2019 vào lúc 12:53 I2M <sendmailtav@gmail.com> đã viết:"
            String[] commaSplit = (lineSplit[j].split("\\,"));
            if (commaSplit.length >= 3) {
                String[] colonSplit = commaSplit[commaSplit.length - 1].split("\\:");
                if (colonSplit.length >= 1) {
                    String[] emailSplit = colonSplit[1].split("<flowershower282@gmail\\.com>");
                    if (emailSplit.length == 2) {
//                        line = j;
                        receive = false;
                    }
                }
            }

            //Influencer reply message here (after "Information line")
            if (receive == true) {
                reviveMessage.add((lineSplit[j]));
            }

            //User's message sent to Influencer (before "Information line")
            //We need those message to detect who own this email
            if (receive == false) {
                sentMessage.add((lineSplit[j]).substring(1));
            }
        }

        //Remove empty line
        reviveMessage.remove(0);
        reviveMessage.remove(reviveMessage.size() - 1);

        //Remove empty line
        if (sentMessage.size() > 1) {
            sentMessage.remove(0);
        }
//        if (sentMessage.size() % 2 == 0) {
//            sentMessage.remove(sentMessage.size() - 1);
//        }

        sentMessage.set(0, sentMessage.get(0).substring(1));

        return new ReceiveMessageModel(reviveMessage, sentMessage);
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }
    private String findEmail(String stringContent) {
        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(stringContent);
        while (m.find()) {
            return m.group();
        }
        return "";
    }
}
