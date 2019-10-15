package web.payload;

import java.util.Date;

public class MessageDetailResponse {
    private Long id;
    private String body;
    private boolean isSent;
    private Date sendDate;
    private String subject;
    private String fileUrl;

    public MessageDetailResponse(Long id, String body, boolean isSent, Date sendDate, String subject, String fileUrl) {
        this.id = id;
        this.body = body;
        this.isSent = isSent;
        this.sendDate = sendDate;
        this.subject = subject;
        this.fileUrl = fileUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
