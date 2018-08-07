import model.Email;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by nWX540006 on 4/8/2018.
 */
@WebServlet(name = "SecondServlet")
public class SecondServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/test";
        String driver = "com.mysql.jdbc.Driver";

        try {

            String receiver = request.getParameter("receiver");
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            HttpSession session = request.getSession();
            String sender = (String) session.getAttribute("username");

            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, "root", "1386");
            PreparedStatement pst = conn.prepareStatement("insert into `test`.`emailbox`(Sender,Receiver,Title,Content) values(?,?,?,?);");

            pst.setString(1, sender);
            pst.setString(2, receiver);
            pst.setString(3, title);
            pst.setString(4, content);


            int i = pst.executeUpdate();
            String msg = " ";
            if (i != 0) {
                msg = "Email has sent!";
                pw.println("<font size='6' color=blue>" + msg + "</font>");


            } else {
                msg = "failed to send the Email";
                pw.println("<font size='6' color=blue>" + msg + "</font>");
            }
            pst.close();
        } catch (Exception e) {
            pw.println(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/test";
        String driver = "com.mysql.jdbc.Driver";

        try {

            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, "root", "1386");
            HttpSession session = request.getSession();
            String sender = (String) session.getAttribute("username");


            PreparedStatement pst = conn.prepareStatement("SELECT Sender,Title,Content,EmailId FROM `test`.`emailbox` WHERE emailbox.Receiver = ? ");
            pst.setString(1, sender);
            pst.execute();


            ResultSet rs = pst.getResultSet();


            ArrayList<Email> table =new ArrayList<Email>();

            while (rs.next()) {

                Email a = new Email();
                a.setSender(rs.getString("Sender"));
                a.setTitle(rs.getString("Title"));
                a.setContent(rs.getString("Content"));
                a.setEmailId(rs.getInt("EmailId"));

                table.add(a);

            }
            request.setAttribute("table",table);
            request.getRequestDispatcher("/InboxPage.jsp").forward(request, response);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
