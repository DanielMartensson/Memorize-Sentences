package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.danielmartensson.entity.Language;
import se.danielmartensson.entity.Sentence;
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
	
	public void checkAndSave(String sentenceInForeignLanguage, String sentenceInYourLanguage, String yourLanugage) {
		Language languageObject = languageService.findByLanguage(yourLanugage);
		if(languageObject == null) {
			languageObject = languageService.save(new Language(0L, yourLanugage));
		}
		boolean sentenceExist = sentenceRepository.existsBySentenceInForeignLanguageAndSentenceInYourLanguage(sentenceInForeignLanguage, sentenceInYourLanguage);
		if(!sentenceExist) {
			save(new Sentence(0L, sentenceInForeignLanguage, sentenceInYourLanguage, languageObject));
		}
	}

	@Transactional // Important to have here, else we cannot run this method
	public void deleteAllThatContains(Language language) {
		sentenceRepository.deleteByLanguage(language);
	}
}
