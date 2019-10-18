package web.payload;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDetailResponse {
    private String id;
    private String body;
    private boolean isSent;
    private Date sendDate;
    private String subject;
    private String fileUrl;

    public MessageDetailResponse(String id, String body, boolean isSent, Date sendDate, String subject, String fileUrl) {
        this.id = id;
        this.body = body;
        this.isSent = isSent;
        this.sendDate = sendDate;
        this.subject = subject;
        this.fileUrl = fileUrl;
    }

}
