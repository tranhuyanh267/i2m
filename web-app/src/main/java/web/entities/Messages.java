package web.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String body;
    //if user send multiple file
    private Date sendDate;
    private boolean isSended;
    private String fileUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Confession confession;

    public Messages(String subject, String body, Date sendDate, boolean isSended, String fileUrl, Confession confession) {
        this.subject = subject;
        this.body = body;
        this.sendDate = sendDate;
        this.isSended = isSended;
        this.fileUrl = fileUrl;
        this.confession = confession;
    }

    public Messages() {
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Confession getConfession() {
        return confession;
    }

    public void setConfession(Confession confession) {
        this.confession = confession;
    }

    public boolean isSended() { return isSended; }

    public void setSended(boolean sended) {
        isSended = sended;
    }
}
