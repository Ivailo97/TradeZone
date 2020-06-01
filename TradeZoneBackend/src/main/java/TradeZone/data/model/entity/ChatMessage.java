package TradeZone.data.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "chat_messages")
public class ChatMessage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private UserProfile sender;

    @ManyToOne
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private Channel channel;

    @Column(name = "content")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "read_date")
    private Date readDate;
}
