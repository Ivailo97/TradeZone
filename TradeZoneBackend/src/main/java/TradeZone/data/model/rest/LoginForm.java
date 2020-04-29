package TradeZone.data.model.rest;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginForm {

    @NotBlank
    @Size(min=3, max = 11)
    private String username;

    @NotBlank
    @Size(min = 3, max = 16)
    private String password;
}
