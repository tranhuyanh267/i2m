package web.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String email;
    private String fullName;
    private String password;
    private boolean isActive;
    private String imgUrl;

    @OneToOne(cascade = CascadeType.ALL)
    private Role role;

}
