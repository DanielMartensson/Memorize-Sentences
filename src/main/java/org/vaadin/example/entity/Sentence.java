package org.vaadin.example.entity;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Sentence {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String wordInFrench;
	
	@NotNull
	private String wordInOtherLanguage;
	
	@ManyToOne
	@NotNull
	private Language languages;
	
}
