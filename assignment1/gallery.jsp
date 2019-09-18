<%@ page import="java.io.*,javax.servlet.ServletException,javax.servlet.http.*" %>
<html>
<meta charset='UTF-8'>

<%
String userId = (String) session.getAttribute("userId");
String imgList = (String) session.getAttribute("imgList"); 
String dateList = (String) session.getAttribute("dateList"); 
String captionList = (String) session.getAttribute("captionList"); 
System.out.println( imgList + " from jsp");
%>

<body data-imgList="<%=imgList%>" data-dateList="<%=dateList%>" data-captionList="<%=captionList%>">

    <h2> You are logged in as: <%=userId%></h2>
    <img id='changeImg' src='./images/default.jpg' alt='default-image' height='150' width='150'>
    <br>
    <div class='detail'>
        <h3>caption: <span id='caption'>N/A</span></h3>
        <h3>date: <span id='date'>N/A</span></h3>
    </div>
    <br>
    <div class='button'>
        <button class='button' type='button' id='prev'>Prev</button>
        <button class='button' type='button' id='auto'>Auto</button>
        <button class='button' type='button' id='next'>Next</button>
        <input type="hidden" name="myField" id="myField" value="" />
        <br>
        <form action="">
            <button class="button" type='button' id='delete'>DELETE</button>
        </form>
    </div><br>
    <form action='assign01-2' method='POST' enctype='multipart/form-data'>
        Select a file: <input type='file' name='file' required><br><br>
        <button id='upload'>UPLOAD FILE</button></form><br>
    <h2>Current files belong to this user..</h2>
    <p id='photos'> <%=imgList%> </p>
    Please use this button to logout!<button class='button' type='button' id='logout'>LOGOUT</button>


</body>
<script type="text/JavaScript" src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.js"></script>
<script type='text/javascript' src='./script.js'></script>

</html>