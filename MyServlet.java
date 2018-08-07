import com.sun.deploy.util.SessionState;
import test.Dao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.PasswordAuthentication;
import java.sql.DriverManager;
import java.sql.*;
import java.util.Scanner;
import java.util.jar.Attributes;

import static java.lang.System.out;
import java.util.UUID;

/**
 * Created by nWX540006 on 2/7/2018.
 */

@WebServlet(name = "MyServlet")



public class MyServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("uname");
        String password = request.getParameter("psw");


        HttpSession session2 = request.getSession(true);
        String sessionId = (String) session2.getAttribute("sessionId");


        response.setContentType("text/html;charset=UTF-8");
        PrintWriter outt = response.getWriter();



        out.println("<html>");
        out.println("<body>");



        if (sessionId != null) {
            outt.print("<p>you are logged in </p>");
            outt.print("<button>Log out</button>");
        } else {
            boolean userExists = Dao.Check(userName, password);
            if (userExists) {
                UUID newSessionId = UUID.randomUUID();
                session2.setAttribute("sessionId", newSessionId.toString());
                session2.setAttribute("username",userName );

                request.getRequestDispatcher("SecondPage.jsp").forward(request, response);
                //response.sendRedirect("SecondPage.jsp");

            } else {
                outt.print("<p>wrong user/pass</p>");
            }
        }



        out.println("</body>");
        out.println("</html>");


        ////////////////////////////////////

        String command = request.getParameter("command");

        if (command != null && command.equals("logout")){
            request.getSession().invalidate();
        }
    }

}