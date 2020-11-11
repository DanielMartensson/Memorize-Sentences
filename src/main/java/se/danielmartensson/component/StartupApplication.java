package se.danielmartensson.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import se.danielmartensson.entity.Language;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.service.LanguageService;
import se.danielmartensson.service.SentenceService;

@Component
public class StartupApplication implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private SentenceService sentenceService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
	}

}
