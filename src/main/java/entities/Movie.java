package entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Getter @Setter
public class Movie {
    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date added_at;

    public Movie() {
    }

    public Movie(String id) {
        this.id = id;
    }
}
