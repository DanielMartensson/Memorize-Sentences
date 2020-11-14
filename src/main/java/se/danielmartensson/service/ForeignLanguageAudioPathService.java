package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		// Important to remove the audio path to sentence before deleting the audio
		Sentence sentence = sentenceService.findBySentenceInForeignLanguage(foreignLanguageAudioPath.getFileName().replace(".mp3", "")); // fileName and foreign language sentence is the same
		sentence.getForeignLanguageAudioPath().setForeignLanguageAudioPath("-");
		sentenceService.save(sentence);
		foreignLanguageAudioPathRepository.delete(foreignLanguageAudioPath); 
	}
	
	public boolean existsById(Long id) {
		return foreignLanguageAudioPathRepository.existsById(id);
	}
	
	public ForeignLanguageAudioPath findByFileName(String fileName) {
		return foreignLanguageAudioPathRepository.findByFileName(fileName);
	}
}
