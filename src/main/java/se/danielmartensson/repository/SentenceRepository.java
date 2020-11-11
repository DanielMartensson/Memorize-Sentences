package se.danielmartensson.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entity.Language;
import se.danielmartensson.entity.Sentence;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
	boolean existsBySentenceInFrenchOrSentenceInOtherLanguage(String sentenceInFrench, String sentenceInOtherLanguage);
	void deleteByLanguage(Language language);
	List<Sentence> findByLanguage(Language language);
}