package fxgame;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LabEight {

	private static final Random RANDOM = new Random();

	private static final Pane pane = new Pane();
	private static final Scene scene = new Scene(pane);

	private static final VBox gameOverPane = new VBox();
	private static final Text restartText = new Text("[Press 2 or ENTER to restart]");

	private static final Brinn player = new Brinn();
	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final List<AnimSprite> monsters = new ArrayList<AnimSprite>();

	private static AnimationTimer animationTimer;
	private static final LongProperty lastUpdateTime = new SimpleLongProperty();
	private static Timeline monstersTimeline;

	private static final MediaPlayer music = new MediaPlayer(
		new Media(LabEight.class.getResource("audio/intro.mp3").toString())
	);

	private static final AudioClip gameOverSoundEffect = new AudioClip(
		LabEight.class.getResource("audio/gameover.wav").toString()
	);
	private static final MediaPlayer gameOverMusic = new MediaPlayer(
		new Media(LabEight.class.getResource("audio/gameover.mp3").toString())
	);

	private static final Set<KeyCode> keysPressed = new HashSet<KeyCode>();

	static {
		pane.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY, null, null)));

		pane.getChildren().add(player.getImageView());
		pane.setMinSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);

		// Set player starting position to center of pane
		player.getImageView().setTranslateX(pane.getMinWidth()/2 - player.getWidth()/2);
		player.getImageView().setTranslateY(pane.getMinHeight()/2 - player.getHeight()/2);
		player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());
		sprites.add(player);

		// Initialize trees
		createTrees();

		// Initialize monsters
		createMonsters();

		music.setVolume(0.6);
		music.setCycleCount(AudioClip.INDEFINITE);

		gameOverMusic.setVolume(0.5);
		createGameOverPane();

		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {
					double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;

					animatePlayer(elapsedSeconds);
					animateMonsters(elapsedSeconds);

					// Check if the player has collided with a monster
					checkForMonsterCollision();
				}
				lastUpdateTime.set(timestamp);
			}
		};
		animationTimer.start();

		// Start loop that controls monster walking directions
		setMonsterDirections();

		// Start key event handlers for player movement
	    startKeyPressedEventHandler();
		startKeyReleasedEventHandler();
	}


	public static Scene getScene() {
		music.play();
		return scene;
	}

	// Initialize trees and set their positions on the pane
	private static void createTrees() {
		for (int i = 1; i <= 7; i++) {
			Tree tree = new Tree();
			pane.getChildren().add(tree.getImageView());
			switch (i)
			{
			case 1: tree.getImageView().setX(200); tree.getImageView().setY(150); break;
			case 2: tree.getImageView().setX(400); tree.getImageView().setY(200); break;
			case 3: tree.getImageView().setX(100); tree.getImageView().setY(250); break;
			case 4: tree.getImageView().setX(600); tree.getImageView().setY(400); break;
			case 5: tree.getImageView().setX(330); tree.getImageView().setY(350); break;
			case 6: tree.getImageView().setX(40); tree.getImageView().setY(50); break;
			case 7: tree.getImageView().setX(610); tree.getImageView().setY(80); break;
			}
			tree.setPos(tree.getImageView().getX(), tree.getImageView().getY());
			sprites.add(tree);
			obstacles.add(tree.getCBox());
		}
	}

	// Initialize monsters and set their initial positions on the pane
	private static void createMonsters() {
		for (int i = 1; i <= 8; i++) {
			Skeleton skeleton = new Skeleton();
			pane.getChildren().add(skeleton.getImageView());
			sprites.add(skeleton);
			monsters.add(skeleton);
		}
		initMonsterPos();
	}

	// Set initial positions on the pane for each monster
	private static void initMonsterPos() {
		int i = 1;
		for (AnimSprite monster : monsters) {
			monster.standFront();
			switch (i)
			{
			case 1: monster.getImageView().setTranslateX(100); monster.getImageView().setTranslateY(150); break;
			case 2: monster.getImageView().setTranslateX(300); monster.getImageView().setTranslateY(400); break;
			case 3: monster.getImageView().setTranslateX(80); monster.getImageView().setTranslateY(180); break;
			case 4: monster.getImageView().setTranslateX(600); monster.getImageView().setTranslateY(340); break;
			case 5: monster.getImageView().setTranslateX(200); monster.getImageView().setTranslateY(40); break;
			case 6: monster.getImageView().setTranslateX(520); monster.getImageView().setTranslateY(200); break;
			case 7: monster.getImageView().setTranslateX(610); monster.getImageView().setTranslateY(0); break;
			case 8: monster.getImageView().setTranslateX(400); monster.getImageView().setTranslateY(100); break;
			default: monster.getImageView().setTranslateX(400); monster.getImageView().setTranslateY(100);
			}
			monster.setPos(monster.getImageView().getTranslateX(), monster.getImageView().getTranslateY());
			i++;
		}
	}

	// Animate the player movement based on velocity set by key presses
	private static void animatePlayer(double elapsedSeconds) {
		double deltaX = elapsedSeconds * player.getXVelocity();
		double deltaY = elapsedSeconds * player.getYVelocity();
		double oldX = player.getImageView().getTranslateX();
		double newX = Math.max(0, Math.min(pane.getWidth() - player.getWidth(), oldX + deltaX));
		double oldY = player.getImageView().getTranslateY();
		double newY = Math.max(0, Math.min(pane.getHeight() - player.getHeight(), oldY + deltaY));
		boolean collision = checkForObstacleCollision(player, newX, newY);
		if (!collision) {
			player.getImageView().setTranslateX(newX);
			player.getImageView().setTranslateY(newY);
			player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());
			reorderNodes();
		}
	}

	// Animate the monsters movement based on their randomly set velocities
	private static void animateMonsters(double elapsedSeconds) {
		for (AnimSprite monster : monsters) {
			double sDeltaX = elapsedSeconds * monster.getXVelocity();
			double sDeltaY = elapsedSeconds * monster.getYVelocity();
			double sOldX = monster.getImageView().getTranslateX();
			double sNewX = Math.max(0, Math.min(pane.getWidth() - monster.getWidth(), sOldX + sDeltaX));
			double sOldY = monster.getImageView().getTranslateY();
			double sNewY = Math.max(0, Math.min(pane.getHeight() - monster.getHeight(), sOldY + sDeltaY));
			boolean sCollision = checkForObstacleCollision(monster, sNewX,sNewY);
			if (!sCollision) {
				monster.getImageView().setTranslateX(sNewX);
				monster.getImageView().setTranslateY(sNewY);
				monster.setPos(monster.getImageView().getTranslateX(), monster.getImageView().getTranslateY());
				reorderNodes();
			}
			fixMonsterDirection(monster, sOldX + sDeltaX, sOldY + sDeltaY);
		}
	}

	// Sends each monster in a random direction every 2 seconds
	private static void setMonsterDirections() {
		monstersTimeline = new Timeline(
				new KeyFrame(
						Duration.ZERO, e -> {
							for (AnimSprite monster : monsters) {
								int randomDirection = RANDOM.nextInt(4);
								switch(randomDirection) {
									case 0: monster.walkDown(); break;
									case 1: monster.walkUp(); break;
									case 2: monster.walkRight(); break;
									case 3: monster.walkLeft();
								}
							}
						}
				),
				new KeyFrame(Duration.seconds(2))
		);
		monstersTimeline.setCycleCount(Timeline.INDEFINITE);
		monstersTimeline.play();
	}

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

	private static boolean checkForObstacleCollision(Sprite sprite, double newX, double newY) {
		// If sprite is a monster check for collisions with other monsters
		if (sprite != player) {
			for (Sprite monster : monsters) {
				if (monster != sprite) {
					if (monster.getCBox().intersects(
							newX + sprite.getCBoxOffsetX(), newY + sprite.getCBoxOffsetY(),
							sprite.getCBoxWidth(), sprite.getCBoxHeight())) {
						if (sprite == player) {
							gameOver();
						}
						return true;
					}
				}
			}
		}
		// Check for collisions with obstacles
		for (Rectangle2D obstacle : obstacles) {
			if (obstacle.intersects(newX + sprite.getCBoxOffsetX(), newY + sprite.getCBoxOffsetY(),
					sprite.getCBoxWidth(), sprite.getCBoxHeight())) {
				return true;
			}
		}
		return false;
	}

	// Check if player has collided with a monster and if so call game over
	private static void checkForMonsterCollision() {
		for (Sprite monster : monsters) {
			if (monster.getCBox().intersects(player.getCBox())) {
				gameOver();
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

	private static void createGameOverPane() {
		Text gameOverText = new Text("game over");
		gameOverText.setFill(Color.WHITE);
		gameOverText.setFont(Font.loadFont(LabEight.class.getResourceAsStream("fonts/MonsterFriendFore.otf"), 50));

		// Instructions for restarting the game (starts out hidden)
		restartText.setFill(Color.BLACK);
		restartText.setFont(Font.loadFont(LabEight.class.getResourceAsStream("fonts/DTM-Mono.otf"), 20));

		gameOverPane.getChildren().addAll(gameOverText, restartText);
		gameOverPane.setSpacing(50);
		gameOverPane.setAlignment(Pos.CENTER);
		gameOverPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		gameOverPane.prefWidthProperty().bind(pane.widthProperty());
		gameOverPane.prefHeightProperty().bind(pane.heightProperty());
	}

	private static void gameOver() {
		music.stop();
		animationTimer.stop();
		monstersTimeline.stop();

		gameOverSoundEffect.play();

		// Play music after 3 seconds, once the game over sound effect finishes
		Timeline delayMusic = new Timeline(new KeyFrame(Duration.seconds(3), e -> gameOverMusic.play()));

		// Add game over pane, display the instructions for restarting after 3 seconds
		restartText.setFill(Color.BLACK);
		pane.getChildren().add(gameOverPane);
		Timeline delayRestartText = new Timeline(
				new KeyFrame(Duration.seconds(3), e -> restartText.setFill(Color.LIGHTGRAY))
		);

		// Let key press restart game after 3 seconds on the game over pane
		Timeline delayRestart = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
			scene.setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DIGIT2) {
					pane.getChildren().remove(gameOverPane);
					restart();
				}
			});
		}));

		delayMusic.play();
		delayRestartText.play();
		delayRestart.play();
	}

	private static void restart() {
		keysPressed.clear();
		music.play();
		gameOverMusic.stop();

		// Reset player position to center of pane
		player.standFront();
		player.getImageView().setTranslateX(pane.getMinWidth()/2 - player.getWidth()/2);
		player.getImageView().setTranslateY(pane.getMinHeight()/2 - player.getHeight()/2);
		player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());

		// Reset monster positions
		initMonsterPos();

		// Restart animations
		animationTimer.start();
		monstersTimeline.play();
		startKeyPressedEventHandler();
	}

}
