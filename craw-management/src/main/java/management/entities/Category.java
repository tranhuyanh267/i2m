package management.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Category {

    @Id
    private String id;
    private String name;

}

