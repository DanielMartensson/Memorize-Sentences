package se.danielmartensson.tools;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import se.danielmartensson.views.AudiosView;
import se.danielmartensson.views.TranslateFromToView;
import se.danielmartensson.views.SentencesView;
import se.danielmartensson.views.TrainView;
import se.danielmartensson.views.UploadAudiosView;
import se.danielmartensson.views.UploadSentencesView;

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
        Tab translateFromToTab = new Tab("Translate from to");
        translateFromToTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(TranslateFromToView.class);
        });
        Tab sentencesTab = new Tab("Sentences");
        sentencesTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(SentencesView.class);
        });
        Tab audiosTab = new Tab("Audios");
        audiosTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(AudiosView.class);
        });
        Tab uploadSentencesTab = new Tab("Upload sentences");
        uploadSentencesTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(UploadSentencesView.class);
        });
        Tab uploadAudiosTab = new Tab("Upload audios");
        uploadAudiosTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(UploadAudiosView.class);
        });

        tabs = new Tabs(trainTab, sentencesTab, translateFromToTab, audiosTab, uploadSentencesTab, uploadAudiosTab);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        
	}
	
	public void setTopAppLayout(AppLayout appLayout) {
		appLayout.addToNavbar(drawerToggle, img);
		appLayout.addToDrawer(tabs);
	}
}