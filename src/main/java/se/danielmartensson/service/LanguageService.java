package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.danielmartensson.entity.Language;
import se.danielmartensson.repository.LanguageRepository;

@Service
public class LanguageService {

	private final LanguageRepository languageRepository;
	
	@Autowired
	private SentenceService sentenceService;

	public LanguageService(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	public List<Language> findAll() {
		return languageRepository.findAll();
	}

	public Language save(Language language) {
		return languageRepository.save(language);
	}

	public void delete(Language language) {
		// Important to delete all the sentences first that contains that language
		sentenceService.deleteAllThatContains(language);
		languageRepository.delete(language); 
	}
	
	public boolean existsById(Long id) {
		return languageRepository.existsById(id);
	}
	
	public Language findByLanguage(String language) {
		return languageRepository.findByLanguage(language);
	}
}
