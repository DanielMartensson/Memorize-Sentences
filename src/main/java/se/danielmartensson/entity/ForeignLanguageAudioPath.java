package se.danielmartensson.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignLanguageAudioPath {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String fromLanguage;
	
	@NotNull
	private String fileName;
	
	@NotNull
	private String foreignLanguageAudioPath;

	@Override
	public String toString() {
		return foreignLanguageAudioPath; // We only want to see this field in the grid
	}
}
