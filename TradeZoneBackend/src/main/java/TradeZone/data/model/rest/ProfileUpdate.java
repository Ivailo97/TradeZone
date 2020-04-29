package TradeZone.data.model.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdate {

    private Long id;

    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private String aboutMe;
}
