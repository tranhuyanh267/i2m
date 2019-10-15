package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.entities.Messages;
import web.payload.MessageDetailResponse;
import web.repositories.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public List<MessageDetailResponse> getMessageDetail(Long confessionId) {
        List<MessageDetailResponse> result = new ArrayList<>();
        List<Messages> messageList = messageRepository.findByConfessionId(confessionId);
        for (Messages message : messageList) {
            result.add(new MessageDetailResponse(message.getId(), message.getBody(), message.isSended(), message.getSendDate(), message.getSubject(), message.getFileUrl()));
        }
        return result;
    }
}