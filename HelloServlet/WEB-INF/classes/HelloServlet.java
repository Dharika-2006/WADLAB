import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String name = request.getParameter("username");
        out.println("<html><body>");
        out.println("<h2>Hello " + name + "!</h2>");
        out.println("</body></html>");
    }
}
