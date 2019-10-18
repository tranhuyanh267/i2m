package web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Message {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String subject;
    private String body;
    //if user send multiple file
    private Date sendDate;
    private boolean isSended;
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mail_id")
    private MailBox mailBox;

    public Message(String subject, String body, Date sendDate, boolean isSended, String fileUrl, MailBox mailBox) {
        this.subject = subject;
        this.body = body;
        this.sendDate = sendDate;
        this.isSended = isSended;
        this.fileUrl = fileUrl;
        this.mailBox = mailBox;
    }
}
