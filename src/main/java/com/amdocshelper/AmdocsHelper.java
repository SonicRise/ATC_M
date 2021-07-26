package com.amdocshelper;

import com.amdocshelper.connections.ConnectionData;
import com.amdocshelper.connections.Connections;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class AmdocsHelper {
    private static final Connections connections = new Connections();

    public static void main(String[] args) {
        String test = "10415203514;\n" +
                "10415203516;\n" +
                "10415203518;\n" +
                "10415203520;\n" +
                "10415203522;\n" +
                "10415203524;\n" +
                "10415203526;\n" +
                "10415203534;\n" +
                "10415203536;\n" +
                "10415203538;\n" +
                "10415203542;\n" +
                "10415203543;\n" +
                "10415203776;\n" +
                "10415203777;\n" +
                "10415203778;\n" +
                "10415203779;\n" +
                "10415203780;\n" +
                "10415203508;\n" +
                "10415187565;\n" +
                "10415187567;\n" +
                "10415187570;";

        List<String> lines = Arrays.asList(test.split("\n"));

        lines.stream().map((s) -> {
            s = s.substring(0 , s.length() - 1);
            String subscriberId = getSubscriberId(s);
            String townId = getTownId(subscriberId);
            System.out.println("UPDATE DB.RPR9_USAGE_INTERFACE SET TOWN_ID = " + townId + " WHERE ID = " + s + ";");
            return s;
        }).forEach(System.out::println);

        String line = "UPDATE DB.RPR9_USAGE_INTERFACE SET TOWN_ID = '07002' WHERE ID = 10412499346;";
        String errorIdTest = line.substring(line.indexOf("RE ID") + 8, line.length() - 1);


        /*String errorId = "10415203737";
        String subscriberId = getSubscriberId(errorId);
        System.out.println("SubscriberId: " + subscriberId);
        String townId = getTownId(subscriberId);
        System.out.println("TownId: " + townId);
        System.out.println("Result string: ");
        System.out.println("UPDATE DB.RPR9_USAGE_INTERFACE SET TOWN_ID = " + townId + " WHERE ID = " + errorId + ";");*/
    }

    private static String getSubscriberId(String errorId) {
        /*
         * 0 - dkp1
         */
        ConnectionData connectionData = connections.getConnectionData().get(0);

        try {
            OracleConnection connection = (OracleConnection) getConnection(connectionData);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select e.error, i.* " +
                            "from db.rpr9_error e, db.rpr9_usage_interface i " +
                            "where e.rpr9_usage_interface_id = i.id " +
                            "and e.rpr9_portion_id=i.rpr9_portion_id " +
                            "and i.id = ?");

            preparedStatement.setString(1, errorId);
            return sqlExecutor(preparedStatement, "subscriber_id", errorId);
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.getMessage());
            return null;
        }
    }

    private static String getTownId(String subscriberId) {
        /*
         * 1 - kztusg1
         */
        ConnectionData connectionData = connections.getConnectionData().get(1);

        try {
            OracleConnection connection = (OracleConnection) getConnection(connectionData);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select distinct rpr.town_id " +
                            "from rpr9_usage_interface rpr " +
                            "where 1 = 1 " +
                            "and rpr.subscriber_id in(?) " +
                            "and rpr.cycle_code in (07) " +
                            "and rpr.cycle_month in ('7')" +
                            "and rpr.record_type in ('V')" +
                            "and rpr.rerate_value = '0'");
            preparedStatement.setString(1, subscriberId);
            return sqlExecutor(preparedStatement, "town_id", subscriberId);
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.getMessage());
            return null;
        }
    }

    private static Connection getConnection(ConnectionData connectionData) throws SQLException {
        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, connectionData.getUsername());
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, connectionData.getPassword());

        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setURL(connectionData.getConnectionString());
            ods.setConnectionProperties(info);
            return ods.getConnection();
        } catch (SQLException e) {
            System.out.println("Exception in connection: " + e.getMessage());
            throw new SQLException();
        }
    }

    private static String sqlExecutor(PreparedStatement preparedStatement, String resultColumn, String subId) {
        int count = 0;
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count++;
            }
            
            if (count > 1) {
                System.out.println("//////////////////////// " + subId);
            }
            resultSet.next();
            return resultSet.getString(resultColumn);
        } catch (Exception e) {
            System.out.println("Exception in query: " + e.getMessage());
            return null;
        }
    }
}
