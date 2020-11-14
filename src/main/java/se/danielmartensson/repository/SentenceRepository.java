package se.danielmartensson.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entity.TranslateFromTo;
import se.danielmartensson.entity.Sentence;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
	boolean existsBySentenceInForeignLanguageAndSentenceInYourLanguage(String sentenceInForeignLanguage, String sentenceInYourLanguage);
	void deleteByTranslateFromTo(TranslateFromTo translateFromTo);
	List<Sentence> findByTranslateFromTo(TranslateFromTo translateFromTo);
	Sentence findBySentenceInForeignLanguage(String sentenceInForeignLanguage);
}