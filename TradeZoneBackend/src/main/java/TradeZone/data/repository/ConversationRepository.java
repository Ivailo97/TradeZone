package TradeZone.data.repository;

import TradeZone.data.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> getByHostUserUsernameAndInterlocutorUserUsername(String receiver, String sender);
}
