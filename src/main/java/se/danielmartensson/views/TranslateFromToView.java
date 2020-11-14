package se.danielmartensson.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import se.danielmartensson.entity.TranslateFromTo;
import se.danielmartensson.service.TranslateFromToService;
import se.danielmartensson.tools.Top;

import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;


@Route("translatefromto")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class TranslateFromToView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TranslateFromToView(TranslateFromToService translateFromToService) {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// crud instance
		GridCrud<TranslateFromTo> crud = new GridCrud<>(TranslateFromTo.class);
		
		// grid configuration
		crud.getGrid().setColumns("fromLanguage", "toLanguage");
        crud.getGrid().setColumnReorderingAllowed(true);
        
        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties("fromLanguage", "toLanguage");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.ADD, "fromLanguage", "toLanguage");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.READ, "fromLanguage", "toLanguage");
        crud.getCrudFormFactory().setDisabledProperties(CrudOperation.UPDATE, "fromLanguage", "toLanguage");
		
        // layout configuration
        setContent(crud);
        
        // logic configuration
        crud.setOperations(
                () -> translateFromToService.findAll(),
                language -> translateFromToService.save(language),
                language -> translateFromToService.save(language),
                language -> translateFromToService.delete(language)
        );   
	}
}
