import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    static PartnerConnection connection;
    static Properties prop;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        prop = new Properties();
        InputStream in = Main.class.getResourceAsStream("conf/app.properties");
        try {
            prop.load(in);
            in.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(prop.getProperty("sfdc.user"));
        config.setPassword(prop.getProperty("sfdc.password"));
        //config.setTraceMessage(true);

        try {

            connection = Connector.newConnection(config);

            // display some current settings
            System.out.println("Auth EndPoint: "+config.getAuthEndpoint());
            System.out.println("Service EndPoint: "+config.getServiceEndpoint());
            System.out.println("Username: "+config.getUsername());
            System.out.println("SessionId: "+config.getSessionId());

            // run the different examples
            queryContacts();

            //createAccounts();

            //updateAccounts();

            //deleteAccounts();


        } catch (ConnectionException e1) {
            e1.printStackTrace();
        }

    }

    // queries and displays the 5 newest contacts
    private static void queryContacts() {

        System.out.println("Querying for the 5 newest Contacts...");

        try {

            // query for the 5 newest contacts
            QueryResult queryResults = connection.query("SELECT Id, FirstName, LastName, Account.Name " +
                    "FROM Contact WHERE AccountId != NULL ORDER BY CreatedDate DESC LIMIT 5");
            if (queryResults.getSize() > 0) {
                for (SObject s: queryResults.getRecords()) {
                    System.out.println("Id: " + s.getId() + " " + s.getField("FirstName") + " " +
                            s.getField("LastName") + " - " + s.getChild("Account").getField("Name"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void createAccounts(int amount) {
        System.out.println("Creating 5 new test Accounts...");
        SObject[] records = new SObject[5];

        try {

            // create 5 test accounts
            for (int i=0;i<amount;i++) {
                SObject so = new SObject();
                so.setType("Account");
                so.setField("Name", "Account "+i);
                records[i] = so;
            }


            // create the records in Salesforce.com
            SaveResult[] saveResults = connection.create(records);

            // check the returned results for any errors
            for (int i=0; i< saveResults.length; i++) {
                if (saveResults[i].isSuccess()) {
                    System.out.println(i+". Successfully created record - Id: " + saveResults[i].getId());
                } else {
                    Error[] errors = saveResults[i].getErrors();
                    for (int j=0; j< errors.length; j++) {
                        System.out.println("ERROR creating record: " + errors[j].getMessage());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create 5 test Accounts
    private static void createAccounts() {
        createAccounts(5);
    }

    // updates the 5 newly created Accounts
    private static void updateAccounts() {

        System.out.println("Update the 5 new test Accounts...");
        SObject[] records = new SObject[5];

        try {

            QueryResult queryResults = connection.query("SELECT Id, Name FROM Account ORDER BY " +
                    "CreatedDate DESC LIMIT 5");
            if (queryResults.getSize() > 0) {
                for (int i=0;i<queryResults.getRecords().length;i++) {
                    SObject so = (SObject)queryResults.getRecords()[i];
                    System.out.println("Updating Id: " + so.getId() + " - Name: "+so.getField("Name"));
                    // create an sobject and only send fields to update
                    SObject soUpdate = new SObject();
                    soUpdate.setType("Account");
                    soUpdate.setId(so.getId());
                    soUpdate.setField("Name", so.getField("Name")+" -- UPDATED");
                    records[i] = soUpdate;
                }
            }


            // update the records in Salesforce.com
            SaveResult[] saveResults = connection.update(records);

            // check the returned results for any errors
            for (int i=0; i< saveResults.length; i++) {
                if (saveResults[i].isSuccess()) {
                    System.out.println(i+". Successfully updated record - Id: " + saveResults[i].getId());
                } else {
                    Error[] errors = saveResults[i].getErrors();
                    for (int j=0; j< errors.length; j++) {
                        System.out.println("ERROR updating record: " + errors[j].getMessage());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // delete the 5 newly created Account
    private static void deleteAccounts() {

        System.out.println("Deleting the 5 new test Accounts...");
        String[] ids = new String[5];

        try {

            QueryResult queryResults = connection.query("SELECT Id, Name FROM Account ORDER BY " +
                    "CreatedDate DESC LIMIT 5");
            if (queryResults.getSize() > 0) {
                for (int i=0;i<queryResults.getRecords().length;i++) {
                    SObject so = (SObject)queryResults.getRecords()[i];
                    ids[i] = so.getId();
                    System.out.println("Deleting Id: " + so.getId() + " - Name: "+so.getField("Name"));
                }
            }


            // delete the records in Salesforce.com by passing an array of Ids
            DeleteResult[] deleteResults = connection.delete(ids);

            // check the results for any errors
            for (int i=0; i< deleteResults.length; i++) {
                if (deleteResults[i].isSuccess()) {
                    System.out.println(i+". Successfully deleted record - Id: " + deleteResults[i].getId());
                } else {
                    Error[] errors = deleteResults[i].getErrors();
                    for (int j=0; j< errors.length; j++) {
                        System.out.println("ERROR deleting record: " + errors[j].getMessage());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
