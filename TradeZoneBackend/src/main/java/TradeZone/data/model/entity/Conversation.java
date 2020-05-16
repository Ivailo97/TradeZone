package TradeZone.data.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Conversation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "interlocutor_id", referencedColumnName = "id")
    private UserProfile interlocutor;

    @ManyToOne
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private UserProfile host;

    @ManyToMany(mappedBy = "conversations")
    private List<Message> messages;

    public Conversation(UserProfile host, UserProfile interlocutor) {
        this.interlocutor = interlocutor;
        this.host = host;
        this.messages = new ArrayList<>();
    }
}
