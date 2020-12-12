package com.anson.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseBody {
  // use this annotation to convert a Java object to an Json format object
  @JsonProperty("user_id")
  private final String userId;

  @JsonProperty("name")
  private final String name;

  public LoginResponseBody(String userId, String name) {
    this.userId = userId;
    this.name = name;
  }

  public String getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

}
