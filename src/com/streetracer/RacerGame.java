package com.streetracer;

import com.codegym.engine.cell.*;
import com.streetracer.raceutils.Direction;
import com.streetracer.raceutils.FinishLine;
import com.streetracer.raceutils.PlayerCar;
import com.streetracer.raceutils.ProgressBar;
import com.streetracer.raceutils.RoadMarking;
import com.streetracer.road.RoadManager;

public class RacerGame extends Game {
  public static final int WIDTH = 64;
  public static final int HEIGHT = 64;
  public static final int CENTER_X = WIDTH / 2;
  public static final int ROADSIDE_WIDTH = 14;
  private static final int RACE_GOAL_CARS_COUNT = 40;
  private RoadMarking roadMarking;
  private PlayerCar player;
  private RoadManager roadManager;
  private boolean isGameStopped;
  private FinishLine finishLine;
  private ProgressBar progressBar;
  private int score;

  @Override
  public void initialize() {
    showGrid(false);
    setScreenSize(WIDTH, HEIGHT);
    createGame();
  }

  private void createGame() {
    roadMarking = new RoadMarking();
    player = new PlayerCar();
    roadManager = new RoadManager();
    finishLine = new FinishLine();
    progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
    drawScene();
    setTurnTimer(40);
    isGameStopped = false;
    score = 3500;
  }
  
  private void drawScene() {
    drawField();
    progressBar.draw(this);
    finishLine.draw(this);
    roadManager.draw(this);
    roadMarking.draw(this);
    player.draw(this);
  }

  private void drawField() {
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        if (x == CENTER_X) {
          setCellColor(CENTER_X, y, Color.WHITE);
        } else if (x >= ROADSIDE_WIDTH && x < WIDTH - ROADSIDE_WIDTH) {
          setCellColor(x, y, Color.DIMGREY);
        } else {
          setCellColor(x, y, Color.GREEN);
        }
      }
    }
  }

  @Override
  public void setCellColor(int x, int y, Color color) {
    if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
      return;
    }
    super.setCellColor(x, y, color);
  }

  private void moveAll() {
    roadMarking.move(player.speed);
    player.move();
    roadManager.move(player.speed);
    finishLine.move(player.speed);
    progressBar.move(roadManager.getPassedCarsCount());
  }

  @Override
  public void onTurn(int step) {
    if(roadManager.checkCrash(player)) {
      gameOver();      
      drawScene();
      return;
    }
    if(finishLine.isCrossed(player)) {
      win();
      drawScene();
      return;
    }
    if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
      finishLine.show();
    }
    moveAll();
    roadManager.generateNewRoadObjects(this);
    score -= 5;
    setScore(score);
    drawScene();
  }

  @Override
  public void onKeyPress(Key key) {
    if (key == Key.LEFT) {
      player.setDirection(Direction.LEFT);
    } else if (key == Key.RIGHT) {
      player.setDirection(Direction.RIGHT);
    } else if (key == Key.SPACE && isGameStopped) {
      createGame();
    } else if (key == Key.UP) {
      player.speed = 2;
    }
  }

  @Override
  public void onKeyReleased(Key key) {
    if (key == Key.LEFT && player.getDirection() == Direction.LEFT
        || key == Key.RIGHT && player.getDirection() == Direction.RIGHT) {
      player.setDirection(Direction.NONE);
    } else if (key == Key.UP) {
      player.speed = 1;
    }
  }

  private void gameOver() {
    isGameStopped = true;
    showMessageDialog(Color.RED, "Game over", Color.BLACK, 100);
    stopTurnTimer();
    player.stop();
  }

  private void win() {
    isGameStopped = true;
    showMessageDialog(Color.GREEN, "You win", Color.BLACK, 100);
    stopTurnTimer();
  }
}
