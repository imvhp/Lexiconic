package com.vhp.lexiconic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String front;
    private String image;

    @Column(nullable = false)
    private String definition;
    private String example;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;
}
