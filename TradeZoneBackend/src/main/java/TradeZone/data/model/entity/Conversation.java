package TradeZone.data.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "conversations")
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
