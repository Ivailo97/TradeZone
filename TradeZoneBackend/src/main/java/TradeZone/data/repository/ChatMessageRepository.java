package TradeZone.data.repository;

import TradeZone.data.model.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findAllByChannelOrderByTimestampDesc(String channel, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update chat_messages " +
            " join profiles p on chat_messages.sender_id = p.id " +
            " join users u on u.profile_id = p.id set read_date = now() "
            + " where channel = ?1 and u.username = ?2 and read_date is null",
            nativeQuery = true)
    void sendReadReceipt(String channel, String sender);

}
