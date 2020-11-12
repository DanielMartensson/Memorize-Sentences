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
import com.vaadin.flow.server.PWA;

import se.danielmartensson.entity.Language;
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
	private String sentenceInFrench = "";
	private String sentenceInOtherLanguage = "";

	public TrainView(SentenceService sentenceService, LanguageService languageService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// Create the select language drop down button
		Select<Language> seletedLanguage = new Select<>();
		seletedLanguage.setWidthFull();
		seletedLanguage.setLabel("Select your language");
		seletedLanguage.setItems(languageService.findAll()); // Thanks to modified toString in Language class
		seletedLanguage.addValueChangeListener(e -> {
			sentences = sentenceService.findByLanguage(e.getValue());
			amoutOfSentences = sentences.size();
		});
	
		// Text fields
		TextField frenchSentence = new TextField();
		frenchSentence.setLabel("Français");
		frenchSentence.setWidthFull();
		frenchSentence.setEnabled(false);
		TextField yourTranslation = new TextField();
		yourTranslation.setLabel("Your translation");
		yourTranslation.setWidthFull();
		
		// Audio player
		AudioPlayer player = new AudioPlayer();
		
		// Checkbox if we want to do a reverse translation
		Checkbox reverseTranslation = new Checkbox("Reverse", false);
		reverseTranslation.addValueChangeListener(e -> {
			if(e.getValue()) {
				frenchSentence.setLabel("Your translation");
				yourTranslation.setLabel("Français");
				player.setVisible(false);
			}else {
				yourTranslation.setLabel("Your translation");
				frenchSentence.setLabel("Français");
				player.setVisible(true);
			}
			frenchSentence.setValue("");
			yourTranslation.setValue("");
			
		});
		

		
		// Check your translation
		Button checkSentence = new Button("Check");
		checkSentence.addClickListener(e -> {
			String yourAnser = yourTranslation.getValue().toLowerCase().replace(" ", "");
			boolean correct = false;
			if(!reverseTranslation.getValue()) {
				String theAnswer = sentenceInOtherLanguage.toLowerCase().replace(" ", "");
				correct = theAnswer.contains(yourAnser); 
			}else {
				String theAnswer = sentenceInFrench.toLowerCase().replace(" ", "");
			    correct = theAnswer.contains(yourAnser);
			}
			if(correct && yourAnser.length() > 0) {
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
				sentenceInFrench = sentences.get(sentenceNumber).getSentenceInFrench();
			    sentenceInOtherLanguage = sentences.get(sentenceNumber).getSentenceInOtherLanguage();
			    if(!reverseTranslation.getValue()) {
			    	frenchSentence.setValue(sentenceInFrench);
			    	player.setSource("languages/French Audio/" + sentenceInFrench + ".mp3");
			    	System.out.println("languages/French Audio/" + sentenceInFrench + ".mp3");
			    }else {
			    	frenchSentence.setValue(sentenceInOtherLanguage);
			    }
			    yourTranslation.setValue(""); // Auto clear
			    checkSentence.getStyle().set("background-color", null); // Normal
			}
		});
		
		// See the answer
		Button seeTheAnswer = new Button("See the answer");
		seeTheAnswer.addClickListener(e -> {
			if(!reverseTranslation.getValue()) {
				yourTranslation.setValue(sentenceInOtherLanguage);
			}else {
				yourTranslation.setValue(sentenceInFrench);
			}
		});
		


		// Layout
		HorizontalLayout checkBox_player = new HorizontalLayout(reverseTranslation, player);
		checkBox_player.setAlignItems(Alignment.CENTER);
		VerticalLayout layout = new VerticalLayout(seletedLanguage, checkBox_player, frenchSentence, yourTranslation, new HorizontalLayout(nextSentence, seeTheAnswer), checkSentence);
		setContent(layout);
		
	}
}
