package se.danielmartensson.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import se.danielmartensson.entity.Language;
import se.danielmartensson.service.LanguageService;
import se.danielmartensson.tools.Top;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.impl.GridCrud;


@Route("addLanguage")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class AddLanguageView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddLanguageView(LanguageService languageService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// crud instance
		GridCrud<Language> crud = new GridCrud<>(Language.class);
		
		// grid configuration
		crud.getGrid().setColumns("language");
        crud.getGrid().setColumnReorderingAllowed(true);
        
        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties("language");
		
        // layout configuration
        setContent(crud);
        crud.setFindAllOperationVisible(false);
        
        // logic configuration
        crud.setOperations(
                () -> languageService.findAll(),
                language -> languageService.save(language),
                language -> languageService.save(language),
                language -> languageService.delete(language)
        );
        
	}
 

}
