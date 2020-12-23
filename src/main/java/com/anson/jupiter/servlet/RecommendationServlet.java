package com.anson.jupiter.servlet;

import com.anson.jupiter.entity.Item;
import com.anson.jupiter.recommendation.ItemRecommender;
import com.anson.jupiter.recommendation.RecommendationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "RecommendationServlet", urlPatterns = {"/recommendation"})
public class RecommendationServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);  // no create session if not login
    ItemRecommender itemRecommender = new ItemRecommender();
    Map<String, List<Item>> itemMap;
    try {
      if (session == null) {  // not login
        itemMap = itemRecommender.recommendItemsByDefault();
      } else {  // login
        String userId = (String) request.getSession().getAttribute("user_id");
        itemMap = itemRecommender.recommendItemsByUser(userId);
      }
    } catch (RecommendationException e) {
      throw new ServletException(e);
    }

    ServletUtil.writeItemMap(response, itemMap);

  }
}
