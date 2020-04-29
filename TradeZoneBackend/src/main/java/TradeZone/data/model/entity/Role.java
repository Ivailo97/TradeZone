package TradeZone.data.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import TradeZone.data.model.enums.RoleName;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {

    @NaturalId
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Override
    public String getAuthority() {
        return this.name.name();
    }
}
