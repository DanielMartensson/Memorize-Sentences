package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.danielmartensson.entity.YourLanguage;
import se.danielmartensson.repository.LanguageRepository;

@Service
public class LanguageService {

	private final LanguageRepository languageRepository;
	
	@Autowired
	private SentenceService sentenceService;

	public LanguageService(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	public List<YourLanguage> findAll() {
		return languageRepository.findAll();
	}

	public YourLanguage save(YourLanguage yourLanguage) {
		return languageRepository.save(yourLanguage);
	}

	public void delete(YourLanguage yourLanguage) {
		// Important to delete all the sentences first that contains that language
		sentenceService.deleteAllThatContains(yourLanguage);
		languageRepository.delete(yourLanguage); 
	}
	
	public boolean existsById(Long id) {
		return languageRepository.existsById(id);
	}
	
	public YourLanguage findByYourLanguage(String yourLanguage) {
		return languageRepository.findByYourLanguage(yourLanguage);
	}
}
