package TradeZone.data.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "photos")
@EqualsAndHashCode(callSuper = true)
public class Photo extends BaseEntity {

    @Column
    private String idInCloud;

    @Column
    private String url;
}
