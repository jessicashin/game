package fxgame;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fxgame.Game.GameState;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class PlayerController {

	private static Scene scene;
	private static Brinn player = Game.getPlayer();

	private static List<Sprite> sprites;
	private static List<Rectangle2D> obstacles;
	private static Map<KeyCode, GameState> exits;

	private static AnimationTimer animationTimer;
	private static final LongProperty lastUpdateTime = new SimpleLongProperty();

	// How far the player can travel outside the scene (to travel to another scene)
	private static final int OFFSCREEN_X = 19;
	private static final int OFFSCREEN_Y = 31;

	PlayerController() {
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {
					double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
					animatePlayer(elapsedSeconds);
				}
				lastUpdateTime.set(timestamp);
			}
		};
	}

	public void start() {
		scene = Game.getScene();
		animationTimer.start();
		startKeyPressedEventHandler();
		startKeyReleasedEventHandler();
	}

	public void setVals(List<Sprite> sprites, List<Rectangle2D> obstacles, Map<KeyCode, GameState> exits) {
		PlayerController.sprites = sprites;
		PlayerController.obstacles = obstacles;
		PlayerController.exits = exits;
	}

	private static final Set<KeyCode> keysPressed = new HashSet<KeyCode>();

	// Event handler for player movement using arrow keys
		private static void startKeyPressedEventHandler() {
			scene.setOnKeyPressed(e -> {
				KeyCode key = e.getCode();

				switch(key) {
					case UP:	player.walkUp(); break;
					case RIGHT:	player.walkRight(); break;
					case DOWN:	player.walkDown(); break;
					case LEFT:	player.walkLeft(); break;
					default: break;
				}

				keysPressed.add(key);
			});
		}

		// Event handler for player movement using arrow keys
		private static void startKeyReleasedEventHandler() {
			scene.setOnKeyReleased(e -> {
				KeyCode key = e.getCode();
				keysPressed.remove(key);

				if (keysPressed.size() == 1) {
					if (keysPressed.contains(KeyCode.UP))
						player.walkUp();
					else if (keysPressed.contains(KeyCode.RIGHT))
						player.walkRight();
					else if (keysPressed.contains(KeyCode.DOWN))
						player.walkDown();
					else if (keysPressed.contains(KeyCode.LEFT))
						player.walkLeft();
				}

				else if (keysPressed.isEmpty()) {
					switch(key) {
						case UP:	player.standBack(); break;
						case RIGHT:	player.standRight(); break;
						case DOWN:	player.standFront(); break;
						case LEFT:	player.standLeft(); break;
						default: break;
					}
				}
			});
		}

		// Animate the player movement based on velocity set by key presses
		private static void animatePlayer(double elapsedSeconds) {
			double deltaX = elapsedSeconds * player.getXVelocity();
			double deltaY = elapsedSeconds * player.getYVelocity();
			double oldX = player.getImageView().getTranslateX();
			double newX = Math.max(0 - OFFSCREEN_X, Math.min(scene.getWidth() - OFFSCREEN_X, oldX + deltaX));
			double oldY = player.getImageView().getTranslateY();
			double newY = Math.max(0 - OFFSCREEN_Y, Math.min(scene.getHeight() - OFFSCREEN_Y, oldY + deltaY));

			boolean collision = checkForObstacleCollision(newX, newY);
			if (!collision) {
				player.getImageView().setTranslateX(newX);
				player.getImageView().setTranslateY(newY);
				player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());
				reorderNodes();
			}

			GameState newScene = checkIfExit(newX, newY);
			if (newScene != null) {
				Game.setPane(newScene);
			}
		}

		// Check if player is exiting scene
		private static GameState checkIfExit(double x, double y) {
			if (keysPressed.contains(KeyCode.UP) && exits.containsKey(KeyCode.UP)
					&& y == 0 - OFFSCREEN_Y)
				return exits.get(KeyCode.UP);

			else if (keysPressed.contains(KeyCode.RIGHT) && exits.containsKey(KeyCode.RIGHT)
					&& x == scene.getWidth() - OFFSCREEN_X)
				return exits.get(KeyCode.RIGHT);

			else if (keysPressed.contains(KeyCode.DOWN) && exits.containsKey(KeyCode.DOWN)
					&& y == scene.getHeight() - OFFSCREEN_Y)
				return exits.get(KeyCode.DOWN);

			else if (keysPressed.contains(KeyCode.LEFT) && exits.containsKey(KeyCode.LEFT)
					&& x == 0 - OFFSCREEN_X)
				return exits.get(KeyCode.LEFT);

			// If igloo door, change to room scene
			else if (Game.getCurrentState() == GameState.HOME && keysPressed.contains(KeyCode.UP)
					&& x > 260 && x < 282 && y < 340)
				return GameState.ROOM;

			return null;
		}

		// Check for collisions with obstacles
		private static boolean checkForObstacleCollision(double newX, double newY) {
			for (Sprite sprite : sprites) {
				if (sprite != player) {
					if (sprite.getCBox().intersects(newX + player.getCBoxOffsetX(), newY + player.getCBoxOffsetY(),
							player.getCBoxWidth(), player.getCBoxHeight())) {
						return true;
					}
				}
			}
			for (Rectangle2D obstacle : obstacles) {
				if (obstacle.intersects(newX + player.getCBoxOffsetX(), newY + player.getCBoxOffsetY(),
						player.getCBoxWidth(), player.getCBoxHeight())) {
					return true;
				}
			}
			return false;
		}

		// Change the z-order of sprites on the pane based on xPos of collision box
		private static void reorderNodes() {
			Collections.sort(sprites);
			for (Sprite sprite : sprites) {
				sprite.getImageView().toFront();
			}
		}

}
