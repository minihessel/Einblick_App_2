/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import javafx.concurrent.Task;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Eskil
 */
public class SQL_manager {

    public static Connection conn;
    public ResultSet rs;
    public String instanceName;
    public Boolean connected;
    public Task<Boolean> task;
    public Task<Boolean> service;

    public SQL_manager() {

    }

    public void getAllTables(ChoiceBox choiceBox) throws SQLException {
        //Henter ut alle tabellene i databasen ved hjelp av metadata
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        while (rs.next()) {
            System.out.println(rs.getString(3));
            //deretter legger jeg til alle tabellene i en choicebox så brukeren kan velge hvilken tabell
            choiceBox.getItems().add(rs.getString(3));

        }

    }

    public void getConnection(String mySqlAdress, String myPort, String sqlInstance) throws SQLException, ClassNotFoundException, InterruptedException, ExecutionException {

        task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Boolean result = null;
                try {
                    instanceName = sqlInstance;
                    Class.forName("com.mysql.jdbc.Driver").newInstance();

                    System.out.println("Driver Loaded.");
                    String myUrl = "jdbc:mysql://" + mySqlAdress + ":" + myPort + "/" + sqlInstance + "?socketTimeout=3000&connectTimeout=3000";
                    DriverManager.setLoginTimeout(10);
                    conn = DriverManager.getConnection(myUrl, "root", "root");

                    System.out.println("Got Connection. " + conn);
                    result = true;
                } catch (SQLException e) {
                    System.out.println("Driver Loaded FAILED.");
                    result = false;
                }
                return result;
            }

        };

        new Thread(task).start();

        //Thread completed, end main thread
        System.out.println("End");

    }

    public void getDataFromSQL(String SQL) throws SQLException {

        try {
            System.out.println(conn + "blabla");
            rs = conn.createStatement().executeQuery(SQL);

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());

        }

    }
}
