package com.lexiconic.service;

import com.lexiconic.domain.entity.Deck;
import com.lexiconic.domain.entity.FlashCard;
import com.lexiconic.domain.entity.Review;
import com.lexiconic.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public LocalDateTime calculateSuperMemo2Algorithm(Deck deck, FlashCard dbFlashCard, int quality) {
        if (quality < 0 || quality > 5) {
            throw new IllegalArgumentException("quality must be between 0 and 5");
        }
        if (deck != null && dbFlashCard != null) {
            Review review = reviewRepository.findTopByFlashCardOrderByCreatedDesc(dbFlashCard);

            int repetitions = 0;
            int interval = 1;
            float easiness = 2.5f;

            if (review != null) {
                // retrieve the stored values (default values if new cards)
                repetitions = review.getRepetitions();
                easiness = review.getEaseFactor();
                interval = review.getIntervals();
            }

            // easiness factor
            easiness = (float) Math.max(1.3, easiness + 0.1 - (5.0 - quality) * (0.08 + (5.0 - quality) * 0.02));

            if (review == null || quality < 3) {
                repetitions = 0;
            } else {
                repetitions += 1;
            }
            
            // interval
            if (repetitions <= 1) {
                interval = 1;
            } else if (repetitions == 2) {
                interval = 6;
            } else {
                interval = Math.round(interval * easiness);
            }

            if(easiness < 1.3){
                easiness = 1.3f;
            }

            LocalDateTime nextPracticeDate = LocalDateTime.now().plusDays(interval);

            reviewRepository.save(new Review(
                    null,
                    quality,
                    repetitions,
                    easiness,
                    interval,
                    dbFlashCard,
                    null,
                    nextPracticeDate
                ));
            return nextPracticeDate;
            }
        return null;
    }
}
