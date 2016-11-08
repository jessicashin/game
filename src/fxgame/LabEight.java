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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LabEight {

	private static final Random RANDOM = new Random();

	private final Pane pane = new Pane();
	private final Scene scene = new Scene(pane);

	private final Brinn player = new Brinn();
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private List<Rectangle> obstacles = new ArrayList<Rectangle>();
	private List<AnimSprite> monsters = new ArrayList<AnimSprite>();
	private Rectangle monsterCBox = new Rectangle();

	private AnimationTimer animationTimer;
	private final LongProperty lastUpdateTime = new SimpleLongProperty();
	private Timeline timeline;

	private final MediaPlayer introMusic = new MediaPlayer(
		new Media(getClass().getResource("audio/intro.mp3").toString())
	);

	private final MediaPlayer gameOverMusic = new MediaPlayer(
		new Media(getClass().getResource("audio/gameover.mp3").toString())
	);

	private Set<KeyCode> keysPressed = new HashSet<KeyCode>();


	public Scene createSpriteAnimationGame() {
		pane.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

		pane.getChildren().add(player.getImageView());
		pane.setMinSize(700, 500);

		// Set player starting position to center of pane
		player.getImageView().setTranslateX(pane.getMinWidth()/2 - player.getWidth()/2);
		player.getImageView().setTranslateY(pane.getMinHeight()/2 - player.getHeight()/2);
		player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());

		sprites.add(player);

		// Initialize trees
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
			Rectangle treeCollisionBox = new Rectangle(tree.getXPos() + tree.getCBoxOffsetX(),
					tree.getYPos() + tree.getCBoxOffsetY(), tree.getCBoxWidth(), tree.getCBoxHeight());
			sprites.add(tree);
			obstacles.add(treeCollisionBox);
		}

		// Initialize monsters
		for (int i = 1; i <= 8; i++) {
			Skeleton skeleton = new Skeleton();
			switch (i)
			{
			case 1: skeleton.getImageView().setTranslateX(100); skeleton.getImageView().setTranslateY(150); break;
			case 2: skeleton.getImageView().setTranslateX(300); skeleton.getImageView().setTranslateY(400); break;
			case 3: skeleton.getImageView().setTranslateX(80); skeleton.getImageView().setTranslateY(180); break;
			case 4: skeleton.getImageView().setTranslateX(600); skeleton.getImageView().setTranslateY(340); break;
			case 5: skeleton.getImageView().setTranslateX(200); skeleton.getImageView().setTranslateY(40); break;
			case 6: skeleton.getImageView().setTranslateX(520); skeleton.getImageView().setTranslateY(200); break;
			case 7: skeleton.getImageView().setTranslateX(610); skeleton.getImageView().setTranslateY(0); break;
			case 8: skeleton.getImageView().setTranslateX(400); skeleton.getImageView().setTranslateY(100); break;
			}
			skeleton.setPos(skeleton.getImageView().getTranslateX(), skeleton.getImageView().getTranslateY());
			pane.getChildren().add(skeleton.getImageView());
			sprites.add(skeleton);
			monsters.add(skeleton);
		}

		introMusic.setVolume(0.6);
		introMusic.setCycleCount(AudioClip.INDEFINITE);
		introMusic.play();

		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {

					// Animate the player movement based on velocity set by key presses
					double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
					double deltaX = elapsedSeconds * player.getXvelocity();
					double deltaY = elapsedSeconds * player.getYvelocity();
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

					// Animate the monsters randomly
					for (AnimSprite monster : monsters) {
						double sDeltaX = elapsedSeconds * monster.getXvelocity();
						double sDeltaY = elapsedSeconds * monster.getYvelocity();
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

					checkForMonsterCollision(newX, newY);

				}
				lastUpdateTime.set(timestamp);
			}
		};
		animationTimer.start();

		timeline = new Timeline(
				new KeyFrame(
						Duration.ZERO, e -> {
							for (AnimSprite monster : monsters) {
								int randomDirection = RANDOM.nextInt(4);
								switch(randomDirection) {
								case 0:
									monster.walkDown();
									break;
								case 1:
									monster.walkUp();
									break;
								case 2:
									monster.walkRight();
									break;
								case 3:
									monster.walkLeft();
								}
							}
						}
				),
				new KeyFrame(Duration.seconds(2))
		);
		timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.play();

	    startKeyPressedEventHandler();
		startKeyReleasedEventHandler();

		return scene;

	}

	// Event handler for player movement using arrow keys
	private void startKeyPressedEventHandler() {
		scene.setOnKeyPressed(e -> {
			KeyCode key = e.getCode();
			if (key == KeyCode.UP || key == KeyCode.KP_UP) {
				player.walkUp();
				keysPressed.add(KeyCode.UP);
			}
			else if (key == KeyCode.RIGHT || key == KeyCode.KP_RIGHT) {
				player.walkRight();
				keysPressed.add(KeyCode.RIGHT);
			}
			else if (key == KeyCode.DOWN || key == KeyCode.KP_DOWN) {
				player.walkDown();
				keysPressed.add(KeyCode.DOWN);
			}
			else if (key == KeyCode.LEFT || key == KeyCode.KP_LEFT) {
				player.walkLeft();
				keysPressed.add(KeyCode.LEFT);
			}
		});
	}

	// Event handler for player movement using arrow keys
	private void startKeyReleasedEventHandler() {
		scene.setOnKeyReleased(e -> {
			KeyCode key = e.getCode();
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;

			if (key == KeyCode.UP || key == KeyCode.KP_UP) {
				keysPressed.remove(KeyCode.UP);
				up = true;
			}
			else if (key == KeyCode.RIGHT || key == KeyCode.KP_RIGHT) {
				keysPressed.remove(KeyCode.RIGHT);
				right = true;
			}
			else if (key == KeyCode.DOWN || key == KeyCode.KP_DOWN) {
				keysPressed.remove(KeyCode.DOWN);
				down = true;
			}
			else if (key == KeyCode.LEFT || key == KeyCode.KP_LEFT) {
				keysPressed.remove(KeyCode.LEFT);
				left = true;
			}

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
				if (up) {
					player.standBack();
				}
				else if (right) {
					player.standRight();
				}
				else if (down) {
					player.standFront();
				}
				else if (left) {
					player.standLeft();
				}
			}
		});
	}

	// Change direction if obstacle collision
	private void fixMonsterDirection(AnimSprite monster, double desiredX, double desiredY) {
		if (monster.getXPos() < desiredX) {
			int randomDirection = RANDOM.nextInt(3);
			switch(randomDirection) {
			case 0:
				monster.walkDown();
				break;
			case 1:
				monster.walkUp();
				break;
			case 2:
				monster.walkLeft();
			}
		}
		else if (monster.getXPos() > desiredX) {
			int randomDirection = RANDOM.nextInt(3);
			switch(randomDirection) {
			case 0:
				monster.walkDown();
				break;
			case 1:
				monster.walkUp();
				break;
			case 2:
				monster.walkRight();
				break;
			}
		}
		else if (monster.getYPos() < desiredY) {
			int randomDirection = RANDOM.nextInt(3);
			switch(randomDirection) {
			case 0:
				monster.walkLeft();
				break;
			case 1:
				monster.walkUp();
				break;
			case 2:
				monster.walkRight();
			}
		}
		else if (monster.getYPos() > desiredY) {
			int randomDirection = RANDOM.nextInt(3);
			switch(randomDirection) {
			case 0:
				monster.walkDown();
				break;
			case 1:
				monster.walkLeft();
				break;
			case 2:
				monster.walkRight();
			}
		}
	}

	private boolean checkForObstacleCollision(Sprite sprite, double newX, double newY) {
		// If sprite is a monster check for collisions with other monsters
		if (sprite != player) {
			for (Sprite monster : monsters) {
				if (monster != sprite) {
					monsterCBox.setX(monster.getXPos() + monster.getCBoxOffsetX());
					monsterCBox.setY(monster.getYPos() + monster.getCBoxOffsetY());
					monsterCBox.setWidth(monster.getCBoxWidth());
					monsterCBox.setHeight(monster.getCBoxHeight());
					if (monsterCBox.getBoundsInLocal().intersects(
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
		for (Rectangle obstacle : obstacles) {
			if (obstacle.getBoundsInLocal().intersects(
					newX + sprite.getCBoxOffsetX(), newY + sprite.getCBoxOffsetY(),
					sprite.getCBoxWidth(), sprite.getCBoxHeight())) {
				return true;
			}
		}
		return false;
	}

	// Check if player has collided with a monster and if so call game over
	private void checkForMonsterCollision(double newX, double newY) {
		for (Sprite monster : monsters) {
			monsterCBox.setX(monster.getXPos() + monster.getCBoxOffsetX());
			monsterCBox.setY(monster.getYPos() + monster.getCBoxOffsetY());
			monsterCBox.setWidth(monster.getCBoxWidth());
			monsterCBox.setHeight(monster.getCBoxHeight());
			if (monsterCBox.getBoundsInLocal().intersects(
					newX + player.getCBoxOffsetX(), newY + player.getCBoxOffsetY(),
					player.getCBoxWidth(), player.getCBoxHeight())) {
				gameOver();
				break;
			}
		}
	}

	// Change the z-order of sprites on the pane based on xPos of collision box
	private void reorderNodes() {
		Collections.sort(sprites);
		for (Sprite sprite : sprites) {
			sprite.getImageView().toFront();
		}
	}

	private VBox createGameOverPane() {
		VBox vbox = new VBox();
		Text gameOverText = new Text("game over");
		gameOverText.setFill(Color.WHITE);
		gameOverText.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/MonsterFriendFore.otf"), 50));
		Text restartText = new Text("[Press 2 or ENTER to restart]");
		restartText.setFill(Color.BLACK);
		Timeline restartTextDelay = new Timeline(
				new KeyFrame(Duration.seconds(3), e -> restartText.setFill(Color.LIGHTGRAY))
		);
		restartTextDelay.play();
		restartText.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/DTM-Mono.otf"), 20));
		vbox.getChildren().addAll(gameOverText, restartText);
		vbox.setSpacing(50);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefSize(pane.getBoundsInParent().getWidth(), pane.getBoundsInParent().getHeight());
		vbox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		return vbox;
	}

	private void gameOver() {
		AudioClip soundEffect = new AudioClip(getClass().getResource("audio/gameover.wav").toString());
		soundEffect.play();
		animationTimer.stop();
		timeline.stop();
		introMusic.stop();

		gameOverMusic.setVolume(0.5);
		Timeline delayedMusic = new Timeline(new KeyFrame(Duration.seconds(3), e -> gameOverMusic.play()));
		delayedMusic.play();

		System.out.println("GAME OVER");
		VBox gameOverMessage = createGameOverPane();
		pane.getChildren().add(gameOverMessage);
		gameOverMessage.prefWidthProperty().bind(pane.widthProperty());
		gameOverMessage.prefHeightProperty().bind(pane.heightProperty());

		Timeline delayRestart = new Timeline(new KeyFrame(Duration.millis(3100), event -> {
			scene.setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DIGIT2) {
					pane.getChildren().remove(gameOverMessage);
					restart();
				}
			});
		}));
		delayRestart.play();
	}

	private void restart() {
		keysPressed.clear();
		introMusic.play();
		gameOverMusic.stop();

		// Reset player position to center of pane
		player.standFront();
		player.getImageView().setTranslateX(pane.getMinWidth()/2 - player.getWidth()/2);
		player.getImageView().setTranslateY(pane.getMinHeight()/2 - player.getHeight()/2);
		player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());

		// Reset monster positions
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
			}
			monster.setPos(monster.getImageView().getTranslateX(), monster.getImageView().getTranslateY());
			i++;
		}

		// Restart animations
		animationTimer.start();
		timeline.play();
		startKeyPressedEventHandler();
	}

}
