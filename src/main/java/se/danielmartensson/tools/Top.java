package se.danielmartensson.tools;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import se.danielmartensson.views.AddLanguageView;
import se.danielmartensson.views.AddSentenceView;
import se.danielmartensson.views.TrainView;

public class Top {
	
	private Tabs tabs;
	private Image img;
	private DrawerToggle drawerToggle;

	public Top() {
		// Header
		img = new Image("img/barPicture.png", "Learn French");
        img.setHeight("44px");
        drawerToggle = new DrawerToggle();
        
        // Tabs and its listener
        Tab trainTab = new Tab("Train");
        trainTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(TrainView.class);
        });
        Tab addLanguageTab = new Tab("Add language");
        addLanguageTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(AddLanguageView.class);
        });
        Tab addSentenceTab = new Tab("Add sentence");
        addSentenceTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(AddSentenceView.class);
        });

        tabs = new Tabs(trainTab, addSentenceTab, addLanguageTab);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        
	}
	
	public void setTopAppLayout(AppLayout appLayout) {
		appLayout.addToNavbar(drawerToggle, img);
		appLayout.addToDrawer(tabs);
	}
}