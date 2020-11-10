package se.danielmartensson.service;

import java.util.List;

import org.springframework.stereotype.Service;

import se.danielmartensson.entity.Sentence;
import se.danielmartensson.repository.SentenceRepository;

@Service
public class SentenceService {

	private final SentenceRepository sentenceRepository;

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

}
