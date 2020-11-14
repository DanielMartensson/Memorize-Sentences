package se.danielmartensson.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entity.ForeignLanguageAudioPath;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.entity.TranslateFromTo;
import se.danielmartensson.service.TranslateFromToService;
import se.danielmartensson.service.SentenceService;
import se.danielmartensson.tools.Top;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;


@Route("uploadsentences")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class UploadSentencesView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadSentencesView(SentenceService sentenceService, TranslateFromToService translateFromToService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		createUploader(sentenceService, translateFromToService);
	}

	private void createUploader(SentenceService sentenceService, TranslateFromToService translateFromToService) {
		// Grid
		List<Sentence> sentenceList = new ArrayList<>();
		Grid<Sentence> grid = new Grid<>(Sentence.class);
		grid.setColumns("sentenceInForeignLanguage", "sentenceInYourLanguage", "translateFromTo");
		
		MemoryBuffer buffer = new MemoryBuffer();
		Upload upload = new Upload(buffer);
		upload.setMaxFiles(1);
		upload.setDropLabel(new Label("Upload .csv in format SentenceInForeignLanguage,SentenceInYourLanguage,From,To"));
		upload.setAcceptedFileTypes("text/csv");
		upload.setMaxFileSize(30000); // 30 MB
		Div output = new Div();
			
		// Confirm dialog - ask if we going to insert into the database
		Button insert = new Button("Insert into the database");
		Dialog dialog = new Dialog();
		insert.addClickListener(e -> {
			if(sentenceList.size() > 0) // We need to have something inside the grid
				dialog.open();
		});
		Button confirmButton = new Button("Yes", event -> {
			uploadToDatabase(sentenceList, sentenceService);
			dialog.close();
		});
		Button cancelButton = new Button("No", event -> {
			dialog.close();
		});
		dialog.add(new Text("Insert into database: "), confirmButton, cancelButton);

		// Listeners for the uploader
		upload.addFileRejectedListener(event -> {
		    Paragraph component = new Paragraph();
		    showOutput(event.getErrorMessage(), component, output);
		});
		upload.addSucceededListener(event -> {
		    createGrid(buffer.getInputStream(), sentenceList, grid);
		    Paragraph component = new Paragraph();
		    showOutput(event.getFileName(), component, output);
		});
		
		// Layout
		HorizontalLayout firstRow = new HorizontalLayout(upload, insert);
		firstRow.setAlignItems(Alignment.CENTER);
		setContent(new VerticalLayout(firstRow, grid));
		
	}
	
	private void uploadToDatabase(List<Sentence> sentenceList, SentenceService sentenceService) {
		for(Sentence sentence : sentenceList) {
			String sentenceInForeignLanguage = sentence.getSentenceInForeignLanguage();
			String sentenceInYourLanguage = sentence.getSentenceInYourLanguage();
			String fromLanguage = sentence.getTranslateFromTo().getFromLanguage();
			String toLanguage = sentence.getTranslateFromTo().getToLanguage();
			sentenceService.checkAndSave(sentenceInForeignLanguage, sentenceInYourLanguage, fromLanguage, toLanguage);
		}
	}

	private void createGrid(InputStream stream, List<Sentence> sentenceList, Grid<Sentence> grid) {
	        try {
				String[] csvRows = IOUtils.toString(stream, StandardCharsets.UTF_8).split("\n");
				// Check the first row how many columns
				if(csvRows[0].split(",").length == 4) {
					// Clear the grid first before you fill up the grid again
					sentenceList.clear();
					grid.setItems(sentenceList);
					for(String csvRow : csvRows) {
						String[] columns = csvRow.split(",");
						String sentenceInForeignLanguage = columns[0];
						String sentenceInYourLanguage = columns[1];
						TranslateFromTo yourLanguage = new TranslateFromTo(0L, columns[2], columns[3]);
						ForeignLanguageAudioPath foreignLanguageAudioPath = new se.danielmartensson.entity.ForeignLanguageAudioPath(); // Not going to see this in the grid
						sentenceList.add(new Sentence(0L, sentenceInForeignLanguage, sentenceInYourLanguage, yourLanguage, foreignLanguageAudioPath));
					}
				    grid.setItems(sentenceList); // Insert new data to the grid
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void showOutput(String text, Component content, HasComponents outputContainer) {
	    HtmlComponent p = new HtmlComponent(Tag.P);
	    p.getElement().setText(text);
	    outputContainer.add(p);
	    outputContainer.add(content);
	}
}
