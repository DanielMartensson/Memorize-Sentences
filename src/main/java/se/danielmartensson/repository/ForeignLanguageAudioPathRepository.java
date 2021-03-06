package se.danielmartensson.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entity.ForeignLanguageAudioPath;

@Repository
public interface ForeignLanguageAudioPathRepository extends JpaRepository<ForeignLanguageAudioPath, Long> {
	ForeignLanguageAudioPath findByFileName(String fileName);
	void deleteByFromLanguage(String language);
}