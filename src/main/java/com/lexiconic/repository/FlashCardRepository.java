package com.lexiconic.repository;

import com.lexiconic.domain.entity.FlashCard;
import org.springframework.context.annotation.Fallback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, UUID> {
    List<FlashCard> findAllByDeckId(UUID deckId);

    @Modifying
    @Query("DELETE FROM FlashCard f WHERE f.id = :cardId AND f.deck.id = :deckId")
    void deleteByIdAndDeckId(@Param("cardId") UUID cardId, @Param("deckId") UUID deckId);

}
