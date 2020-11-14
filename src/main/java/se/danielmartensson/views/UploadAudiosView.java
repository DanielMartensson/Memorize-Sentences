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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entity.ForeignLanguageAudioPath;
import se.danielmartensson.entity.TranslateFromTo;
import se.danielmartensson.service.TranslateFromToService;
import se.danielmartensson.service.ForeignLanguageAudioPathService;
import se.danielmartensson.service.SentenceService;
import se.danielmartensson.tools.Top;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;


@Route("uploadaudios")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class UploadAudiosView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadAudiosView(ForeignLanguageAudioPathService foreignLanguageAudioPathService, TranslateFromToService translateFromToService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		createUploader(foreignLanguageAudioPathService, translateFromToService);
	}

	private void createUploader(ForeignLanguageAudioPathService foreignLanguageAudioPathService, TranslateFromToService translateFromToService) {
		// Upload
		MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
		Upload upload = new Upload(buffer);
		upload.setDropLabel(new Label("Upload .mp3 files that corresponds to the sentences"));
		//upload.setAcceptedFileTypes("audio/mp3");
		upload.setMaxFileSize(30000); // 30 MB
		Div output = new Div();
		
		// Language
		Select<String> selectedFromLanguage = new Select<>();
		selectedFromLanguage.setLabel("Select your foreign language");
		selectedFromLanguage.setItems(translateFromToService.findAllFromLanguage());
			
		// Listeners for the uploader
		upload.addFileRejectedListener(event -> {
		    Paragraph component = new Paragraph();
		    showOutput(event.getErrorMessage(), component, output);
		});
		upload.addSucceededListener(event -> {
			if(saveMP3Files(event.getMIMEType(), event.getFileName(), buffer, selectedFromLanguage, foreignLanguageAudioPathService)) {
				Paragraph component = new Paragraph();
				showOutput(event.getFileName(), component, output);
			}
		});
		
		// Layout
		HorizontalLayout firstRow = new HorizontalLayout(upload, selectedFromLanguage);
		setContent(firstRow);
		
	}
	
	private boolean saveMP3Files(String mimeType, String fileName, MultiFileMemoryBuffer buffer, Select<String> selectedFromLanguage, ForeignLanguageAudioPathService foreignLanguageAudioPathService) {
		// We need to have selected a language first
		boolean success = false;
		if(selectedFromLanguage.getValue() == null) {
			new Notification("Please, select a language first", 3000).open();
			return success;
		}
		
		try {
			// Save the MP3 file
			String audioPath = "Audio/" + selectedFromLanguage.getValue() + "/" + fileName;
			Path path = Paths.get(audioPath);
			Files.createDirectories(path.getParent()); // If not exist
			FileOutputStream fos = new FileOutputStream(audioPath);
		    fos.write(buffer.getInputStream(fileName).readAllBytes());
		    fos.close();
		    
		    // Save the audio path
		    ForeignLanguageAudioPath foreignLanguageAudioPath = foreignLanguageAudioPathService.findByFileName(fileName);
		    foreignLanguageAudioPath.setForeignLanguageAudioPath(audioPath);
		    foreignLanguageAudioPathService.save(foreignLanguageAudioPath);
		    
		    success = true;
		    return success;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	

	private void showOutput(String text, Component content, HasComponents outputContainer) {
	    HtmlComponent p = new HtmlComponent(Tag.P);
	    p.getElement().setText(text);
	    outputContainer.add(p);
	    outputContainer.add(content);
	}
}
