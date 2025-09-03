package com.vhp.lexiconic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Users owner;

    @OneToMany(mappedBy = "deck")
    private List<FlashCard> flashCards;
}
