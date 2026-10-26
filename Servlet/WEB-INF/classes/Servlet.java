import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Servlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String name = req.getParameter("name");
        int m1 = Integer.parseInt(req.getParameter("m1"));
        int m2 = Integer.parseInt(req.getParameter("m2"));
        int m3 = Integer.parseInt(req.getParameter("m3"));

        int total = m1 + m2 + m3;
        double avg = total / 3.0;

        String grade;

        if (avg >= 90) {
            grade = "A";
        } else if (avg >= 75) {
            grade = "B";
        } else if (avg >= 60) {
            grade = "C";
        } else {
            grade = "F";
        }

        out.println("<html><body>");
        out.println("<h2>Student Grade</h2>");
        out.println("Name: " + name + "<br>");
        out.println("Grade :" + grade + "<br>");
        out.println("Total: " + total + "<br>");
        out.println("</body></html>");
    }
}