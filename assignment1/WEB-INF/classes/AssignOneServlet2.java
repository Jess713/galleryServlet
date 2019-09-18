import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.oreilly.servlet.MultipartRequest;
import java.nio.file.*;

public class AssignOneServlet2 extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    String requestValue = null;
    String filename = null;
    String userId = (String) session.getAttribute("userId");

    String errMsg = "Testing";
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    if (request.getParameter("value") != null) {

      requestValue = request.getParameter("value");
      filename = request.getParameter("filename");
      System.out.println("requestValue: " + requestValue + " filename: " + filename);
      String deleteQuery = "delete from photos where userid = '" + userId + "' and caption = '" + filename + "'";
      System.out.println("deleteQuery: " + deleteQuery);
      try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
          Statement stmt = conn.createStatement();) {
        stmt.executeUpdate(deleteQuery);
        System.out.println("Record deleted successfully");
      } catch (SQLException e) {
        e.printStackTrace();
      }

    } else if (requestValue == null) {

      MultipartRequest m = new MultipartRequest(request, "c:/tomcat/webapps/upload/images");
      System.out.println("Successfully uploaded img to upload/images folder");

      try {
        try {
          Class.forName("oracle.jdbc.OracleDriver");
        } catch (Exception ex) {
        }
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
        errMsg += "Con";
        Statement stmt = con.createStatement();
        errMsg += "stmt";
        PreparedStatement ps = con.prepareStatement("insert into photos (userid, filename, caption) VALUES(?,?,?)");

        // request.setAttribute("userId", userId);

        // File imgFile = request.getParameter("myFile"); //not sure if TJ want this
        // yet.

        // Part filePart = request.getPart("file");
        // String fileName =
        // Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileName = m.getFilesystemName("file");
        // String caption = "place holder for caption";
        System.out.println(userId + " " + fileName + " " + fileName);
        // InputStream inputStream = filePart.getInputStream();
        ps.setString(1, userId);
        ps.setString(2, fileName);
        ps.setString(3, fileName);

        int i = ps.executeUpdate();
        System.out.println(i + " records affected");
        // ps.setBinaryStream(4, inputStream, inputStream.available());

        out.println("<html>");
        out.println("<meta charset='UTF-8'>");
        out.println("<form action='assign01' method ='GET' ");
        out.println("<body>");
        out.println("<h1>file uploaded. </h1>");
        // out.println("<input name='userid' value='" + userId + "'>");// Look into
        // passing values to
        // // client using jsp file

        out.println("<button>Go To Gallery</button></form>");
        // out.println("</form>");
        out.println("</body></html>");
        out.close();
        stmt.close();
        con.close();
        errMsg += "End";
      } catch (SQLException ex) {

        ex.printStackTrace();
        // errMsg += "";
      }
    }

    // System.out.println(errMsg);
  }
}

// }
