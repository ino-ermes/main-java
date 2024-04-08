package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/kino";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static String executeQuery(String sqlQuery) {
        StringBuilder resultBuilder = new StringBuilder();

        try (
            Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
        ) {
            int columnCount = rs.getMetaData().getColumnCount();
            
            for (int i = 1; i <= columnCount; i++) {
                resultBuilder.append(rs.getMetaData().getColumnName(i)).append("\t");
            }
            resultBuilder.append("\n");
            
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    resultBuilder.append(rs.getString(i)).append("\t");
                }
                resultBuilder.append("\n");
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        }

        return resultBuilder.toString();
    }
}
