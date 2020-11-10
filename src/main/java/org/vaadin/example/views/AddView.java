package org.vaadin.example.views;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.example.entity.Sentence;
import org.vaadin.example.service.SentenceService;


@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class AddView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddView(SentenceService sentencesService) {
		// crud instance
		GridCrud<Sentence> crud = new GridCrud<>(Sentence.class);
		
		// grid configuration
		crud.getGrid().setColumns("wordInFrench", "wordInOtherLanguage", "language");
        crud.getGrid().setColumnReorderingAllowed(true);
        
        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties("wordInFrench", "wordInOtherLanguage", "language");
        
        // layout configuration
        setContent(crud);
        crud.setFindAllOperationVisible(false);
        
        // logic configuration
        crud.setOperations(
                () -> sentencesService.findAll(),
                sentences -> sentencesService.save(sentences),
                sentences -> sentencesService.save(sentences),
                sentences -> sentencesService.delete(sentences)
        );
        
	}
 

}
