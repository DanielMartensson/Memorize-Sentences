package org.vaadin.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Language {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String language;
}
