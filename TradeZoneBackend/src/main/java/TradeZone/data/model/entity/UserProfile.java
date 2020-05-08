package TradeZone.data.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "profiles")
@NoArgsConstructor
public class UserProfile extends BaseEntity {

    @OneToOne(mappedBy = "profile")
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @Column(name = "about_me", columnDefinition = "TEXT")
    private String aboutMe;

    @Column
    private String city;

    @Column
    private String country;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "profiles_favorites",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fav_advertisement_id", referencedColumnName = "id")
    )
    private List<Advertisement> favorites;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "profiles_viewed",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "viewed_advertisement_id", referencedColumnName = "id")
    )
    private List<Advertisement> viewed;

    @OneToMany(mappedBy = "creator")
    private List<Advertisement> createdAdvertisements;

    @OneToMany(mappedBy = "creator")
    private List<Category> createdCategories;

    public UserProfile(User user) {
        this.setUser(user);
        this.setIsCompleted(false);
        this.setFavorites(new ArrayList<>());
        this.setCreatedAdvertisements(new ArrayList<>());
        this.setCreatedCategories(new ArrayList<>());
        this.setViewed(new ArrayList<>());
    }
}
