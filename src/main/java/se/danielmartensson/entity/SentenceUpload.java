package se.danielmartensson.entity;

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
