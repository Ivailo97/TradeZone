package TradeZone.data.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity(name = "channels")
@AllArgsConstructor
@NoArgsConstructor
public class Channel {

    @Id
    private String id;

    @OneToMany(mappedBy = "channel")
    private List<ChatMessage> messages;

    @ManyToMany(mappedBy = "subscribedTo")
    private List<UserProfile> subscribed;

    public Channel(String id) {
        this.id = id;
    }
}
