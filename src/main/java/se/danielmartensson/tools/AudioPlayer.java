package se.danielmartensson.tools;

import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;

@Tag("audio")
public class AudioPlayer extends HtmlContainer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final PropertyDescriptor<String, String> srcDescriptor = PropertyDescriptors.attributeWithDefault("src", "");

    public AudioPlayer() {
        super();
        getElement().setProperty("controls", true);
    }

    public AudioPlayer(String src) {
        setSrc(src);
        getElement().setProperty("controls", true);
    }

    public String getSrc() {
        return get(srcDescriptor);
    }

    public void setSrc(String src) {
        set(srcDescriptor, src);
    }
}