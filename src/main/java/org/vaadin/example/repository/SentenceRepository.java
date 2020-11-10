package org.vaadin.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.Sentence;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {

}