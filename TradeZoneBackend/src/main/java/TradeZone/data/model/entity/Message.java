package TradeZone.data.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Message extends BaseEntity {

    @Column
    private String content;

    @ManyToMany
    @JoinTable(
            name = "messages_conversations",
            joinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "conversation_in", referencedColumnName = "id")
    )
    private List<Conversation> conversations;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private UserProfile sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private UserProfile receiver;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public Message(String content, UserProfile sender, UserProfile receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = LocalDateTime.now();
        this.conversations = new ArrayList<>();
    }
}
