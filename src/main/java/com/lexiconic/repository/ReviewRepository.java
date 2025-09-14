package com.lexiconic.repository;

import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Review findTopByFlashCardOrderByCreatedDesc(FlashCard flashCard);
}
