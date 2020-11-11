package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public List<Sentence> findByLanguage(Language language) {
		return sentenceRepository.findByLanguage(language);
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
	
	public void checkAndSave(String sentenceInFrench, String sentenceInOtherLanguage, String language) {
		Language languageObject = languageService.findByLanguage(language);
		if(languageObject == null) {
			languageObject = languageService.save(new Language(0L, language));
		}
		boolean sentenceExist = sentenceRepository.existsBySentenceInFrenchOrSentenceInOtherLanguage(sentenceInFrench, sentenceInOtherLanguage);
		if(!sentenceExist) {
			save(new Sentence(0L, sentenceInFrench, sentenceInOtherLanguage, languageObject));
		}
	}

	@Transactional // Important to have here, else we cannot run this method
	public void deleteAllThatContains(Language language) {
		sentenceRepository.deleteByLanguage(language);
	}
}
