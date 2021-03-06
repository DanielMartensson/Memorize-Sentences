package se.danielmartensson.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entity.ForeignLanguageAudioPath;
import se.danielmartensson.service.ForeignLanguageAudioPathService;
import se.danielmartensson.tools.Top;

import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;


@Route("audios")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class AudiosView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AudiosView(ForeignLanguageAudioPathService foreignLanguageAudioPathService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// crud instance
		GridCrud<ForeignLanguageAudioPath> crud = new GridCrud<>(ForeignLanguageAudioPath.class);
		
		// grid configuration
		crud.getGrid().setColumns("fromLanguage", "fileName", "foreignLanguageAudioPath");
        crud.getGrid().setColumnReorderingAllowed(true);
        
        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties("fromLanguage", "fileName", "foreignLanguageAudioPath");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.DELETE, "fromLanguage", "fileName", "foreignLanguageAudioPath");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.ADD, "fromLanguage", "fileName", "foreignLanguageAudioPath");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.UPDATE, "fromLanguage", "fileName", "foreignLanguageAudioPath");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.READ, "fromLanguage", "fileName", "foreignLanguageAudioPath");
        
        // layout configuration
        setContent(crud);
        
        // logic configuration
        crud.setOperations(
                () -> foreignLanguageAudioPathService.findAll(),
                language -> foreignLanguageAudioPathService.save(language),
                language -> foreignLanguageAudioPathService.save(language),
                language -> foreignLanguageAudioPathService.delete(language)
        );   
	}
}
