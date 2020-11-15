package se.danielmartensson.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.danielmartensson.entity.TranslateFromTo;
import se.danielmartensson.repository.TranslateFromToRepository;

@Service
public class TranslateFromToService {

	private final TranslateFromToRepository translateFromToRepository;
	
	@Autowired
	private SentenceService sentenceService;
	
	@Autowired
	private ForeignLanguageAudioPathService foreignLanguageAudioPathService;

	public TranslateFromToService(TranslateFromToRepository translateFromToRepository) {
		this.translateFromToRepository = translateFromToRepository;
	}

	public List<TranslateFromTo> findAll() {
		return translateFromToRepository.findAll();
	}
	
	public List<String> findAllFromLanguage() {
		List<String> fromLanguageList = new ArrayList<>();
		for(TranslateFromTo translateFromTo : findAll()) {
			fromLanguageList.add(translateFromTo.getFromLanguage());
		}
		return fromLanguageList;
	}

	public TranslateFromTo save(TranslateFromTo translateFromTo) {
		return translateFromToRepository.save(translateFromTo);
	}

	public void delete(TranslateFromTo translateFromTo) {
		// Important to delete all the sentences first that contains that language
		sentenceService.deleteAllThatContains(translateFromTo);
		foreignLanguageAudioPathService.deleteAllThatContains(translateFromTo.getFromLanguage());
		translateFromToRepository.delete(translateFromTo); 
	}
	
	public boolean existsById(Long id) {
		return translateFromToRepository.existsById(id);
	}
	
	public TranslateFromTo findByFromLanguage(String fromLanguage) {
		return translateFromToRepository.findByFromLanguage(fromLanguage);
	}
}
