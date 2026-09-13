import java.sql.*;
import java.util.Scanner;

public class GrievanceRedressalSystem {

    // Database Connection
    public static Connection getConnection() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/grievance_system",
                    "root",
                    ""
            );

            System.out.println("Database connected successfully!");

        } catch (Exception e) {
            System.out.println(
                    "Database connection failed: " + e.getMessage()
            );
        }

        return con;
    }

    // Validate User Login
    public static boolean validateUser(
            String username,
            String password,
            String role) {

        boolean status = false;

        try {
            Connection con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users " +
                    "WHERE username=? AND password=? AND role=?"
            );

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            status = rs.next();

            con.close();

        } catch (Exception e) {
            System.out.println(
                    "Error during login: " + e.getMessage()
            );
        }

        return status;
    }

    // Submit Grievance
    public static void submitGrievance(
            String studentId,
            String category,
            String description) {

        try {
            Connection con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO grievances " +
                    "(student_id, category, description, status) " +
                    "VALUES (?, ?, ?, 'Pending')"
            );

            ps.setString(1, studentId);
            ps.setString(2, category);
            ps.setString(3, description);

            ps.executeUpdate();

            System.out.println(
                    "Grievance submitted successfully!"
            );

            con.close();

        } catch (Exception e) {
            System.out.println(
                    "Error while submitting grievance: "
                    + e.getMessage()
            );
        }
    }

    // Update Grievance Status
    public static void updateStatus(
            int grievanceId,
            String status,
            String remarks) {

        try {
            Connection con = getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE grievances " +
                    "SET status=?, remarks=? " +
                    "WHERE grievance_id=?"
            );

            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setInt(3, grievanceId);

            ps.executeUpdate();

            System.out.println(
                    "Grievance status updated successfully!"
            );

            con.close();

        } catch (Exception e) {
            System.out.println(
                    "Error while updating status: "
                    + e.getMessage()
            );
        }
    }

    // Generate Grievance Report
    public static void generateReport() {

        try {
            Connection con = getConnection();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT category, COUNT(*) AS total " +
                    "FROM grievances " +
                    "GROUP BY category"
            );

            System.out.println(
                    "\n---- Grievance Summary Report ----"
            );

            while (rs.next()) {

                System.out.println(
                        rs.getString("category")
                        + " : "
                        + rs.getInt("total")
                        + " grievances"
                );
            }

            con.close();

        } catch (Exception e) {
            System.out.println(
                    "Error generating report: "
                    + e.getMessage()
            );
        }
    }

    // Main Method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println(
                "Welcome to the Grievance Redressal System"
        );

        // Login Details
        System.out.print("Enter username: ");
        String user = sc.nextLine();

        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        System.out.print(
                "Enter role (student/hod/principal): "
        );
        String role = sc.nextLine();

        // Validate Login
        if (validateUser(user, pass, role)) {

            System.out.println(
                    "Login Successful! Welcome, " + user
            );

            // Student Operations
            if (role.equalsIgnoreCase("student")) {

                System.out.print("\nEnter Student ID: ");
                String id = sc.nextLine();

                System.out.print("Enter Category: ");
                String cat = sc.nextLine();

                System.out.print("Enter Description: ");
                String desc = sc.nextLine();

                submitGrievance(id, cat, desc);
            }

            // HOD / Principal Operations
            else if (
                    role.equalsIgnoreCase("hod")
                    || role.equalsIgnoreCase("principal")
            ) {

                System.out.print(
                        "\nEnter Grievance ID to update: "
                );

                int gid = sc.nextInt();
                sc.nextLine();

                System.out.print(
                        "Enter New Status (Resolved/In Progress): "
                );

                String status = sc.nextLine();

                System.out.print("Enter Remarks: ");
                String remarks = sc.nextLine();

                updateStatus(gid, status, remarks);

                generateReport();
            }

        } else {

            System.out.println(
                    "Invalid credentials! Please try again."
            );
        }

        sc.close();

        System.out.println(
                "\nThank you for using the Grievance Redressal System!"
        );
    }
}