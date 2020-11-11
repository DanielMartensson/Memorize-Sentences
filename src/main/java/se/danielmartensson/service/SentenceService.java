package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.danielmartensson.entity.Language;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.repository.LanguageRepository;
import se.danielmartensson.repository.SentenceRepository;

@Service
public class SentenceService {

	private final SentenceRepository sentenceRepository;
	
	@Autowired
	private LanguageService languageService;

	public SentenceService(SentenceRepository sentenceRepository) {
		this.sentenceRepository = sentenceRepository;
	}

	public List<Sentence> findAll() {
		return sentenceRepository.findAll();
	}

	public Sentence save(Sentence sentence) {
		return sentenceRepository.save(sentence);
	}

	public void delete(Sentence sentence) {
		sentenceRepository.delete(sentence);
	}
	
	public boolean existsById(Long id) {
		return sentenceRepository.existsById(id);
	}
	
	public void checkAndSave(String wordInFrench, String wordInOtherLanguage, String language) {
		Language languageObject = languageService.findByLanguage(language);
		if(languageObject == null) {
			languageObject = languageService.save(new Language(0L, language));
		}
		boolean sentenceExist = sentenceRepository.existsByWordInFrenchOrWordInOtherLanguage(wordInFrench, wordInOtherLanguage);
		if(!sentenceExist) {
			save(new Sentence(0L, wordInFrench, wordInOtherLanguage, languageObject));
		}
	}
}
