package com.anson.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestBody {
  private final String userId;
  private final String password;

  // use this constructor to convert an Json format object to a Java object
  @JsonCreator
  public LoginRequestBody(@JsonProperty("user_id") String userId, @JsonProperty("password") String password) {
    this.userId = userId;
    this.password = password;
  }

  public String getUserId() {
    return userId;
  }

  public String getPassword() {
    return password;
  }
}
