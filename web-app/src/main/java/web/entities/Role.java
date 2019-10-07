package web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private String id;
}
