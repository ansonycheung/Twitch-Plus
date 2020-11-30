package com.anson.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = Game.Builder.class)
public class Game {

  @JsonProperty("id")
  private final String id;

  @JsonProperty("name")
  private final String name;

  @JsonProperty("box_art_url")
  private final String boxArtUrl;

  private Game(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.boxArtUrl = builder.boxArtUrl;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getBoxArtUrl() {
    return boxArtUrl;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Builder {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("box_art_url")
    private String boxArtUrl;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder boxArtUrl(String boxArtUrl) {
      this.boxArtUrl = boxArtUrl;
      return this;
    }

    public Game build() {
      return new Game(this);
    }
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
