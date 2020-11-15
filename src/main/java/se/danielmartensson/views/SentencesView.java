package se.danielmartensson.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import se.danielmartensson.entity.TranslateFromTo;
import se.danielmartensson.entity.ForeignLanguageAudioPath;
import se.danielmartensson.entity.Sentence;
import se.danielmartensson.service.TranslateFromToService;
import se.danielmartensson.service.ForeignLanguageAudioPathService;
import se.danielmartensson.service.SentenceService;
import se.danielmartensson.tools.Top;

import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;


@Route("sentences")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class SentencesView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public SentencesView(SentenceService sentenceService, TranslateFromToService translateFromToService, ForeignLanguageAudioPathService foreignLanguageAudioPathService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// crud instance
		GridCrud<Sentence> crud = new GridCrud<>(Sentence.class);
		        
    	// grid configuration
		crud.getGrid().setColumns("sentenceInForeignLanguage", "sentenceInYourLanguage", "translateFromTo", "foreignLanguageAudioPath");
        crud.getGrid().setColumnReorderingAllowed(true);
        
        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties("sentenceInForeignLanguage", "sentenceInYourLanguage", "translateFromTo", "foreignLanguageAudioPath");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.ADD, "sentenceInForeignLanguage", "sentenceInYourLanguage", "translateFromTo", "foreignLanguageAudioPath");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.UPDATE, "sentenceInForeignLanguage", "sentenceInYourLanguage", "translateFromTo", "foreignLanguageAudioPath");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.READ, "sentenceInForeignLanguage", "sentenceInYourLanguage", "translateFromTo", "foreignLanguageAudioPath");
        crud.getCrudFormFactory().setFieldProvider("translateFromTo", new ComboBoxProvider<>("TranslateFromTo", translateFromToService.findAll(), new TextRenderer<>(TranslateFromTo::toString), TranslateFromTo::toString));
        crud.getCrudFormFactory().setFieldProvider("foreignLanguageAudioPath", new ComboBoxProvider<>("ForeignLanguageAudioPath", foreignLanguageAudioPathService.findAll(), new TextRenderer<>(ForeignLanguageAudioPath::getForeignLanguageAudioPath), ForeignLanguageAudioPath::getForeignLanguageAudioPath));
        
        // layout configuration
        setContent(crud);
        
        // logic configuration
        crud.setOperations(
                () -> sentenceService.findAll(),
                sentence -> sentenceService.save(sentence),
                sentence -> sentenceService.save(sentence),
                sentence -> sentenceService.delete(sentence)
        );
        
	}

}
