package pdfExtractorForEli;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@FXMLController
@Component
public class MmiController {

    @FXML
    private Label outputFolderValue;

    @FXML
    private Label inputFolderValue;

    @FXML
    private Label result;

    @Autowired
    private PdfService pdfService;

    @FXML
    void initialize(){
        inputFolderValue.setText(pdfService.getInputFolder());
        outputFolderValue.setText(pdfService.getOutputFolder());
    }



    @FXML
    public void reactOnClick(final Event e) {
        pdfService.processPdfs();
        result.setText("C'est Fini !");
    }

}