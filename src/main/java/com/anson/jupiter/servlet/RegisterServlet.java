package com.anson.jupiter.servlet;

import com.anson.jupiter.db.MySQLConnection;
import com.anson.jupiter.db.MySQLException;
import com.anson.jupiter.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    User user = mapper.readValue(request.getReader(), User.class);
    if (user == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    boolean isUserAdded = false;
    MySQLConnection connection = null;
    try {
      connection = new MySQLConnection();
      // Do not forget the step of encrypting password
      user.setPassword(ServletUtil.encryptPassword(user.getUserId(), user.getPassword()));
      isUserAdded = connection.addUser(user);
    } catch (MySQLException e) {
      throw new ServletException(e);
    } finally {
      connection.close();
    }

    if (!isUserAdded) {
      response.setStatus(HttpServletResponse.SC_CONFLICT);
    }
  }

}
