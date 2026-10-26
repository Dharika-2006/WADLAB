import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DBaseServlet extends HttpServlet {

    static final String URL = "jdbc:mysql://localhost:3306/school";
    static final String USER = "root";
    static final String PASS = "sqlroot123"; // change to your MySQL password

    Connection conn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }

    String calcGrade(double avg) {
        if (avg >= 90)
            return "A";
        else if (avg >= 75)
            return "B";
        else if (avg >= 60)
            return "C";
        else
            return "F";
    }

    // ---------- CREATE : POST /Servlet (form data) ----------
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try (Connection c = conn()) {
            String name = req.getParameter("name");
            int m1 = Integer.parseInt(req.getParameter("m1"));
            int m2 = Integer.parseInt(req.getParameter("m2"));
            int m3 = Integer.parseInt(req.getParameter("m3"));

            int total = m1 + m2 + m3;
            double avg = total / 3.0;
            String grade = calcGrade(avg);

            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO students(name,m1,m2,m3,total,average,grade) VALUES(?,?,?,?,?,?,?)");
            ps.setString(1, name);
            ps.setInt(2, m1);
            ps.setInt(3, m2);
            ps.setInt(4, m3);
            ps.setInt(5, total);
            ps.setDouble(6, avg);
            ps.setString(7, grade);
            ps.executeUpdate();

            out.println("<html><body>");
            out.println("<h2>Student Saved</h2>");
            out.println("Name: " + name + "<br>");
            out.println("Grade: " + grade + "<br>");
            out.println("Total: " + total + "<br>");
            out.println("</body></html>");
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }

    // ---------- READ : GET /Servlet or GET /Servlet?id=1 ----------
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try (Connection c = conn()) {
            String id = req.getParameter("id");
            PreparedStatement ps;
            if (id == null) {
                ps = c.prepareStatement("SELECT * FROM students");
            } else {
                ps = c.prepareStatement("SELECT * FROM students WHERE id=?");
                ps.setInt(1, Integer.parseInt(id));
            }
            ResultSet rs = ps.executeQuery();

            out.println("<html><body><h2>Students</h2>");
            out.println(
                    "<table border='1'><tr><th>ID</th><th>Name</th><th>Total</th><th>Average</th><th>Grade</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name")
                        + "</td><td>" + rs.getInt("total") + "</td><td>" + rs.getDouble("average")
                        + "</td><td>" + rs.getString("grade") + "</td></tr>");
            }
            out.println("</table></body></html>");
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }

    // ---------- UPDATE : PUT /Servlet?id=1&name=..&m1=..&m2=..&m3=.. ----------
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try (Connection c = conn()) {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            int m1 = Integer.parseInt(req.getParameter("m1"));
            int m2 = Integer.parseInt(req.getParameter("m2"));
            int m3 = Integer.parseInt(req.getParameter("m3"));

            int total = m1 + m2 + m3;
            double avg = total / 3.0;
            String grade = calcGrade(avg);

            PreparedStatement ps = c.prepareStatement(
                    "UPDATE students SET name=?, m1=?, m2=?, m3=?, total=?, average=?, grade=? WHERE id=?");
            ps.setString(1, name);
            ps.setInt(2, m1);
            ps.setInt(3, m2);
            ps.setInt(4, m3);
            ps.setInt(5, total);
            ps.setDouble(6, avg);
            ps.setString(7, grade);
            ps.setInt(8, id);

            int rows = ps.executeUpdate();
            out.println(rows > 0 ? "Updated. New grade: " + grade : "Student not found");
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }

    // ---------- DELETE : DELETE /Servlet?id=1 ----------
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try (Connection c = conn()) {
            int id = Integer.parseInt(req.getParameter("id"));

            PreparedStatement ps = c.prepareStatement("DELETE FROM students WHERE id=?");
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            out.println(rows > 0 ? "Deleted" : "Student not found");
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}