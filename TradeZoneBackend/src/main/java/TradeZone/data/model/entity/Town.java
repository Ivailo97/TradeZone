package TradeZone.data.model.entity;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "towns")
public class Town extends BaseEntity {

    @Expose
    @Column
    private String name;

    @Expose
    @Column
    private String region;

    @OneToMany
    private List<UserProfile> citizen;

    public Town() {
        this.citizen = new ArrayList<>();
    }
}
