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

import se.danielmartensson.entity.TranslateFromTo;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.service.TranslateFromToService;
import se.danielmartensson.service.SentenceService;
import se.danielmartensson.tools.AudioPlayer;
import se.danielmartensson.tools.Top;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	public TrainView(SentenceService sentenceService, TranslateFromToService translateFromToService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// Create the select language drop down button
		Select<TranslateFromTo> selectedTranslateFromTo = new Select<>();
		selectedTranslateFromTo.setWidthFull();
		selectedTranslateFromTo.setLabel("Translate from to");
		selectedTranslateFromTo.setItems(translateFromToService.findAll()); // Thanks to modified toString in YourLanguage class
		selectedTranslateFromTo.addValueChangeListener(e -> {
			sentences = sentenceService.findByTranslateFromTo(e.getValue());
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
			String yourAnswer = cleanSentence(sentenceInYourLanguage.getValue());
			boolean correct = false;
			if(!reverseTranslation.getValue()) {
				String theAnswer = cleanSentence(yourSentence);
				correct = theAnswer.equals(yourAnswer); 
			}else {
				String theAnswer = cleanSentence(foreignSentence);
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
			    	String audioPath = "Source/Audios/" + selectedTranslateFromTo.getValue().getFromLanguage() + "/" +  foreignSentence + ".mp3";
					AbstractStreamResource resource = new StreamResource(foreignSentence, () -> {
							try {
								return new FileInputStream(audioPath);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							return null;
						});
					if(resource != null)
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
		HorizontalLayout reverse_check = new HorizontalLayout(checkSentence, reverseTranslation);
		reverse_check.setAlignItems(Alignment.CENTER);
		VerticalLayout layout = new VerticalLayout(selectedTranslateFromTo, player, sentenceInForeignLanguage, sentenceInYourLanguage, new HorizontalLayout(nextSentence, seeTheAnswer), reverse_check);
		setContent(layout);
		
	}

	private String cleanSentence(String sentence) {
		sentence = sentence.toLowerCase(); // No case sensitive
		sentence = sentence.replace(" ", ""); // No space sensitive
		sentence = sentence.replace("(f)", ""); // No feminine "(f)" mark
		sentence = sentence.replace("(i)", ""); // No informal "(i)" mark
		return sentence;
		
	}
}
