package core.model;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class ResponseModel {


    private ResultSet resultSet = null;
    private Model model = null;
    private String response = null;

    public ResponseModel(ResultSet resultSet, Model model, String response) {
        this.resultSet = resultSet;
        this.model = model;
        this.response = response;
    }

    public ResponseModel(String response) {
        this.response = response;
    }


    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
