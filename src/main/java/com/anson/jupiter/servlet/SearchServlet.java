package com.anson.jupiter.servlet;

import com.anson.jupiter.entity.*;
import com.anson.jupiter.external.TwitchClient;
import com.anson.jupiter.external.TwitchException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String gameId = request.getParameter("game_id");
    if (gameId == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    TwitchClient client = new TwitchClient();

    try {
//      // format to JSON, and support different languages (char set)
//      // use "ServletUtil.writeItemMap(response, itemMap);" instead of next three lines
//      response.setContentType("application/json;charset=UTF-8");
//      Map<String, List<Item>> items = client.searchItems(gameId);
//      // different direction mapping at line 193 TwitchClient.java
//      response.getWriter().print(new ObjectMapper().writeValueAsString(items));
      ServletUtil.writeItemMap(response, client.searchItems(gameId));
    } catch (TwitchException e) {
      throw new ServletException(e);
    }


  }
}
