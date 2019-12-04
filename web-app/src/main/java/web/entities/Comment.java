package web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String id;
    private String content;

    private String postId;

}
