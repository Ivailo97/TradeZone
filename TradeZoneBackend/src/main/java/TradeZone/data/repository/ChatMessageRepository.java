package TradeZone.data.repository;

import TradeZone.data.model.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findAllByChannelOrderByTimestampDesc(String channel, Pageable pageable);

    //TODO fix query
    @Modifying
    @Query(value = "update message set readDate = now() "
            + " where channel = ?1 and sender = ?2 and readDate is null",
            nativeQuery = true)
    void sendReadReceipt(String channel, String username);
}
