package core.ontology;

import core.model.ResponseModel;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.javatuples.Pair;

import java.io.OutputStream;
import java.util.List;

public class OntologyReader {
    /***********************************/
    /* Constants                       */
    /***********************************/
    OntModel m;
    String botPreviewName;

    public static final String MAIN_NS = "http://coronavirusawareness.rdf#";
    String prefix = "prefix main: <" + MAIN_NS + ">\n" +
            "prefix rdfs: <" + RDFS.getURI() + ">\n" +
            "prefix owl: <" + OWL.getURI() + ">\n";
    public OntologyReader(){
        m = getModel();
        loadData( m );
        showQuery( m,
                prefix +
                        "SELECT ?subject ?object\n" +
                        "\tWHERE { ?subject rdfs:subClassOf ?object }" );
    }

    // Directory where we've stored the local data files, such as pizza.rdf.owl
    public static final String SOURCE = "./src/main/resources/data/";

    // Pizza ontology namespace

    public void run(String[] arr,String botName) {
        botPreviewName = botName;
        String response;
        try {
            String messegeType = arr[1];


            if ("T01".equals(messegeType)) {
                response = arr[2];
                String value = arr[3].replaceAll(" ", "_");
                showQuery( m,
                        prefix +
                                "SELECT ?symptoms\n" +
                                "\tWHERE {  main:"+value+" main:cause_symptoms ?symptoms }" ,response);


            } else if ("T02".equals(messegeType)) {
                response = arr[2];
                String value = arr[3].replaceAll(" ", "_");
                showQuery( m,
                        prefix +
                                "SELECT ?infections\n" +
                                "\tWHERE {  ?infections main:puts_into_risk main:"+value+" }" ,response);
            } else if ("T03".equals(messegeType)) {
                response = arr[2];
                String value = arr[3].replaceAll(" ", "_");
                showQuery( m,
                        prefix +
                                "SELECT *\n" +
                                "\tWHERE {   main:"+value+" main:puts_into_risk ?reason .\n" +
                                " ?reason main:type ?type . }" ,response);
            } else if ("T04".equals(messegeType)) {
                response = arr[2];
                String value = arr[3].replaceAll(" ", "_");
                showQuery( m,
                        prefix +
                                "SELECT DISTINCT ?Risk_of_having ?Prevention_Methods\n" +
                                "\tWHERE {   ?Risk_of_having main:cause_symptoms main:"+value+" .\n" +
                                "OPTIONAL { \n" +
                                "?Risk_of_having main:can_be_prevented_by ?prevention .\n" +
                                "?prevention main:description ?Prevention_Methods .\n" +
                                "} }" ,response);
            } else if ("T05".equals(messegeType)) {
                response = arr[2];
                String value = arr[3].replaceAll(" ", "_");
                showQuery( m,
                        prefix +
                                "SELECT ?Truth_about_"+value+"\n" +
                                "\tWHERE {  main:"+value+" main:has_wrong_information ?header .\n" +
                                "?header main:description ?Truth_about_"+value+" }" ,response);


            } else {
                throw  new Exception();
            }

        }catch (Exception Ex){
            System.out.println("Ex :" + Ex.getLocalizedMessage());
            response = "Sorry! I have difficulty understanding";
            System.out.println(botPreviewName+": " + response);
        }

    }

    protected OntModel getModel() {
        return ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
    }

    protected void showQuery(Model m, String q ) {

    }

    protected void showQuery(Model m, String q ,String s) {
        Query query = QueryFactory.create( q );
        QueryExecution qexec = QueryExecutionFactory.create( query, m );
        try {
            ResultSet results = qexec.execSelect();
            System.out.println(botPreviewName+": " + s);

            ResultSetFormatter.out( results, m );

        }catch (Exception ex){
        }
        finally {
            qexec.close();
        }

    }

    protected void loadData( Model m ) {
        FileManager.get().readModel( m, SOURCE + "main.owl" );
    }

}
