import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DBServlet extends HttpServlet {
    private static final String url = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String password = "sqlroot123";

    private Connection connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, USER, password);
    }

    // read
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html><body><h2>Employee record</h2>");
        out.println("<table border='1'><tr><th>ID</th><th>Age</th><th>First</th><th>Last</th></tr>");

        try (Connection con = connect();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Employees")) {
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getInt("age") + "</td><td>"
                        + rs.getString("first") + "</td><td>" + rs.getString("last") + "</td></tr>");

            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
        out.println("</table><br><a href='index.html'>Back</a></body></html>");
    }

    // create, update, delete
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String action = req.getParameter("action");
        int id = Integer.parseInt(req.getParameter("id"));
        try (Connection con = connect()) {
            PreparedStatement ps;
            if ("add".equals(action)) {
                ps = con.prepareStatement("INSERT INTO Employees VALUES (?,?,?,?)");
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(req.getParameter("age")));
                ps.setString(3, req.getParameter("first"));
                ps.setString(4, req.getParameter("last"));
            } else if ("update".equals(action)) {
                ps = con.prepareStatement("UPDATE Employees SET age=?, first=?, last=? WHERE id=?");
                ps.setInt(1, Integer.parseInt(req.getParameter("age")));
                ps.setString(2, req.getParameter("first"));
                ps.setString(3, req.getParameter("last"));
                ps.setInt(4, id);
            } else {
                ps = con.prepareStatement("DELETE FROM Employees where id=?");
                ps.setInt(1, id);
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.sendRedirect("DBServlet");
    }
}
