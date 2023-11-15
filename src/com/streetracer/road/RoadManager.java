package com.streetracer.road;

import java.util.ArrayList;
import java.util.List;

import com.codegym.engine.cell.Game;
import com.streetracer.RacerGame;
import com.streetracer.raceutils.PlayerCar;

public class RoadManager {
  public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
  public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
  private static final int FIRST_LANE_POSITION = 16;
  private static final int FOURTH_LANE_POSITION = 44;
  private static final int PLAYER_CAR_DISTANCE = 12;
  private List<RoadObject> items = new ArrayList<>();
  private int passedCarsCount = 0;

  public int getPassedCarsCount() {
    return this.passedCarsCount;
  }

  private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
    switch (type) {
      case SPIKE:
        return new Spike(x, y);
      case DRUNK_CAR:
        return new MovingCar(x, y);
      default:
        return new Car(type, x, y);
    }
  }

  private void addRoadObject(RoadObjectType type, Game game) {
    int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
    int y = -1 * RoadObject.getHeight(type);
    RoadObject roadObject = createRoadObject(type, x, y);
    if (roadObject != null && isRoadSpaceFree(roadObject)) {
      items.add(roadObject);
    }
  }

  public void draw(Game game) {
    for (RoadObject roadObject : items) {
      roadObject.draw(game);
    }
  }

  public void move(int boost) {
    for (RoadObject roadObject : items) {
      roadObject.move(roadObject.speed + boost, items);
    }
    deletePassedItems();
  }

  private boolean spikeExists() {
    for (RoadObject roadObject : items) {
      if (roadObject.type == RoadObjectType.SPIKE) {
        return true;
      }
    }
    return false;
  }

  private void generateSpike(Game game) {
    if(game.getRandomNumber(100) < 10 && !spikeExists()) {
      addRoadObject(RoadObjectType.SPIKE, game);
    }
  }

  private void generateRegularCar(Game game) {
    int carTypeNumber = game.getRandomNumber(4);
    if(game.getRandomNumber(100) < 30) {
      addRoadObject(RoadObjectType.values()[carTypeNumber], game);
    }
  }

  public void generateNewRoadObjects(Game game) {
    generateSpike(game);
    generateRegularCar(game);
    generateMovingCar(game);
  }

  private void deletePassedItems() {
    for (RoadObject item : new ArrayList<>(items)) {
      if (item.y >= RacerGame.HEIGHT) {
        if (item.type != RoadObjectType.SPIKE) {
          passedCarsCount++;
        }
        items.remove(item);
      }
    }
  }

  public boolean checkCrash(PlayerCar car) {
    for (RoadObject roadObject : items) {
      if (roadObject.isCollision(car)) {
        return true;
      }
    }
    return false;
  }

  private boolean isRoadSpaceFree(RoadObject object) {
    for (RoadObject roadObject : items) {
      if (roadObject.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)) {
        return false;
      }
    }
    return true;
  }

  private boolean movingCarExists() {
    for (RoadObject roadObject : items) {
      if (roadObject.type == RoadObjectType.DRUNK_CAR) {
        return true;
      }
    }
    return false;
  }

  private void generateMovingCar(Game game) {
    if(game.getRandomNumber(100) < 10 && !movingCarExists()) {
      addRoadObject(RoadObjectType.DRUNK_CAR, game);
    }
  }
}
