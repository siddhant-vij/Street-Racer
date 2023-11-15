package com.streetracer.raceutils;

import com.streetracer.RacerGame;
import com.streetracer.road.RoadManager;

public class PlayerCar extends GameObject {
  private static int playerCarHeight = ShapeMatrix.PLAYER.length;
  public int speed = 1;
  private Direction direction;

  public PlayerCar() {
    super(RacerGame.WIDTH / 2 + 2, RacerGame.HEIGHT - playerCarHeight - 1, ShapeMatrix.PLAYER);
  }

  public Direction getDirection() {
    return this.direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public void move() {
    if (this.x < RoadManager.LEFT_BORDER) {
      this.x = RoadManager.LEFT_BORDER;
    }
    if (this.x > RoadManager.RIGHT_BORDER - this.width) {
      this.x = RoadManager.RIGHT_BORDER - this.width;
    }
    if (this.direction == Direction.LEFT) {
      this.x--;
    } else if (this.direction == Direction.RIGHT) {
      this.x++;
    }
  }

  public void stop() {
    matrix = ShapeMatrix.PLAYER_DEAD;
  }
}
