package se.danielmartensson.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entity.TranslateFromTo;

@Repository
public interface TranslateFromToRepository extends JpaRepository<TranslateFromTo, Long> {
	TranslateFromTo findByFromLanguage(String fromLanguage);
}