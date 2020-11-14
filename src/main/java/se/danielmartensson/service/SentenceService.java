package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.danielmartensson.entity.TranslateFromTo;
import se.danielmartensson.entity.ForeignLanguageAudioPath;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.repository.SentenceRepository;

@Service
public class SentenceService {

	private final SentenceRepository sentenceRepository;
	
	@Autowired
	private TranslateFromToService translateFromToService;
	
	@Autowired
	private ForeignLanguageAudioPathService foreignLanguageAudioPathService;

	public SentenceService(SentenceRepository sentenceRepository) {
		this.sentenceRepository = sentenceRepository;
	}

	public List<Sentence> findAll() {
		return sentenceRepository.findAll();
	}
	
	public List<Sentence> findByTranslateFromTo(TranslateFromTo translateFromTo) {
		return sentenceRepository.findByTranslateFromTo(translateFromTo);
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
	
	public void checkAndSave(String sentenceInForeignLanguage, String sentenceInYourLanguage, String fromLanguage, String toLanguage) {
		boolean sentenceExist = sentenceRepository.existsBySentenceInForeignLanguageAndSentenceInYourLanguage(sentenceInForeignLanguage, sentenceInYourLanguage);
		if(!sentenceExist) {
			TranslateFromTo translateFromToObject = translateFromToService.findByFromLanguage(fromLanguage);
			if(translateFromToObject == null) {
				translateFromToObject = translateFromToService.save(new TranslateFromTo(0L, fromLanguage, toLanguage));
			}
			String fileName = sentenceInForeignLanguage + ".mp3"; 
			ForeignLanguageAudioPath foreignLanguageAudioPathObject = foreignLanguageAudioPathService.findByFileName(fileName);
			if(foreignLanguageAudioPathObject == null) {
				foreignLanguageAudioPathObject = foreignLanguageAudioPathService.save(new ForeignLanguageAudioPath(0L, fromLanguage, fileName, "-"));
			}
			save(new Sentence(0L, sentenceInForeignLanguage, sentenceInYourLanguage, translateFromToObject, foreignLanguageAudioPathObject));
		}
	}
	
	public Sentence findBySentenceInForeignLanguage(String sentenceInForeignLanguage) {
		return sentenceRepository.findBySentenceInForeignLanguage(sentenceInForeignLanguage);
	}

	@Transactional // Important to have here, else we cannot run this method
	public void deleteAllThatContains(TranslateFromTo translateFromTo) {
		sentenceRepository.deleteByTranslateFromTo(translateFromTo);
	}
}
