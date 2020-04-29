package TradeZone.data.model.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdate {

    private Long id;

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;
}
