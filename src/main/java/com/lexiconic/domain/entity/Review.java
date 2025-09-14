package com.lexiconic.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int quality;
    private int repetitions;
    private float easeFactor;
    private int intervals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flashCard_id", nullable = false)
    private FlashCard flashCard;

    @CreationTimestamp
    private LocalDateTime created;

    private LocalDateTime nextReview;
}
