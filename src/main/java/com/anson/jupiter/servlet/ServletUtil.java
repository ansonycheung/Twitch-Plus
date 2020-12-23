package com.anson.jupiter.servlet;

import com.anson.jupiter.entity.Item;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;

public class ServletUtil {
  public static void writeItemMap(HttpServletResponse response, Map<String, List<Item>> itemMap)
      throws IOException, IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().print(new ObjectMapper().writeValueAsString(itemMap));
  }

  public static String encryptPassword(String userId, String password) throws IOException {
    return DigestUtils.md5Hex(userId + DigestUtils.md5Hex(password)).toLowerCase();
  }

  public static <T> T readRequestBody(Class<T> cls, HttpServletRequest request) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(request.getReader(), cls);
    } catch (JsonParseException | JsonMappingException e) {
      return null;
    }
  }

}
