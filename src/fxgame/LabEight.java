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
	private List<Rectangle> obstacles = new ArrayList<Rectangle>();
	private List<Sprite> sprites = new ArrayList<Sprite>();

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
		player.getImageView().setTranslateX(pane.getMinWidth()/2 - player.getWidth()/2);
		player.getImageView().setTranslateY(pane.getMinHeight()/2 - player.getHeight()/2);
		player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());

		Tree tree1 = new Tree();
		pane.getChildren().add(tree1.getImageView());
		tree1.getImageView().setX(200);
		tree1.getImageView().setY(150);
		tree1.setPos(tree1.getImageView().getX(), tree1.getImageView().getY());
		Rectangle tree1CollisionBox = new Rectangle(tree1.getImageView().getX() + tree1.getCBoxOffsetX(),
				tree1.getImageView().getY() + tree1.getCBoxOffsetY(), tree1.getCBoxWidth(), tree1.getCBoxHeight());

		Tree tree2 = new Tree();
		pane.getChildren().add(tree2.getImageView());
		tree2.getImageView().setX(400);
		tree2.getImageView().setY(200);
		tree2.setPos(tree2.getImageView().getX(), tree2.getImageView().getY());
		Rectangle tree2CollisionBox = new Rectangle(tree2.getImageView().getX() + tree2.getCBoxOffsetX(),
				tree2.getImageView().getY() + tree2.getCBoxOffsetY(), tree2.getCBoxWidth(), tree2.getCBoxHeight());

		Skeleton skeleton = new Skeleton();
		pane.getChildren().add(skeleton.getImageView());
		skeleton.getImageView().setTranslateX(100);
		skeleton.getImageView().setTranslateY(150);
		skeleton.setPos(skeleton.getImageView().getTranslateX(), skeleton.getImageView().getTranslateY());
		Rectangle skeletonCBox = new Rectangle(skeleton.getImageView().getTranslateX() + skeleton.getCBoxOffsetX(),
				skeleton.getImageView().getTranslateY() + skeleton.getCBoxOffsetY(),
				skeleton.getCBoxWidth(), skeleton.getCBoxHeight());
		skeleton.walkRight();

		sprites.add(player);
		sprites.add(tree1);
		sprites.add(tree2);
		sprites.add(skeleton);
		obstacles.add(tree1CollisionBox);
		obstacles.add(tree2CollisionBox);

		introMusic.setVolume(0.6);
		introMusic.setCycleCount(AudioClip.INDEFINITE);
		introMusic.play();

		animationTimer = new AnimationTimer() {
			  @Override
			  public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {

				  double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
				  double deltaX = elapsedSeconds * player.getXvelocity();
				  double deltaY = elapsedSeconds * player.getYvelocity();
				  double oldX = player.getImageView().getTranslateX();
				  double newX = Math.max(0, Math.min(pane.getWidth() - player.getWidth(), oldX + deltaX));
				  double oldY = player.getImageView().getTranslateY();
				  double newY = Math.max(0, Math.min(pane.getHeight() - player.getHeight(), oldY + deltaY));
				  boolean collision = checkForCollision(player, newX, newY);
				  if (!collision) {
					  	player.getImageView().setTranslateX(newX);
					  	player.getImageView().setTranslateY(newY);
					  	player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());
					  	reorderNodes();
				  }

				  double sDeltaX = elapsedSeconds * skeleton.getXvelocity();
				  double sDeltaY = elapsedSeconds * skeleton.getYvelocity();
				  double sOldX = skeleton.getImageView().getTranslateX();
				  double sNewX = Math.max(0, Math.min(pane.getWidth() - skeleton.getWidth(), sOldX + sDeltaX));
				  double sOldY = skeleton.getImageView().getTranslateY();
				  double sNewY = Math.max(0, Math.min(pane.getHeight() - skeleton.getHeight(), sOldY + sDeltaY));
				  boolean sCollision = checkForCollision(skeleton, sNewX,sNewY);
				  if (!sCollision) {
					  skeleton.getImageView().setTranslateX(sNewX);
					  skeleton.getImageView().setTranslateY(sNewY);
					  skeleton.setPos(skeleton.getImageView().getTranslateX(), skeleton.getImageView().getTranslateY());
					  skeletonCBox.setX(skeleton.getXPos() + skeleton.getCBoxOffsetX());
					  skeletonCBox.setY(skeleton.getYPos() + skeleton.getCBoxOffsetY());
					  reorderNodes();
				  }

				  if (checkForSkeletonCollision(skeletonCBox, newX, newY)) {
					  skeleton.getAnimation().stop();
					  VBox gameOverMessage = createGameOverPane();
					  pane.getChildren().add(gameOverMessage);
					  Timeline delayRestart = new Timeline(new KeyFrame(Duration.millis(3100), event -> {
						  scene.setOnKeyPressed(e -> {
							  if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DIGIT2) {
								  pane.getChildren().remove(gameOverMessage);
								  skeleton.getImageView().setTranslateX(100);
								  skeleton.getImageView().setTranslateY(150);
								  skeleton.setPos(skeleton.getImageView().getTranslateX(), skeleton.getImageView().getTranslateY());
								  restart();
							  }
						  });
					  }));
					  delayRestart.play();
				  }

				  if (skeleton.getImageView().getTranslateX() < sOldX + sDeltaX) {
					  int randomDirection = RANDOM.nextInt(3);
					  switch(randomDirection) {
					  case 0:
						  skeleton.walkDown();
						  break;
					  case 1:
						  skeleton.walkUp();
						  break;
					  case 2:
						  skeleton.walkLeft();
					  }
				  }
				  else if (skeleton.getImageView().getTranslateX() > sOldX + sDeltaX) {
					  int randomDirection = RANDOM.nextInt(3);
					  switch(randomDirection) {
					  case 0:
						  skeleton.walkDown();
						  break;
					  case 1:
						  skeleton.walkUp();
						  break;
					  case 2:
						  skeleton.walkRight();
						  break;
					  }
				  }
				  else if (skeleton.getImageView().getTranslateY() < sOldY + sDeltaY) {
					  int randomDirection = RANDOM.nextInt(3);
					  switch(randomDirection) {
					  case 0:
						  skeleton.walkLeft();
						  break;
					  case 1:
						  skeleton.walkUp();
						  break;
					  case 2:
						  skeleton.walkRight();
					  }
				  }
				  else if (skeleton.getImageView().getTranslateY() > sOldY + sDeltaY) {
					  int randomDirection = RANDOM.nextInt(3);
					  switch(randomDirection) {
					  case 0:
						  skeleton.walkDown();
						  break;
					  case 1:
						  skeleton.walkLeft();
						  break;
					  case 2:
						  skeleton.walkRight();
					  }
				  }

				}
				lastUpdateTime.set(timestamp);
			  }
		};
		animationTimer.start();

		timeline = new Timeline(
		        new KeyFrame(
		          Duration.ZERO, e -> {
						  int randomDirection = RANDOM.nextInt(4);
						  switch(randomDirection) {
						  case 0:
							  skeleton.walkDown();
							  break;
						  case 1:
							  skeleton.walkUp();
							  break;
						  case 2:
							  skeleton.walkRight();
							  break;
						  case 3:
							  skeleton.walkLeft();
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

			if (keysPressed.isEmpty()) {
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

	private boolean checkForCollision(Sprite sprite, double newX, double newY) {
		boolean collision = false;
		for (Rectangle obstacle : obstacles) {
			if (obstacle.getBoundsInLocal().intersects(
					newX + sprite.getCBoxOffsetX(), newY + sprite.getCBoxOffsetY(),
					sprite.getCBoxWidth(), sprite.getCBoxHeight())) {
				collision = true;
				break;
			}
		}
		return collision;
	}

	private boolean checkForSkeletonCollision(Rectangle skeletonCBox, double newX, double newY) {
		boolean collision = false;
		if (skeletonCBox.getBoundsInLocal().intersects(
				newX + player.getCBoxOffsetX(), newY + player.getCBoxOffsetY(),
				player.getCBoxWidth(), player.getCBoxHeight())) {
			collision = true;
			AudioClip soundEffect = new AudioClip(getClass().getResource("audio/gameover.wav").toString());
			soundEffect.play();
			animationTimer.stop();
			timeline.stop();
			introMusic.stop();
			gameOverMusic.setVolume(0.5);
			Timeline delayedMusic = new Timeline(new KeyFrame(Duration.seconds(3), e -> gameOverMusic.play()));
			delayedMusic.play();
			System.out.println("GAME OVER");
		}
		return collision;
	}

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

	private void restart() {
		keysPressed.clear();
		animationTimer.start();
		introMusic.play();
		timeline.play();
		gameOverMusic.stop();
		player.standFront();
		player.getImageView().setTranslateX(pane.getMinWidth()/2 - player.getWidth()/2);
		player.getImageView().setTranslateY(pane.getMinHeight()/2 - player.getHeight()/2);
		player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());
		startKeyPressedEventHandler();
	}

}
