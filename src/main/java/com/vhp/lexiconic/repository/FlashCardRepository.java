package com.vhp.lexiconic.repository;

import com.vhp.lexiconic.model.FlashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, UUID> {
}
