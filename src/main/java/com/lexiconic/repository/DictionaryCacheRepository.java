package com.lexiconic.repository;

import com.lexiconic.domain.entity.DictionaryCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DictionaryCacheRepository extends JpaRepository<DictionaryCache, UUID> {
    DictionaryCache findByWord(String word);
}
