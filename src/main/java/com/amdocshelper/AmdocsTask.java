package com.amdocshelper;

import com.amdocshelper.connections.ConnectionData;
import com.amdocshelper.connections.Connections;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AmdocsTask {
    private static final Connections connections = new Connections();

    /*
     * 0 - dkp1
     * 1 - kztusg1
     */
    private static final ConnectionData connectionDataDkp1 = connections.getConnectionData().get(0);
    private static final ConnectionData connectionDataKztusg1 = connections.getConnectionData().get(1);

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        List<String> distinctLines = new ArrayList<>();
        try {
            Stream<String> fileStrings = Files.lines(Paths.get("src/main/resources/AmdocsTask.txt"));
            lines = fileStrings.collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Parsing exception: " + e.getMessage());
        }

        List<String> formattedLines = lines.stream().map(l -> l = l.substring(l.indexOf("RE ID") + 8, l.length() - 1)).collect(Collectors.toList());
        formattedLines.stream().peek(l -> {
            if (!distinctLines.contains(l)) {
                distinctLines.add(l);
            }
        }).collect(Collectors.toList());

        //master change2
        //distinctLines.forEach(System.out::println);

        try {
            OracleConnection connectionDkp1 = (OracleConnection) getConnection(connectionDataDkp1);
            OracleConnection connectionKztusg1 = (OracleConnection) getConnection(connectionDataKztusg1);

            distinctLines.stream().map(s -> {
                String subscriberId = getSubscriberId(connectionDkp1, s);
                String townId = getTownId(connectionKztusg1, subscriberId);
                System.out.println("UPDATE DB.RPR9_USAGE_INTERFACE SET TOWN_ID = '" + townId + "' WHERE ID = " + s + ";");
                return s;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Exception in stream: " + e.getMessage());
        }
    }

    private static String getSubscriberId(OracleConnection connection, String errorId) {
        try {
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

    private static String getTownId(OracleConnection connection, String subscriberId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select distinct rpr.town_id " +
                            "from rpr9_usage_interface rpr " +
                            "where rpr.subscriber_id in(?) " +
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
        String result = null;
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getString(resultColumn);
                count++;
            }

            if (count > 1) {
                System.out.println("SubscriberId where Town_id > 1" + subId);
            }
            return result;
        } catch (Exception e) {
            System.out.println("Exception in query: " + e.getMessage());
            return null;
        }
    }
}
