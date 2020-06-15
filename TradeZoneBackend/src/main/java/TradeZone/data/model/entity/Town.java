package TradeZone.data.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "towns")
public class Town extends BaseEntity {

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @OneToMany(mappedBy = "town")
    private List<UserProfile> citizen;

    public Town() {
        this.citizen = new ArrayList<>();
    }
}
