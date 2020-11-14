package se.danielmartensson.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;

import se.danielmartensson.entity.YourLanguage;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.service.LanguageService;
import se.danielmartensson.service.SentenceService;
import se.danielmartensson.tools.AudioPlayer;
import se.danielmartensson.tools.Top;

import java.util.List;
import java.util.Random;


@Route("")
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class TrainView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Sentence> sentences = null;
	private int amoutOfSentences = 0;
	private String foreignSentence = "";
	private String yourSentence = "";

	public TrainView(SentenceService sentenceService, LanguageService languageService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// Create the select language drop down button
		Select<YourLanguage> seletedLanguage = new Select<>();
		seletedLanguage.setWidthFull();
		seletedLanguage.setLabel("Select your language");
		seletedLanguage.setItems(languageService.findAll()); // Thanks to modified toString in YourLanguage class
		seletedLanguage.addValueChangeListener(e -> {
			sentences = sentenceService.findByYourLanguage(e.getValue());
			amoutOfSentences = sentences.size();
		});
	
		// Text fields
		TextField sentenceInForeignLanguage = new TextField();
		sentenceInForeignLanguage.setLabel("Sentence in foreign language");
		sentenceInForeignLanguage.setWidthFull();
		sentenceInForeignLanguage.setEnabled(false);
		TextField sentenceInYourLanguage = new TextField();
		sentenceInYourLanguage.setLabel("Sentence in your language");
		sentenceInYourLanguage.setWidthFull();
		
		// Audio player
		AudioPlayer player = new AudioPlayer();
		
		// Checkbox if we want to do a reverse translation
		Checkbox reverseTranslation = new Checkbox("Reverse", false);
		reverseTranslation.addValueChangeListener(e -> {
			if(e.getValue()) {
				sentenceInForeignLanguage.setLabel("Sentence in your language");
				sentenceInYourLanguage.setLabel("Sentence in foreign language");
				player.setVisible(false);
			}else {
				sentenceInYourLanguage.setLabel("Sentence in your language");
				sentenceInForeignLanguage.setLabel("Sentence in foreign language");
				player.setVisible(true);
			}
			sentenceInForeignLanguage.setValue("");
			sentenceInYourLanguage.setValue("");
			
		});
		
		// Check Sentence in your language
		Button checkSentence = new Button("Check");
		checkSentence.addClickListener(e -> {
			String yourAnswer = sentenceInYourLanguage.getValue().toLowerCase().replace(" ", "");
			boolean correct = false;
			if(!reverseTranslation.getValue()) {
				String theAnswer = yourSentence.toLowerCase().replace(" ", "");
				correct = theAnswer.equals(yourAnswer); 
			}else {
				String theAnswer = foreignSentence.toLowerCase().replace(" ", "");
			    correct = theAnswer.equals(yourAnswer);
			}
			if(correct && yourAnswer.length() > 0) {
				checkSentence.getStyle().set("background-color","#c4f8b5"); // Green
			}else {
				checkSentence.getStyle().set("background-color","#f8bcb5"); // Red
			}
		});

		// Create next sentence button
		Button nextSentence = new Button("Next sentence");
		nextSentence.addClickListener(e -> {
			if(sentences != null) {
				int sentenceNumber = new Random().nextInt(amoutOfSentences);
				foreignSentence = sentences.get(sentenceNumber).getSentenceInForeignLanguage();
			    yourSentence = sentences.get(sentenceNumber).getSentenceInYourLanguage();
			    if(!reverseTranslation.getValue()) {
			    	sentenceInForeignLanguage.setValue(foreignSentence);
			    	String audioPath = "/META-INF/resources/audio/" + seletedLanguage.getValue() + "/" +  foreignSentence + ".mp3";
			    	AbstractStreamResource resource = new StreamResource(foreignSentence, () -> getClass().getResourceAsStream(audioPath));
			    	player.getElement().setAttribute("src", resource);
			    }else {
			    	sentenceInForeignLanguage.setValue(yourSentence);
			    }
			    sentenceInYourLanguage.setValue(""); // Auto clear
			    checkSentence.getStyle().set("background-color", null); // Normal
			}
		});
		
		// See the answer
		Button seeTheAnswer = new Button("See the answer");
		seeTheAnswer.addClickListener(e -> {
			if(!reverseTranslation.getValue()) {
				sentenceInYourLanguage.setValue(yourSentence);
			}else {
				sentenceInYourLanguage.setValue(foreignSentence);
			}
		});

		// Layout
		HorizontalLayout checkBox_player = new HorizontalLayout(reverseTranslation, player);
		checkBox_player.setAlignItems(Alignment.CENTER);
		VerticalLayout layout = new VerticalLayout(seletedLanguage, checkBox_player, sentenceInForeignLanguage, sentenceInYourLanguage, new HorizontalLayout(nextSentence, seeTheAnswer), checkSentence);
		setContent(layout);
		
	}
}
