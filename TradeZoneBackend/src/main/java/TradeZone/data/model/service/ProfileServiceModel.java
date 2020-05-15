package TradeZone.data.model.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileServiceModel extends BaseServiceModel {

    private String firstName;

    private String lastName;

    private String aboutMe;

    private String city;

    private String country;

    private Boolean isCompleted;

    private UserServiceModel user;

    private PhotoServiceModel photo;

    private List<ConversationServiceModel> hostedConversations;

    private List<AdvertisementServiceModel> favorites;

    private List<AdvertisementServiceModel> createdAdvertisements;

    private List<CategoryServiceModel> createdCategories;
}
