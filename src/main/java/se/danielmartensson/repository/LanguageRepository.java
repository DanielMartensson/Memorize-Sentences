package se.danielmartensson.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entity.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
	Language findByLanguage(String language);
}