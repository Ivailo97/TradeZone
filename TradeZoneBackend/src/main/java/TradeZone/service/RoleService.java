package TradeZone.service;

import TradeZone.data.model.service.RoleServiceModel;

import java.util.List;

public interface RoleService {

    void seed();

    boolean rolesAreSeeded();

    List<RoleServiceModel> getAllExceptRoot();
}
