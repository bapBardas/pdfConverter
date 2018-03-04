package pdfExtractorForEli;

/**
 * Created by bap on 04/03/2018.
 */
public enum InvoiceProviders {
    SFR("sfr"),
    BOUYGUES("bouygues"),
    ASTRIUM("astrium"),
    ORANGE("orange");

    private String identifier = "";

    //Constructeur
    InvoiceProviders(String name){
        this.identifier = name;
    }

    public String getIdentifier(){
        return identifier;
    }

    }