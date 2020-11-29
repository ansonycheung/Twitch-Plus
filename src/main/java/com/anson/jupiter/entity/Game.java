package com.anson.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
  @JsonProperty("name")
  public String name;

  @JsonProperty("developer")
  public String developer;

  @JsonProperty("release_time")
  public String releaseTime;

  @JsonProperty("website")
  public String website;

  @JsonProperty("price")
  public double price;

  public Game(String name, String developer, String releaseTime, String website, double price) {
    this.name = name;
    this.developer = developer;
    this.releaseTime = releaseTime;
    this.website = website;
    this.price = price;
  }
//  private final String name;
//  private final String developer;
//  private final String releaseDate;
//  private final float price;
//
//  public Game(String name, String developer, String releaseDate, float price) {
//    this.name = name;
//    this.developer = developer;
//    this.releaseDate = releaseDate;
//    this.price = price;
//  }
//
//  private Game(GameBuilder builder) {
//    this.name = builder.name;
//    this.developer = builder.developer;
//    this.releaseDate = builder.releaseDate;
//    this.price = builder.price;
//  }
//
//  public String getName() {
//    return name;
//  }
//
//  public String getDeveloper() {
//    return developer;
//  }
//
//  public String getReleaseDate() {
//    return releaseDate;
//  }
//
//  public float getPrice() {
//    return price;
//  }
//
//  public static class GameBuilder {
//    private String name;
//    private String developer;
//    private String releaseDate;
//    private float price;
//
//    public GameBuilder() {
//    }
//
//    public void setName(String name) {
//      this.name = name;
//    }
//
//    public void setDeveloper(String developer) {
//      this.developer = developer;
//    }
//
//    public void setReleaseDate(String releaseDate) {
//      this.releaseDate = releaseDate;
//    }
//
//    public void setPrice(float price) {
//      this.price = price;
//    }
//
//    public Game build() {
//      return new Game(this);
//    }
//  }
}

// Game game = new Game("Vincent", "anson.com", "2020-11-28", 10);

// import Game.GameBuilder
//
// GameBuilder builder = new GameBuilder();
// builder.setName("Vincent");
// builder.setDeveloper("anson.com");
// Game game = builder.build();
