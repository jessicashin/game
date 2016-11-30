package fxgame;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fxgame.Game.GameState;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

// This class controls the player movement (from key events),
// the monster movements (random), and the dog movement (follows player).
// It takes into account all the sprites and obstacles and exits
// defined by each pane

public class Controller {

	private static final Random RANDOM = new Random();

	private static Scene scene;
	private static Pane pane;
	private static Brinn player = Game.getPlayer();
	private static Punch playerPunch = new Punch();
	private static Luffy dog = Game.getDog();

	private static List<Sprite> sprites;
	private static List<AnimatedSprite> monsters;
	private static List<Rectangle2D> obstacles;
	private static List<InteractionBox> interactions;
	private static Map<KeyCode, GameState> exits;

	private static Pane currentModalPane = null;

	private static AnimationTimer animationTimer;
	private static final LongProperty lastUpdateTime = new SimpleLongProperty();
	private static long startTimerTime;

	private static boolean keyPressHasAttacked = false;
	private static boolean isPunching = false;
	private static Timeline punchTimeline = new Timeline(
		new KeyFrame(Duration.millis(120), e -> {
			for (ImageView image : playerPunch.getAllImages()) {
				pane.getChildren().remove(image);
			}
			switch(player.getDirection()) {
				case UP:	player.setToBackViewport(); break;
				case RIGHT:	player.setToRightViewport(); break;
				case DOWN:	player.setToFrontViewport(); break;
				case LEFT:	player.setToLeftViewport(); break;
				default: break;
			}
		}),
		new KeyFrame(Duration.millis(180), e -> isPunching = false)
	);

	private static final AudioClip wooshSound = new AudioClip(
		Controller.class.getResource("audio/woosh.wav").toString()
	);
	private static final AudioClip bashSound = new AudioClip(
		Controller.class.getResource("audio/bash.wav").toString()
	);

	// How far the player can travel outside the scene (to travel to another scene)
	public static final int OFFSCREEN_X = 19;
	public static final int OFFSCREEN_Y = 31;

	Controller() {
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {
					double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
					animatePlayer(elapsedSeconds);
					if (isPunching)
						animatePlayerPunch();
					animateMonsters(elapsedSeconds);
					double timeSinceStart = (timestamp - startTimerTime) / 1_000_000_000.0;
					if (Game.getCurrentState() == GameState.ROOM && timeSinceStart > 1.6)
						animateDog(elapsedSeconds);
					setDogInteractionBox();
					reorderNodes();
				}
				lastUpdateTime.set(timestamp);
			}
		};
	}

	public void start() {
		scene = Game.getScene();
		startTimerTime = System.nanoTime();
		animationTimer.start();
		startKeyPressedEventHandler();
		startKeyReleasedEventHandler();
	}

	public void setVals(Pane pane, List<Sprite> sprites, List<AnimatedSprite> monsters,
			List<Rectangle2D> obstacles, List<InteractionBox> interactions, Map<KeyCode, GameState> exits) {
		Controller.pane = pane;
		Controller.sprites = sprites;
		Controller.monsters = monsters;
		Controller.obstacles = obstacles;
		Controller.interactions = interactions;
		Controller.exits = exits;
	}

	private static final Set<KeyCode> keysPressed = new HashSet<KeyCode>();

	// Event handler for player movement using arrow keys
	private static void startKeyPressedEventHandler() {
		scene.setOnKeyPressed(e -> {
			KeyCode key = e.getCode();

			switch(key) {
				case UP:	player.walkUp(); keysPressed.add(key); break;
				case RIGHT:	player.walkRight(); keysPressed.add(key); break;
				case DOWN:	player.walkDown(); keysPressed.add(key); break;
				case LEFT:	player.walkLeft(); keysPressed.add(key); break;

				case Z: case ENTER:	checkIfInteracting(); break;

				case C: case SPACE:
					if (Game.getCurrentState() != GameState.ROOM &&
						!isPunching && !keyPressHasAttacked) {
						keyPressHasAttacked = true;
						isPunching = true;
						playerPunch();
					}
					break;

				case ESCAPE: Platform.exit();
				default: break;
			}
		});
	}

	// Event handler for player movement using arrow keys
	private static void startKeyReleasedEventHandler() {
		scene.setOnKeyReleased(e -> {
			KeyCode key = e.getCode();

			if (key == KeyCode.SPACE || key == KeyCode.C) {
				keyPressHasAttacked = false;
			}

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

		if (!checkForObstacleCollision(player, newX, newY)) {
			player.setPos(newX, newY);
		}

		GameState newScene = checkIfExit(newX, newY);
		if (newScene != null) {
			Game.setPane(newScene);
		}

		// Check if the player has attacked a monster
		if (isPunching) {
			checkIfPunchedMonster();
		}

		// Check if the player has collided with a monster (without attacking)
		checkForMonsterCollision(newX, newY);
	}

	private static void playerPunch() {
		switch(player.getDirection()) {
			case UP:	player.punchUp(); playerPunch.punchUp(); break;
			case RIGHT:	player.punchRight(); playerPunch.punchRight(); break;
			case DOWN:	player.punchDown(); playerPunch.punchDown(); break;
			case LEFT:	player.punchLeft(); playerPunch.punchLeft(); break;
			default: break;
		}
		wooshSound.play();
		pane.getChildren().add(playerPunch.getImageView());
		punchTimeline.play();
	}

	// Animate the player's punch attack to the player's position
	private static void animatePlayerPunch() {
		switch(player.getDirection()) {
		case UP: playerPunch.punchUp(); break;
		case RIGHT: playerPunch.punchRight(); break;
		case DOWN: playerPunch.punchDown(); break;
		case LEFT: playerPunch.punchLeft(); break;
		default: break; }
	}

	// Animate the dog to follow the player around their room
	private static void animateDog(double elapsedSeconds) {
		double oldX = dog.getXPos();
		double oldY = dog.getYPos();
		double deltaX = 0;
		double deltaY = 0;
		if (player.getYPos() + player.getHeight() < dog.getYPos() + dog.getCBoxOffsetY()) {
			deltaY = elapsedSeconds * -dog.getSpeed();
			dog.walkUp();
		}
		else if (player.getYPos() - dog.getCBoxHeight() > dog.getYPos()) {
			deltaY = elapsedSeconds * dog.getSpeed();
			dog.walkDown();
		}
		if (player.getXPos() + player.getWidth() < dog.getXPos()) {
			deltaX = elapsedSeconds * -dog.getSpeed();
			dog.walkLeft();
		}
		else if (player.getXPos() - dog.getWidth() > dog.getXPos()) {
			deltaX = elapsedSeconds * dog.getSpeed();
			dog.walkRight();
		}
		if (deltaX == 0 && deltaY == 0) {
			switch(dog.getDirection())
			{
				case UP: dog.standBack(); break;
				case RIGHT: dog.standRight(); break;
				case DOWN: dog.standFront(); break;
				case LEFT: dog.standLeft(); break;
				default: break;
			}
		}
		double newX = Math.max(0 + OFFSCREEN_X*2, Math.min(scene.getWidth() - OFFSCREEN_X*2, oldX + deltaX));
		double newY = Math.max(0 + OFFSCREEN_Y*2, Math.min(scene.getHeight() - OFFSCREEN_Y*2, oldY + deltaY));
		if (!checkForObstacleCollision(dog, newX, newY))
			dog.setPos(newX, newY);
	}

	private static void setDogInteractionBox() {
		dog.getInteractionBox().setDirection(player.getDirection());
		switch(player.getDirection()) {
			case UP:
				dog.getInteractionBox().setBox(
					new Rectangle2D(dog.getXPos() + dog.getCBoxOffsetX(),
						dog.getYPos() + dog.getCBoxOffsetY() + dog.getCBoxHeight(), dog.getCBoxWidth(), 4)
				);
				break;
			case RIGHT:
				dog.getInteractionBox().setBox(
					new Rectangle2D(dog.getXPos() + dog.getCBoxOffsetX() - 4,
						dog.getYPos() + dog.getCBoxOffsetY(), 4, dog.getCBoxHeight())
				);
				break;
			case DOWN:
				dog.getInteractionBox().setBox(
					new Rectangle2D(dog.getXPos() + dog.getCBoxOffsetX(),
						dog.getYPos() + dog.getCBoxOffsetY() - 4, dog.getCBoxWidth(), 4)
				);
				break;
			case LEFT:
				dog.getInteractionBox().setBox(
					new Rectangle2D(dog.getXPos() + dog.getCBoxOffsetX() + dog.getCBoxWidth(),
						dog.getYPos() + dog.getCBoxOffsetY(), 4, dog.getCBoxHeight())
				);
				break;
			default: break;
		}
	}

	// Animate the monsters movement based on their randomly set velocities
	private static void animateMonsters(double elapsedSeconds) {
		for (AnimatedSprite monster : monsters) {
			if (pane.getChildren().contains(monster.getImageView())) {
				double deltaX = elapsedSeconds * monster.getXVelocity();
				double deltaY = elapsedSeconds * monster.getYVelocity();
				double oldX = monster.getXPos();
				double newX = Math.max(
					0, Math.min(Game.WINDOW_WIDTH - player.getWidth() - monster.getWidth(), oldX + deltaX)
				);
				double oldY = monster.getYPos();
				double newY = Math.max(
					0, Math.min(Game.WINDOW_HEIGHT - player.getHeight() - monster.getHeight(), oldY + deltaY)
				);
				if (!checkForObstacleCollision(monster, newX, newY)) {
					monster.setPos(newX, newY);
				}
				if (monster.getXPos() != oldX + deltaX || monster.getYPos() != oldY + deltaY) {
					fixMonsterDirection(monster);
				}
			}
		}
	}

	// Change direction if obstacle collision
	private static void fixMonsterDirection(AnimatedSprite monster) {
		int randomDirection = RANDOM.nextInt(3);
		if (monster.isFacingRight()) {
			switch(randomDirection) {
				case 0: monster.walkDown(); break;
				case 1: monster.walkUp(); break;
				case 2: monster.walkLeft();
			}
		}
		else if (monster.isFacingLeft()) {
			switch(randomDirection) {
				case 0: monster.walkDown(); break;
				case 1: monster.walkUp(); break;
				case 2: monster.walkRight();
			}
		}
		else if (monster.isFacingDown()) {
			switch(randomDirection) {
				case 0: monster.walkLeft(); break;
				case 1: monster.walkUp(); break;
				case 2: monster.walkRight();
			}
		}
		else if (monster.isFacingUp()) {
			switch(randomDirection) {
				case 0: monster.walkDown(); break;
				case 1: monster.walkLeft(); break;
				case 2: monster.walkRight();
			}
		}
	}

	// Check if player is facing something that can be interacted with
	private static void checkIfInteracting() {
		for (InteractionBox box : interactions) {
			if (player.getCBox().intersects(box.getBox()) && player.getDirection() == box.getDirection()) {
				if (box == dog.getInteractionBox()) {
					switch(player.getDirection()) {
						case UP:	dog.setDirection(KeyCode.DOWN); break;
						case RIGHT:	dog.setDirection(KeyCode.LEFT); break;
						case DOWN:	dog.setDirection(KeyCode.UP); break;
						case LEFT:	dog.setDirection(KeyCode.RIGHT); break;
						default: break;
					}
				}
				Game.addModalPane(box.getModalPaneAndPlay());
				currentModalPane = box.getModalPane();
				switch(player.getDirection()) {
					case UP:	player.standBack(); break;
					case RIGHT:	player.standRight(); break;
					case DOWN:	player.standFront(); break;
					case LEFT:	player.standLeft(); break;
					default: break;
				}
				// Disable key events for player movement in modal view
				scene.setOnKeyReleased(e -> {});
				scene.setOnKeyPressed(e -> {
					boolean animationsFinished = false;
					if ((e.getCode() == KeyCode.Z || e.getCode() == KeyCode.ENTER)) {
						if ((box.getTextAnimation() != null &&
							box.getTextAnimation().getStatus() == Animation.Status.STOPPED) ||
							box.isTextTimelineFinished()) {
							animationsFinished = true;
						}
						if (animationsFinished) {
							Game.removeModalPane(box.getModalPane());
							currentModalPane = null;
							keysPressed.clear();
							startKeyPressedEventHandler();
							startKeyReleasedEventHandler();
						}
					}
					else if (e.getCode() == KeyCode.X || e.getCode() == KeyCode.SHIFT) {
						box.fastForwardText();
					}

					else if (e.getCode() == KeyCode.ESCAPE) {
						Platform.exit();
					}
				});
			}
		}
	}

	// Check if player is exiting scene
	private static GameState checkIfExit(double x, double y) {
		if (y == 0 - OFFSCREEN_Y && exits.containsKey(KeyCode.UP) && player.isFacingUp())
			return exits.get(KeyCode.UP);

		else if (x == scene.getWidth() - OFFSCREEN_X && exits.containsKey(KeyCode.RIGHT)
				&& player.isFacingRight())
			return exits.get(KeyCode.RIGHT);

		else if (y == scene.getHeight() - OFFSCREEN_Y && exits.containsKey(KeyCode.DOWN)
				&& player.isFacingDown())
			return exits.get(KeyCode.DOWN);

		else if (x == 0 - OFFSCREEN_X && exits.containsKey(KeyCode.LEFT) && player.isFacingLeft())
			return exits.get(KeyCode.LEFT);

		// If igloo door, change to room scene
		else if (x > 256 && x < 294 && y < 340 && Game.getCurrentState() == GameState.HOME
				&& player.isFacingUp())
			return GameState.ROOM;

		return null;
	}

	// Check for collisions with obstacles
	private static boolean checkForObstacleCollision(Sprite sprite, double newX, double newY) {
		for (Sprite s : sprites) {
			if (s != sprite && s != player) {
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

	// Check if player has punched a monster
	private static void checkIfPunchedMonster() {
		for (Sprite monster : monsters) {
			if (monster.getBounds().intersects(playerPunch.getCBox())) {
				bashSound.play();
				pane.getChildren().remove(monster.getImageView());
				monster.setPos(0, -200);
			}
		}
	}

	// Check if player has collided with a monster and if so call game over
	private static void checkForMonsterCollision(double newX, double newY) {
		for (Sprite monster : monsters) {
			if (monster.getCBox().intersects(player.getNewCBox(newX, newY))) {
				scene.setOnKeyPressed(empty -> {});
				player.setPos(-100, -100);
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
		if (currentModalPane != null)
			currentModalPane.toFront();
	}

}
