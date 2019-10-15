package web.model;

import java.util.List;

public class ReceiveMessageModel {

    private List<String> receiveMessage;
    private List<String> sentMessage;

    public List<String> getReceiveMessage() {
        return receiveMessage;
    }

    public List<String> getSentMessage() {
        return sentMessage;
    }

    public ReceiveMessageModel(List<String> reviveMessage, List<String> sentMessage) {
        this.receiveMessage = reviveMessage;
        this.sentMessage = sentMessage;
    }

}