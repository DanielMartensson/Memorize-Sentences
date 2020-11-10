package org.vaadin.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Sentence;
import org.vaadin.example.repository.SentenceRepository;


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
