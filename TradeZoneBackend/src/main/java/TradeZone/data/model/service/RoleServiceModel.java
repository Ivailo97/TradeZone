package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;
import TradeZone.data.model.enums.RoleName;

@Getter
@Setter
public class RoleServiceModel {

    private Long id;

    private RoleName roleName;
}
