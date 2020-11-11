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
import se.danielmartensson.entity.SentenceUpload;
import se.danielmartensson.service.LanguageService;
import se.danielmartensson.service.SentenceService;
import se.danielmartensson.tools.Top;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;


@Route("uploadSentences")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class UploadView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadView(SentenceService sentenceService, LanguageService languageService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		createUploader(sentenceService, languageService);
	}

	private void createUploader(SentenceService sentenceService, LanguageService languageService) {
		// Grid
		List<SentenceUpload> sentenceList = new ArrayList<>();
		Grid<SentenceUpload> grid = new Grid<>(SentenceUpload.class);
		grid.setColumns("sentenceInFrench", "sentenceInOtherLanguage", "language");
		
		MemoryBuffer buffer = new MemoryBuffer();
		Upload upload = new Upload(buffer);
		upload.setMaxFiles(1);
		upload.setDropLabel(new Label("Upload .csv in format frenchSentence,yourSentence,language"));
		upload.setAcceptedFileTypes("text/csv");
		upload.setMaxFileSize(3000);
		Div output = new Div();
			
		// Confirm dialog - ask if we going to inser into the database
		Button insert = new Button("Insert into the database");
		Dialog dialog = new Dialog();
		insert.setEnabled(false);
		insert.addClickListener(e -> dialog.open());
		Button confirmButton = new Button("Yes", event -> {
			for(SentenceUpload sentenceUpload : sentenceList) {
				String sentenceInFrench = sentenceUpload.getSentenceInFrench();
				String sentenceInOtherLanguage = sentenceUpload.getSentenceInOtherLanguage();
				String language = sentenceUpload.getLanguage();
				sentenceService.checkAndSave(sentenceInFrench, sentenceInOtherLanguage, language);
			}
			dialog.close();
		});
		Button cancelButton = new Button("No", event -> {
			dialog.close();
		});
		dialog.add(new Text("Insert into database: "), confirmButton, cancelButton);

		// Listeners for the uploader
		upload.addFileRejectedListener(event -> {
			output.removeAll();
		    Paragraph component = new Paragraph();
		    showOutput(event.getErrorMessage(), component, output);
		});
		upload.addSucceededListener(event -> {
			output.removeAll();
			sentenceList.clear();
		    boolean correctColumnSize = createGrid(buffer.getInputStream(), sentenceList);
		    Paragraph component = new Paragraph();
		    showOutput(event.getFileName(), component, output);
		    if(correctColumnSize) {
			    insert.setEnabled(true);
			    grid.setItems(sentenceList);
		    }
		});
		upload.addDetachListener(event ->{
			output.removeAll();
		});
		
		
		HorizontalLayout firstRow = new HorizontalLayout(upload, insert);
		firstRow.setAlignItems(Alignment.CENTER);
		setContent(new VerticalLayout(firstRow, grid));
		
	}
	
	private boolean createGrid(InputStream stream, List<SentenceUpload> sentenceList) {
		boolean correctColumnSize = false;
	        try {
				String[] csvRows = IOUtils.toString(stream, StandardCharsets.UTF_8).split("\n");
				// Check the first row how many columns
				if(csvRows[0].split(",").length == 3) {
					correctColumnSize = true;
					for(String csvRow : csvRows) {
						String[] columns = csvRow.split(",");
						String sentenceInFrench = columns[0];
						String sentenceInOtherLanguage = columns[1];
						String language = columns[2];
						sentenceList.add(new SentenceUpload(sentenceInFrench, sentenceInOtherLanguage, language));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    return correctColumnSize;
	}
	
	private void showOutput(String text, Component content, HasComponents outputContainer) {
	    HtmlComponent p = new HtmlComponent(Tag.P);
	    p.getElement().setText(text);
	    outputContainer.add(p);
	    outputContainer.add(content);
	}
}
