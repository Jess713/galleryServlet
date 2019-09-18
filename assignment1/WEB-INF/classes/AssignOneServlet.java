
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.io.*;

/**
 * Title : Assignment 01 Student Id s: A01045229 Name : Jessica Kim Date :
 * 2019-09-09
 **/
public class AssignOneServlet extends HttpServlet {

  public boolean start = false;

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    HttpSession session = request.getSession(true);

    if (request.getParameter("userId") != null && request.getParameter("psw") != null) {
      String userIdFromForm = request.getParameter("userId");
      String pswFromForm = request.getParameter("psw");

      session.setAttribute("userId", userIdFromForm);
      session.setAttribute("psw", pswFromForm);

    }

    String userId = (String) session.getAttribute("userId");
    System.out.println("Current user: " + userId);
    String psw = (String) session.getAttribute("psw");

    String headerName = request.getHeader("X-Requested-With");
    ArrayList<String> fileNameList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> captionList = new ArrayList<>();
    // String caption = ;

    String errMsg = "Testing";

    System.out.println("Login attempt with userId : " + userId);
    boolean isMatchFromDB = false;

    // Set response content type.
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    try {
      try {
        Class.forName("oracle.jdbc.OracleDriver");
      } catch (Exception ex) {
      }
      Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");

      Statement stmt = con.createStatement();

      Statement photoStmt = con.createStatement();

      String query = "SELECT userid, password FROM users";

      String photoTableQuery = "SELECT userid, filename, caption, datedata From photos where userid = '" + userId + "'";
      ResultSet rs = stmt.executeQuery(query);
      ResultSet photoRs = photoStmt.executeQuery(photoTableQuery);

      String getUserIdFromUser = null;
      // String getUserIdFromPhoto = null;

      String getPsw;

      System.out.println("img array2: " + Arrays.toString(fileNameList.toArray()));
      while (rs.next()) {
        getPsw = rs.getString("password");
        getUserIdFromUser = rs.getString("userid");
        if ((getUserIdFromUser.equals(userId) && getPsw.equals(psw))) {
          isMatchFromDB = true;
          System.out.println("Matching id and psw found");
          break;
        }
      }

      fileNameList.clear();
      dateList.clear();
      captionList.clear();

      while (photoRs.next()) {
        fileNameList.add(photoRs.getString("filename"));
        dateList.add(photoRs.getString("datedata"));
        captionList.add(photoRs.getString("filename"));
      }

      String stringImgArray = Arrays.toString(fileNameList.toArray());
      System.out.println("Done fetching, returning these photos from user: " + userId + " " + stringImgArray);

      String dateArray = Arrays.toString(dateList.toArray());
      String captionArray = Arrays.toString(captionList.toArray());
      session.setAttribute("imgList", stringImgArray);
      session.setAttribute("dateList", dateArray);
      session.setAttribute("captionList", captionArray);

      stmt.close();
      photoStmt.close();
      con.close();

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    if (isMatchFromDB) {
      System.out.println("userid: " + userId + " password: " + psw + " login successful");
      RequestDispatcher view = request.getRequestDispatcher("./gallery.jsp");
      view.forward(request, response);

    } else {
      out.println("<html>");
      out.println("<meta charset='UTF-8'>");
      out.println(
          "<script type='text/JavaScript' src='//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.js'></script>");
      out.println("<script type='text/javascript' src='./script.js'></script>");
      out.println("<body>");
      out.println("<h1>LOG IN FAILED.......<br>USER NAME OR PASSWORD DOESN'T EXIST :(</h1>");
      out.println("<button type='button' id='mainPage'>Back</button>");
      // out.println("</form>");
      out.println("</body></html>");
      out.close();
    }

  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(true);
    String userId = (String) session.getAttribute("userId");
    System.out.println("request value: " + request.getParameter("value"));
    if (request.getParameter("value") != null && request.getParameter("value").equals("invalidate")) {
      session.invalidate();
      // response.sendRedirect("login.html");
      return;
    } else {

      try {
        try {
          Class.forName("oracle.jdbc.OracleDriver");
        } catch (Exception ex) {
        }
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
        String signUpQuery = "INSERT INTO users (userid, password) VALUES ('"
            + request.getParameter("sign-up-user-name") + "','" + request.getParameter("sign-up-psw") + "')";
        Statement stmt = con.createStatement();

        // for now it doesn't check if there's duplicate in DB
        stmt.executeUpdate(signUpQuery);

        stmt.close();
        con.close();

      } catch (SQLException ex) {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<html>");
        out.println("<meta charset='UTF-8'>");
        out.println(
            "<script type='text/JavaScript' src='//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.js'></script>");
        out.println("<script type='text/javascript' src='./script.js'></script>");
        out.println("<body>");
        out.println("<h1>SIGN UP FAILED.......<br>USER NAME ALREADY EXISTS :(</h1>");

        out.println("<button type='button' id='mainPage'>Back</button>");
        out.println("</body></html>");
        out.close();
        ex.printStackTrace();
      }
      PrintWriter out = response.getWriter();
      response.setContentType("text/html");

      String title = "Sign up successful";

      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n");
      out.println(
          "<script type='text/JavaScript' src='//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.js'></script>");
      out.println("<script type='text/javascript' src='./script.js'></script>");
      out.println("<body bgcolor=\"#f0f0f0\">\n" + "<h1 align=\"center\">" + title + "</h1>\n" + "<ul>\n"
          + " <li><b>user id</b>: " + request.getParameter("sign-up-user-name") + "</body></html>");
      out.println("<button class='button' type='button' id='mainPage'>Login</button>");
    }
  }

}
