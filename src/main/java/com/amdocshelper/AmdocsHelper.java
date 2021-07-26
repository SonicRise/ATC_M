package com.amdocshelper;

import com.amdocshelper.connections.ConnectionData;
import com.amdocshelper.connections.Connections;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AmdocsHelper {
    private static final Connections connections = new Connections();

    public static void main(String[] args) {
        String errorId = "10415203737";
        String subscriberId = getSubscriberId(errorId);
        System.out.println("SubscriberId: " + subscriberId);
        String townId = getTownId(subscriberId);
        System.out.println("TownId: " + townId);
        System.out.println("Result string: ");
        System.out.println("UPDATE DB.RPR9_USAGE_INTERFACE SET TOWN_ID = " + townId + " WHERE ID = " + errorId + ";");
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
            return sqlExecutor(preparedStatement, "subscriber_id");
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
                    "select * " +
                            "from rpr9_usage_interface rpr " +
                            "where 1 = 1 " +
                            "and rpr.subscriber_id in(?) " +
                            "and rpr.cycle_code in (07) " +
                            "and rpr.cycle_month in ('7')" +
                            "and rpr.record_type in ('V')" +
                            "and rpr.rerate_value = '0'");
            preparedStatement.setString(1, subscriberId);
            return sqlExecutor(preparedStatement, "town_id");
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

    private static String sqlExecutor(PreparedStatement preparedStatement, String resultColumn) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(resultColumn);
        } catch (Exception e) {
            System.out.println("Exception in query: " + e.getMessage());
            return null;
        }
    }
}
