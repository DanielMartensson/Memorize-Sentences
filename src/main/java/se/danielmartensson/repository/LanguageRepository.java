package se.danielmartensson.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entity.YourLanguage;

@Repository
public interface LanguageRepository extends JpaRepository<YourLanguage, Long> {
	YourLanguage findByYourLanguage(String yourLanguage);
}