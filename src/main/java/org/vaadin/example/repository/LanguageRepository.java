package org.vaadin.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {

}