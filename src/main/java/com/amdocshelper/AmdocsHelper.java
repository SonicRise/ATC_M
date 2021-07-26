package com.amdocshelper;

import com.amdocshelper.connections.ConnectionData;
import com.amdocshelper.connections.Connections;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class AmdocsHelper {
    private static final Connections connections = new Connections();

    public static void main(String[] args) {
        String errorId = "10415203737";
        String subscriberId = getSubscriberId(errorId);
        System.out.println("SubscriberId: " + subscriberId);
    }

    private static String getSubscriberId(String errorId){
        /*
         * 0 - dkp1
         */
        ConnectionData connectionData = connections.getConnectionData().get(0);

        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, connectionData.getUsername());
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, connectionData.getPassword());

        //jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL=TCP)(HOST=blah.example.com)(PORT=1521)))(CONNECT_DATA=(SID=BLAHSID)(GLOBAL_NAME=BLAHSID.WORLD)(SERVER=DEDICATED)))
        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setURL(connectionData.getConnectionString());
            ods.setConnectionProperties(info);

            OracleConnection connection = (OracleConnection) ods.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select e.error, i.* " +
                    "from db.rpr9_error e, db.rpr9_usage_interface i " +
                    "where e.rpr9_usage_interface_id = i.id " +
                    "and e.rpr9_portion_id=i.rpr9_portion_id " +
                    "and i.id = ?");

            preparedStatement.setString(1, errorId);
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.getString("subscriber_id");
            } catch (Exception e) {
                System.out.println("Exception in query: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.getMessage());
        }
    }
}
