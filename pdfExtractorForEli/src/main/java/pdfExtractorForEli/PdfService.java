package pdfExtractorForEli;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PdfService {

    @Autowired
    private MmiController mmi;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Value("${invoices.directory.input}")
    private  String inputFolder;

    public String getInputFolder() { return inputFolder;}
    public String getOutputFolder() { return outputFolder;}

    @Value("${invoices.directory.output}")
    private  String outputFolder;



    public ArrayList<File> getPdfs(){
        File dir = new File(inputFolder);
        ArrayList<File> result = new ArrayList<>(Arrays.asList(dir.listFiles()));
        result = result.stream()
                .filter(file -> file.getName().contains(".pdf"))
                .collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public void processPdfs(){
        createOutputDirIfNecessary();
        for(File input : getPdfs()){
            processPdf(input);
        }
    }

    private void createOutputDirIfNecessary() {
        File file = new File(outputFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("output directory created!");
            } else {
                System.out.println("Failed to create output directory!");
            }
        }
    }

    private File getFile(String pathname) {
        return new File(pathname);
    }

    public void processPdf(File input) {

        PDDocument pd;
        BufferedWriter wr;
        String outputName = outputFolder+input.getName().replace(".pdf",".txt");
        File output = getFile(outputName);


        try {
            pd = PDDocument.load(input);
            LOGGER.info("------------------ Processing : " +input.getName());
            LOGGER.info("nb of pages: " + pd.getNumberOfPages());
            InvoiceProviders invoiceType = whichProvider(pd);
            PDFTextStripper stripper = new PDFTextStripper();
            wr = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(output)));
            stripper.writeText(pd, wr);

            if (pd != null) {
                pd.close();
            }
            // I use close() to flush the stream.
            wr.close();

        } catch (IOException e) {
            LOGGER.error("Couldn't load PDF file : "+input.getName());
            e.printStackTrace();
        }
    }

    private InvoiceProviders whichProvider(PDDocument document) throws IOException {
        PDFTextStripper pdfStripper = new PDFTextStripper();
        final String pdfText = pdfStripper.getText(document).toLowerCase();
        InvoiceProviders result = Arrays.asList(InvoiceProviders.values()).stream()
                .filter(provider -> pdfText.contains(provider.getIdentifier()))
                .findFirst().orElse(InvoiceProviders.ASTRIUM);
        return result;
    }

}
