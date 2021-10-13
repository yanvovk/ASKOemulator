import java.sql.*;

public class Emulator {

    public static Connection getOracleConnection(String hostName, String sid, String userName, String password)
            throws  SQLException {
        String connectionURL = "jdbc:mercury:oracle:@" + hostName + ":1521/" + sid;
        Connection connection = DriverManager.getConnection(connectionURL, userName, password);

        return connection;
    }

    public static void main(String[] args) throws InterruptedException, SQLException {
        String hostName = "192.168.14.53";
        String sid = "orcl";
        String userName = "C##X5";
        String password = "c##x5";

        String insertingIntoTask = "INSERT INTO task (id, ticket_id, header, text, client_id, create_date, external_system, sync_mask, contractor_id, state_id, solution_group_id) " +
                "(SELECT id, id as ticket_id, header, text, applicant_id, create_date, external_system, 0, 1, 1 as state_id, -1 FROM ticket WHERE state_id = -1 AND id = 224790)";
        String updatingStateId = "UPDATE ticket SET state_id = 1 WHERE id IN (SELECT id FROM task WHERE state_id = 1) AND state_id = -1 AND id = 224790";

        while (true) {
            try(Connection dbConnection = getOracleConnection(hostName, sid, userName, password)) {
                Statement stmt = dbConnection.createStatement();
                System.out.println("Created" + stmt.executeUpdate(insertingIntoTask) + "tasks");
                System.out.println("Updated state_id in" + stmt.executeUpdate(updatingStateId) + "tickets");
                dbConnection.commit();
            } catch (SQLException e) {
                System.out.println(e);
            }
            Thread.sleep(1200000);
        }
    }
}
