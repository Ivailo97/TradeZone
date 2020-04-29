package TradeZone.data.model.rest;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SignUpForm {

    @Pattern(regexp = "[a-z][a-z0-9]{2,11}")
    private String username;

    @Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")
    private String email;

    private Set<String> roles;

    @Size(min = 3, max = 16)
    private String password;
}
