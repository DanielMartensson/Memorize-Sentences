package se.danielmartensson.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import se.danielmartensson.entity.Language;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.service.LanguageService;
import se.danielmartensson.service.SentenceService;
import se.danielmartensson.tools.Top;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;


@Route("addSentence")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class AddSentenceView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public AddSentenceView(SentenceService sentenceService, LanguageService languageService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// crud instance
		GridCrud<Sentence> crud = new GridCrud<>(Sentence.class);
		        
    	// grid configuration
		crud.getGrid().setColumns("wordInFrench", "wordInOtherLanguage", "language");
        crud.getGrid().setColumnReorderingAllowed(true);
        
        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties("wordInFrench", "wordInOtherLanguage", "language");
        crud.getCrudFormFactory().setFieldProvider("language",
                new ComboBoxProvider<>("Language", languageService.findAll(), new TextRenderer<>(Language::getLanguage), Language::getLanguage));
        
        // layout configuration
        setContent(crud);
        crud.setFindAllOperationVisible(false);
        
        // logic configuration
        crud.setOperations(
                () -> sentenceService.findAll(),
                sentence -> sentenceService.save(sentence),
                sentence -> sentenceService.save(sentence),
                sentence -> sentenceService.delete(sentence)
        );
        
	}
 

}
