package se.danielmartensson.entity;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentenceUpload {
	// These fields are for the grid inside UploadView
	private String sentenceInFrench;
	private String sentenceInOtherLanguage;
	private String language;
	
}
