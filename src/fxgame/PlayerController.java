package fxgame;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fxgame.Game.GameState;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class PlayerController {

	private static final Random RANDOM = new Random();

	private static Scene scene;
	private static Brinn player = Game.getPlayer();

	private static List<Sprite> sprites;
	private static List<AnimSprite> monsters;
	private static List<Rectangle2D> obstacles;
	private static Map<KeyCode, GameState> exits;

	private static AnimationTimer animationTimer;
	private static final LongProperty lastUpdateTime = new SimpleLongProperty();

	// How far the player can travel outside the scene (to travel to another scene)
	public static final int OFFSCREEN_X = 19;
	public static final int OFFSCREEN_Y = 31;

	PlayerController() {
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {
					double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
					animatePlayer(elapsedSeconds);
					animateMonsters(elapsedSeconds);
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

	public void setVals(List<Sprite> sprites, List<AnimSprite> monsters,
			List<Rectangle2D> obstacles, Map<KeyCode, GameState> exits) {
		PlayerController.sprites = sprites;
		PlayerController.monsters = monsters;
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
		double oldX = player.getXPos();
		double newX = Math.max(0 - OFFSCREEN_X, Math.min(scene.getWidth() - OFFSCREEN_X, oldX + deltaX));
		double oldY = player.getYPos();
		double newY = Math.max(0 - OFFSCREEN_Y, Math.min(scene.getHeight() - OFFSCREEN_Y, oldY + deltaY));

		boolean collision = checkForObstacleCollision(player, newX, newY);
		if (!collision) {
			player.setPos(newX, newY);
			reorderNodes();
		}

		GameState newScene = checkIfExit(newX, newY);
		if (newScene != null) {
			Game.setPane(newScene);
		}

		// Check if the player has collided with a monster
		checkForMonsterCollision(newX, newY);
	}

	// Animate the monsters movement based on their randomly set velocities
	private static void animateMonsters(double elapsedSeconds) {
		for (AnimSprite monster : monsters) {
			double sDeltaX = elapsedSeconds * monster.getXVelocity();
			double sDeltaY = elapsedSeconds * monster.getYVelocity();
			double sOldX = monster.getXPos();
			double sNewX = Math.max(
				0, Math.min(Game.WINDOW_WIDTH - player.getWidth() - monster.getWidth(), sOldX + sDeltaX)
			);
			double sOldY = monster.getYPos();
			double sNewY = Math.max(
				0, Math.min(Game.WINDOW_HEIGHT - player.getHeight() - monster.getHeight(), sOldY + sDeltaY)
			);
			boolean sCollision = checkForObstacleCollision(monster, sNewX,sNewY);
			if (!sCollision) {
				monster.setPos(sNewX, sNewY);
				reorderNodes();
			}
			fixMonsterDirection(monster, sOldX + sDeltaX, sOldY + sDeltaY);
		}
	}

	// Change direction if obstacle collision
	private static void fixMonsterDirection(AnimSprite monster, double desiredX, double desiredY) {
		int randomDirection = RANDOM.nextInt(3);
		if (monster.getXPos() < desiredX) {
			switch(randomDirection) {
				case 0: monster.walkDown(); break;
				case 1: monster.walkUp(); break;
				case 2: monster.walkLeft();
			}
		}
		else if (monster.getXPos() > desiredX) {
			switch(randomDirection) {
				case 0: monster.walkDown(); break;
				case 1: monster.walkUp(); break;
				case 2: monster.walkRight();
			}
		}
		else if (monster.getYPos() < desiredY) {
			switch(randomDirection) {
				case 0: monster.walkLeft(); break;
				case 1: monster.walkUp(); break;
				case 2: monster.walkRight();
			}
		}
		else if (monster.getYPos() > desiredY) {
			switch(randomDirection) {
				case 0: monster.walkDown(); break;
				case 1: monster.walkLeft(); break;
				case 2: monster.walkRight();
			}
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
				&& x > 256 && x < 294 && y < 340)
			return GameState.ROOM;

		return null;
	}

	// Check for collisions with obstacles
	private static boolean checkForObstacleCollision(Sprite sprite, double newX, double newY) {
		for (Sprite s : sprites) {
			if (s != sprite) {
				if (s.getCBox().intersects(sprite.getNewCBox(newX, newY))) {
					return true;
				}
			}
		}
		for (Rectangle2D obstacle : obstacles) {
			if (obstacle.intersects(sprite.getNewCBox(newX, newY))) {
				return true;
			}
		}

		// Temporarily hard-coded for skeletons pane TODO
		if (monsters.contains(sprite) && newX < 192 + player.getWidth())
			return true;

		return false;
	}

	// Check if player has collided with a monster and if so call game over
	private static void checkForMonsterCollision(double newX, double newY) {
		for (Sprite monster : monsters) {
			if (monster.getCBox().intersects(player.getNewCBox(newX, newY))) {
				Game.setPane(GameState.GAME_OVER);
				break;
			}
		}
	}

	// Change the z-order of sprites on the pane based on xPos of collision box
	private static void reorderNodes() {
		Collections.sort(sprites);
		for (Sprite sprite : sprites) {
			sprite.getImageView().toFront();
		}
	}

}
