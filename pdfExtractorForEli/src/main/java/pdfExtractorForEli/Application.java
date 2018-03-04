package pdfExtractorForEli;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport {



	public static void main(String[] args)  {
        Application.launch(Application.class,MmiView.class,args);
	}
}


