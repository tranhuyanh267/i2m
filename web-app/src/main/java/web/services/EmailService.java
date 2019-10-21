package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.entities.MailBox;
import web.entities.Influencer;
import web.entities.Message;
import web.model.ReceiveMessageModel;
import web.repositories.InfluencerRepository;
import web.repositories.MessageRepository;
import web.repositories.UserRepository;

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
import java.util.*;

@Service
@EnableScheduling
public class EmailService {
    //This email must be enable 'Less Secure Apps' allow
    private final String MY_EMAIL_ADDRESS = "sendmailtav@gmail.com";
    private final String MY_EMAIL_PASSWORD = "Zaq@123456";

    @Autowired
    ConfessionService confessionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InfluencerRepository influencerRepository;

    @Autowired
    MessageRepository messageRepository;

    //@Scheduled(fixedRate = 10 * 60 * 1000)
    public void checkRecords() {
        try {
            downloadEmailsFromInbox();
        } catch (Exception e) {

        }
    }

    @Async
    public void send(@Nullable MultipartFile file, String subject, String body, String userId, String influencerId) throws Exception {
        Influencer currentInfluencer = influencerRepository.findById(influencerId).orElse(null);
        Properties props = new Properties();
        Authenticator auth;
        Session session = null;
        try {
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
            //create Authenticator object to pass in Session.getInstance argument
            auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MY_EMAIL_ADDRESS, MY_EMAIL_PASSWORD);
                }
            };
            session = Session.getInstance(props, auth);
        } catch (Exception e) {
            //Error from authentication Email
            //Log
            throw new Exception("An error occurred. Please try again.");
        }
        String filename = "";
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("sendmailtav@gmail.com", "I2M"));
            msg.setReplyTo(InternetAddress.parse("sendmailtav@gmail.com", false));
            //Set message content
            msg.setSubject(subject, "UTF-8");

            //Someone change this line, that why I can't test with postman
            //The DB store '/n' but the sent email don't.
            //Example: I send the email with body: "Hello, B\nNice to meet you."
            //Email influencer will get: "Hello, B Nice to meet you."
            //But the DB store : "Hello, B\nNice to meet you."
            //So, system can't detect who's the one sent it.
            //If it work correctly with react, keep it
            msg.setContent(body, "text/html; charset=UTF-8");

            //This is my old line
            //msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());
            msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(currentInfluencer.getEmail(), false));

            //Attach File To Message
            if (file != null) {
                // Create the message body part
                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText(body);
                messageBodyPart.setContent(body,"text/html; charset=UTF-8");

                // Create a multipart message for attachment
                Multipart multipart = new MimeMultipart();
                // Set text message part
                multipart.addBodyPart(messageBodyPart);
                String uuid = UUID.randomUUID().toString().replace("-", "");
                // Second part is attachment
                messageBodyPart = new MimeBodyPart();
                File sendFile = fileNameConverter(file, uuid);
                DataSource source = new FileDataSource(sendFile);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.getOriginalFilename());
                multipart.addBodyPart(messageBodyPart);
                // Send the complete message parts
              ///  msg.setContent(multipart);
                msg.setContent(multipart, "text/html; charset=UTF-8");
                filename = sendFile.getName();
            }

            // Send message
            Transport.send(msg);
        } catch (Exception e) {
            //Log
            throw new Exception("Failed to send your email. Please try again.");
        }
        //Store Confession to DB
        try {
            MailBox confession = confessionService.findConfession(userId, influencerId);
            if (confession == null) {
                MailBox newConfession = new MailBox();
                newConfession.setUser(userRepository.findUserByIdCustom(userId));
                newConfession.setInfluencer(currentInfluencer);
                confession = confessionService.createConfession(newConfession);
            }
            Message newMessage = new Message(subject, body, new Date(), true, filename, confession);
            messageRepository.save(newMessage);
        } catch (Exception e) {
            //Log
            throw new Exception("Your email have been sent successfully. But there is an Error when we try to store it!");
        }
    }

    public void downloadEmailsFromInbox() {
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
            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_WRITE);
            // find unread message
            javax.mail.Message[] messages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (int i = 0; i < messages.length; i++) {
                javax.mail.Message msg = messages[i];
                // mark as read
                folderInbox.setFlags(new javax.mail.Message[]{msg}, new Flags(Flags.Flag.SEEN), true);
                //
                detectConfession(messages[i]);
            }
            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (Exception e) {
            //Log
            System.out.println(e.getMessage());
        }
    }

    private static File fileNameConverter(MultipartFile file, String filename) throws IOException {
        //File Rename
        String[] extensions = file.getOriginalFilename().split("\\.");
        filename = filename + "." + extensions[extensions.length - 1];
        File result = new File("Media/Mail/" + filename);
        //File Store
        result.createNewFile();
        FileOutputStream fos = new FileOutputStream(result);
        fos.write(file.getBytes());
        fos.close();
        return result;
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

    public void detectConfession(javax.mail.Message msg) throws Exception {
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

        String mySentMessage = String.join("", receiveMessage.getSentMessage());
        //My SQl stored "\n" replace "\r ".
        mySentMessage = mySentMessage.replaceAll("\r ", "\n");
        //GMAIL sometimes add '\r' or ' ' to end of email.
        if (mySentMessage.charAt((mySentMessage.length() - 1)) == ' '
                || mySentMessage.charAt((mySentMessage.length() - 1)) == '\r'
                || mySentMessage.charAt((mySentMessage.length() - 1)) == '\n') {
            mySentMessage = mySentMessage.substring(0, mySentMessage.length() - 1);
        }
        String myReceiveMessage = String.join("", receiveMessage.getReceiveMessage());
        Message oldMessage = messageRepository.findByDetail(mySentMessage, subject);
        Message newMessage = new Message("Re: " + subject, myReceiveMessage, new Date(), false, "", oldMessage.getMailBox());
        messageRepository.save(newMessage);
    }

    public ReceiveMessageModel replyEmailContainHandle(String content) {
        List<String> reviveMessage = new ArrayList<>();
        List<String> sentMessage = new ArrayList<>();

        int line = 0;
        boolean revive = true;

        //Split email contain to each line
        String[] lineSplit = content.split("\n");

        for (int j = 0; j < lineSplit.length; j++) {
            //Track "Information line". Kind of : "Vào Th 3, 1 thg 10, 2019 vào lúc 12:53 I2M <sendmailtav@gmail.com> đã viết:"
            String[] commaSplit = (lineSplit[j].split("\\,"));
            if (commaSplit.length == 3) {
                String[] colonSplit = commaSplit[2].split("\\:");
                if (colonSplit.length > 1) {
                    String[] emailSplit = colonSplit[1].split("<sendmailtav@gmail\\.com>");
                    if (emailSplit.length == 2) {
                        line = j;
                        revive = false;
                    }
                }
            }

            //Influencer reply message here (after "Information line")
            if (revive == true) {
                reviveMessage.add((lineSplit[j]));
            }

            //User's message sent to Influencer (before "Information line")
            //We need those message to detect who own this email
            if (revive == false && j > (line + 1)) {
                sentMessage.add((lineSplit[j]).substring(1));
            }
        }

        //Remove empty line
        reviveMessage.remove(0);
        reviveMessage.remove(reviveMessage.size() - 1);

        //Remove empty line
        if(sentMessage.size() > 1){
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
}
