package com.anson.jupiter.recommendation;

import com.anson.jupiter.db.MySQLConnection;
import com.anson.jupiter.db.MySQLException;
import com.anson.jupiter.entity.*;
import com.anson.jupiter.external.TwitchClient;
import com.anson.jupiter.external.TwitchException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemRecommender {
  private static final int DEFAULT_GAME_LIMIT = 3;
  private static final int DEFAULT_PER_GAME_RECOMMENDATION_LIMIT = 10;
  private static final int DEFAULT_TOTAL_RECOMMENDATION_LIMIT = 20;

  // How to recommend if a user does not have favorite items?
  // Get topGames -> recommend based on topGames
  private List<Item> recommendByTopGames(ItemType type, List<Game> topGames)
      throws RecommendationException {
    List<Item> recommendItems = new ArrayList<>();
    TwitchClient twitchClient = new TwitchClient();

    // break the outer loop
    outerLoop:
    for (Game game : topGames) {
      List<Item> items;
      try {
        items = twitchClient.searchByType(game.getId(), type, DEFAULT_PER_GAME_RECOMMENDATION_LIMIT);
      } catch (TwitchException e) {
        throw new RecommendationException("Failed to get recommendation result.");
      }

      for (Item item : items) {
        if (recommendItems.size() == DEFAULT_TOTAL_RECOMMENDATION_LIMIT) {
          break outerLoop;  // break the outer loop (connected line 23)
        }
        recommendItems.add(item);
      }
    }
    return recommendItems;
  }

  // How to recommend if a user has some favorite items?
  // 1. Get favorite items
  // 2. Get favorite game ids from favorite items
  //    user1111 -> [aaaa, bbbb, cccc, dddd]
  //        aaaa -> {gameId: 1234, name: “...”, url: “...”, type: video}
  //        bbbb -> {gameId: 1234, name: “...”, url: “...”, type: video}
  //        cccc -> {gameId: 2345, name: “...”, url: “...”, type: video}
  //        dddd -> {gameId: 3456, name: “...”, url: “...”, type: stream}
  // 3. recommend gameId 1234 related video, then gameId 2345 related video
  private List<Item> recommendByFavoriteHistory(
      Set<String> favoriteItemIds, List<String> favoriteGameIds, ItemType type)
      throws RecommendationException {
    List<Item> recommendItems = new ArrayList<>();
    TwitchClient twitchClient = new TwitchClient();

    // favoriteGameIds: [2345, 1234, 1234] --> [{1234 : 2}, {2345 : 1}]
    // step 1. convert a list to a map, gameId as key, count of gameId as value
    // favoriteGameIds: [2345, 1234, 1234] --> [{2345 : 1}, {1234 : 2}]
    Map<String, Long> favoriteGameIdsByCount = favoriteGameIds.parallelStream().collect(
        Collectors.groupingBy(Function.identity(), Collectors.counting()));
    // step 2. sort by count:
    // favoriteGameIds: [{2345 : 1}, {1234 : 2}] -->  [{1234 : 2}, {2345 : 1}]
    List<Map.Entry<String, Long>> sortedFavoriteGameIdListByCount
        = new ArrayList<>(favoriteGameIdsByCount.entrySet());
    sortedFavoriteGameIdListByCount.sort((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()));

    // step 3. limited the size
    if (sortedFavoriteGameIdListByCount.size() > DEFAULT_GAME_LIMIT) {
      sortedFavoriteGameIdListByCount = sortedFavoriteGameIdListByCount.subList(0, DEFAULT_GAME_LIMIT);
    }

    List<Item> recommendedItems = new ArrayList<>();
    TwitchClient client = new TwitchClient();

    outerLoop:
    for (Map.Entry<String, Long> favoriteGame : sortedFavoriteGameIdListByCount) {
      List<Item> items;
      try {
        items = client.searchByType(favoriteGame.getKey(), type, DEFAULT_PER_GAME_RECOMMENDATION_LIMIT);
      } catch (TwitchException e) {
        throw new RecommendationException("Failed to get recommendation result");
      }

      for (Item item : items) {
        if (recommendedItems.size() == DEFAULT_TOTAL_RECOMMENDATION_LIMIT) {
          break outerLoop;
        }
        if (!favoriteItemIds.contains(item.getId())) {
          recommendedItems.add(item);
        }
      }
    }
    return recommendedItems;
  }

  public Map<String, List<Item>> recommendItemsByUser(String userId) throws RecommendationException {
    Map<String, List<Item>> recommendedItemMap = new HashMap<>();
    Set<String> favoriteItemIds;
    Map<String, List<String>> favoriteGameIds;  // {"video: 1234, 2345, 3456", "stream: 3456"}
    MySQLConnection connection = null;
    try {
      connection = new MySQLConnection();
      favoriteItemIds = connection.getFavoriteItemIds(userId);
      favoriteGameIds = connection.getFavoriteGameIds(favoriteItemIds);
    } catch (MySQLException e) {
      throw new RecommendationException("Failed to get user favorite history for recommendation");
    } finally {
      connection.close();
    }

    for (Map.Entry<String, List<String>> entry : favoriteGameIds.entrySet()) {
      if (entry.getValue().size() == 0) {
        TwitchClient client = new TwitchClient();
        List<Game> topGames;
        try {
          topGames = client.topGames(DEFAULT_GAME_LIMIT);
        } catch (TwitchException e) {
          throw new RecommendationException("Failed to get game data for recommendation");
        }
        recommendedItemMap.put(entry.getKey(), recommendByTopGames(ItemType.valueOf(entry.getKey()), topGames));
      } else {
        recommendedItemMap.put(entry.getKey(), recommendByFavoriteHistory(favoriteItemIds, entry.getValue(), ItemType.valueOf(entry.getKey())));
      }
    }
    return recommendedItemMap;
  }

  public Map<String, List<Item>> recommendItemsByDefault() throws RecommendationException {
    Map<String, List<Item>> recommendedItemMap = new HashMap<>();
    TwitchClient client = new TwitchClient();
    List<Game> topGames;
    try {
      topGames = client.topGames(DEFAULT_GAME_LIMIT);
    } catch (TwitchException e) {
      throw new RecommendationException("Failed to get game data for recommendation");
    }

    for (ItemType type : ItemType.values()) {
      recommendedItemMap.put(type.toString(), recommendByTopGames(type, topGames));
    }
    return recommendedItemMap;
  }

}
