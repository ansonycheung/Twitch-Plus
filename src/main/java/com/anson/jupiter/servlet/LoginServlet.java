package com.anson.jupiter.servlet;

import com.anson.jupiter.db.MySQLConnection;
import com.anson.jupiter.db.MySQLException;
import com.anson.jupiter.entity.LoginRequestBody;
import com.anson.jupiter.entity.LoginResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
//    ObjectMapper mapper = new ObjectMapper();
//    LoginRequestBody body = mapper.readValue(request.getReader(), LoginRequestBody.class);
    LoginRequestBody body = ServletUtil.readRequestBody(LoginRequestBody.class, request);
    if (body == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String username;
    MySQLConnection connection = null;

    try {
      connection = new MySQLConnection();
      String userId = body.getUserId();
      String password = ServletUtil.encryptPassword(body.getUserId(), body.getPassword());
      username = connection.verifyLogin(userId, password);
    } catch (MySQLException e) {
      throw new ServletException(e);
    } finally {
      connection.close();
    }

    if (!username.isEmpty()) {
      // create a new session (it default has a session Id)
      HttpSession session = request.getSession();

      session.setAttribute("user_id", body.getUserId());
      session.setMaxInactiveInterval(600);  // expiration time, 600 seconds = 10 minutes

      // Tomcat will directly send cookies in the HTTP response (internal management of sessionId)
      LoginResponseBody loginResponseBody = new LoginResponseBody(body.getUserId(), username);
      response.setContentType("application/json;charset=UTF-8");
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getWriter(), loginResponseBody);
//      response.getWriter().print(new ObjectMapper().writeValueAsString(loginResponseBody));
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

}
