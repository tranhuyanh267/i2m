package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.entities.Message;
import web.payload.MessageDetailResponse;
import web.repositories.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    EmailService emailService;

    public List<MessageDetailResponse> getMessageDetail(String confessionId) {
        List<MessageDetailResponse> result = new ArrayList<>();
        List<Message> messageList = messageRepository.findByConfessionId(confessionId);

        for (Message message : messageList) {
            result.add(new MessageDetailResponse(message.getId(), message.getBody(),
                    message.isSended(), message.getSendDate(), message.getSubject(), message.getFileUrl(),
                    message.getMailBox().getInfluencer().getFullName(),
                    message.getMailBox().getUser().getFullName(),
                    message.getMailBox().getUser().getEmail(),
                    message.getMailBox().getInfluencer().getEmail()
            ));
        }
        return result;
    }

    public boolean checkOwnedFile(String userId,String fileName){
        Message message = messageRepository.findByFileUrl(fileName);
        if(message == null){
            return false;
        }
        if(!message.getMailBox().getUser().getId().equals(userId)){
            return false;
        }
        return true;
    }
}