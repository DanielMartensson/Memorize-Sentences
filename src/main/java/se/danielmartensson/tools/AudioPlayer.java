package se.danielmartensson.tools;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;

@Tag("audio")
public class AudioPlayer extends Component {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AudioPlayer(){
        getElement().setAttribute("controls",true);

    }

    public void setSource(String path){
        getElement().setProperty("src",path);
    }
}