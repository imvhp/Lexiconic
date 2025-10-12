package com.lexiconic.repository;

import com.lexiconic.domain.entity.Deck;
import com.lexiconic.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeckRepository extends JpaRepository<Deck, UUID> {
    List<Deck> findAllByOwner(Users owner);
    Deck findByIdAndOwner(UUID id, Users owner);
}
