package se.danielmartensson.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entity.Sentence;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {

}