package com.amdocshelper;

import com.amdocshelper.connections.ConnectionData;
import com.amdocshelper.connections.Connections;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class AmdocsHelper {
    public static void main(String[] args) {
        /*
         * 0 - dkp1
         */
        Connections connections = new Connections();
        ConnectionData connectionData = connections.getConnectionData().get(0);

        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, connectionData.getUsername());
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, connectionData.getPassword());
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

        //jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL=TCP)(HOST=blah.example.com)(PORT=1521)))(CONNECT_DATA=(SID=BLAHSID)(GLOBAL_NAME=BLAHSID.WORLD)(SERVER=DEDICATED)))
        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setURL(connectionData.getConnectionString());
            ods.setConnectionProperties(info);

            OracleConnection connection = (OracleConnection) ods.getConnection();
            DatabaseMetaData dbmd = connection.getMetaData();
            if (dbmd != null) {
                System.out.println("Driver Name: " + dbmd.getDriverName());
                System.out.println("Driver Version: " + dbmd.getDriverVersion());
                System.out.println("Database Username is: " + connection.getUserName());
            } else {
                System.out.println("null");
            }

            try {
                Statement statement = connection.createStatement();
                try {
                    ResultSet resultSet = statement.executeQuery(
                            "select e.error, i.* " +
                                    "from db.rpr9_error e, db.rpr9_usage_interface  " +
                                    "where e.rpr9_usage_interface_id = i.id " +
                                    "and e.rpr9_portion_id=i.rpr9_portion_id " +
                                    "end i.id = 10415203737");

                    if (resultSet != null) {
                        System.out.println("Size: " + resultSet.getFetchSize());
                    }
                    while (resultSet.next()) {
                        System.out.println(resultSet);
                    }
                } catch (Exception e) {
                    System.out.println("Exception in query: " + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("Exception in statement: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.getMessage());
        }
    }
}
