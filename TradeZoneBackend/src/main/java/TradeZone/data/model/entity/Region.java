package TradeZone.data.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "regions")
@Getter
@Setter
@NoArgsConstructor
public class Region extends BaseEntity {

    @Column
    private String name;

    @OneToMany(mappedBy = "region")
    private List<Town> towns;
}
