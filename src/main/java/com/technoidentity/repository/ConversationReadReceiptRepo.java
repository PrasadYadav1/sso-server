package com.technoidentity.repository;

import com.technoidentity.entity.ConversationReadReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversationReadReceiptRepo extends JpaRepository<ConversationReadReceipt, UUID> {
}
