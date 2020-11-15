package se.danielmartensson.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.danielmartensson.entity.ForeignLanguageAudioPath;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.repository.ForeignLanguageAudioPathRepository;

@Service
public class ForeignLanguageAudioPathService {

	private final ForeignLanguageAudioPathRepository foreignLanguageAudioPathRepository;

	@Autowired
	private SentenceService sentenceService;

	public ForeignLanguageAudioPathService(ForeignLanguageAudioPathRepository foreignLanguageAudioPathRepository) {
		this.foreignLanguageAudioPathRepository = foreignLanguageAudioPathRepository;
	}

	public List<ForeignLanguageAudioPath> findAll() {
		return foreignLanguageAudioPathRepository.findAll();
	}

	public ForeignLanguageAudioPath save(ForeignLanguageAudioPath foreignLanguageAudioPath) {
		return foreignLanguageAudioPathRepository.save(foreignLanguageAudioPath);
	}

	public void delete(ForeignLanguageAudioPath foreignLanguageAudioPath) {
		Sentence sentence = sentenceService.findBySentenceInForeignLanguage(foreignLanguageAudioPath.getFileName().replace(".mp3", "")); // fileName and sentenceInForeignLanguage is the same
		if(sentence != null) {
			sentenceService.delete(sentence); // If we detected this from the Audio CRUD
		}
		foreignLanguageAudioPathRepository.delete(foreignLanguageAudioPath);
		File deleteAudioFile = new File(foreignLanguageAudioPath.getForeignLanguageAudioPath());
		deleteAudioFile.delete();
	}
	
	public boolean existsById(Long id) {
		return foreignLanguageAudioPathRepository.existsById(id);
	}
	
	public ForeignLanguageAudioPath findByFileName(String fileName) {
		return foreignLanguageAudioPathRepository.findByFileName(fileName);
	}

	@Transactional // Important to have here, else we cannot run this method
	public void deleteAllThatContains(String fromLanguage) {
		foreignLanguageAudioPathRepository.deleteByFromLanguage(fromLanguage);
	}
}