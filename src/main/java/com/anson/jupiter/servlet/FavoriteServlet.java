package com.anson.jupiter.servlet;

import com.anson.jupiter.db.MySQLConnection;
import com.anson.jupiter.db.MySQLException;
import com.anson.jupiter.entity.FavoriteRequestBody;
import com.anson.jupiter.entity.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorite"})
public class FavoriteServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    String userId = request.getParameter("user_id");
    ObjectMapper mapper = new ObjectMapper();
    FavoriteRequestBody body = mapper.readValue(request.getReader(), FavoriteRequestBody.class);

    if (body == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    MySQLConnection connection = null;
    try {
      connection = new MySQLConnection();
      connection.setFavoriteItem(userId, body.getFavoriteItem());
    } catch (MySQLException e) {
      throw new ServletException(e);
    } finally {
      if (connection != null) {
        connection.close();
      }
    }

  }

  protected void doDelete(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    String userId = request.getParameter("user_id");
    ObjectMapper mapper = new ObjectMapper();
    FavoriteRequestBody body = mapper.readValue(request.getReader(), FavoriteRequestBody.class);

    if (body == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    MySQLConnection connection = null;
    try {
      connection = new MySQLConnection();
      connection.unsetFavoriteItem(userId, body.getFavoriteItem().getId());
    } catch (MySQLException e) {
      throw new ServletException(e);
    } finally {
      if (connection != null) {
        connection.close();
      }
    }

  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    String userId = request.getParameter("user_id");
    Map<String, List<Item>> itemMap;

    MySQLConnection connection = null;
    try {
      connection = new MySQLConnection();
      itemMap = connection.getFavoriteItems(userId);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().print(new ObjectMapper().writeValueAsString(itemMap));
    } catch (MySQLException e) {
      throw new ServletException(e);
    } finally {
      if (connection != null) {
        connection.close();
      }
    }

  }
}
