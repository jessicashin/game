package fxgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxgame.Game.GameState;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class RoomPane {

	private static final Pane pane = new Pane();

	private static final Image bgImage = new Image("fxgame/images/room.png");

	private static final MediaPlayer music = new MediaPlayer(
		new Media(LabEight.class.getResource("audio/home.mp3").toString())
	);

	private static Brinn player;
	private static final Luffy dog = new Luffy();

	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();

	static {
		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		music.setVolume(0.6);
		music.setCycleCount(AudioClip.INDEFINITE);

		player = Game.getPlayer();
		player.getImageView().setTranslateX(Game.WINDOW_WIDTH/2 - player.getWidth()/2);
		player.getImageView().setTranslateY(Game.WINDOW_HEIGHT/2 - player.getHeight()/2);
		player.setPos(player.getImageView().getTranslateX(), player.getImageView().getTranslateY());
		pane.getChildren().add(player.getImageView());

		sprites.add(player);
		sprites.add(dog);

		Sprite table = new Sprite("fxgame/images/table.png", 70, 116);
		table.setCBox(0, 14, table.getWidth()-3, 84);
		table.setPos(131, 270);
		table.getImageView().setX(table.getXPos());
		table.getImageView().setY(table.getYPos());

		Sprite bedTop = new Sprite("fxgame/images/bedtop.png", 72, 42);
		bedTop.setCBox(4, 14, 64-3, 26);
		bedTop.setPos(131, 133);
		bedTop.getImageView().setX(bedTop.getXPos());
		bedTop.getImageView().setY(bedTop.getYPos());

		Sprite bedBottom = new Sprite("fxgame/images/bedbottom.png", 72, 18);
		bedBottom.setCBox(4, 0, 64-3, 10);
		bedBottom.setPos(131, 223);
		bedBottom.getImageView().setX(bedBottom.getXPos());
		bedBottom.getImageView().setY(bedBottom.getYPos());

		Sprite leftWall = new Sprite("fxgame/images/roomleftwall.png", 281, 60);
		leftWall.setCBox(0, 5, leftWall.getWidth(), leftWall.getHeight()-5);
		leftWall.setPos(0, 420);
		leftWall.getImageView().setX(leftWall.getXPos());
		leftWall.getImageView().setY(leftWall.getYPos());

		Sprite rightWall = new Sprite("fxgame/images/roomrightwall.png", 281, 60);
		rightWall.setCBox(0, 5, rightWall.getWidth(), rightWall.getHeight()-5);
		rightWall.setPos(359, 420);
		rightWall.getImageView().setX(rightWall.getXPos());
		rightWall.getImageView().setY(rightWall.getYPos());

		sprites.add(table);
		sprites.add(bedTop);
		sprites.add(bedBottom);
		sprites.add(leftWall);
		sprites.add(rightWall);

		obstacles.add(new Rectangle2D(0, 0, 640, 104));		// top wall
		obstacles.add(new Rectangle2D(519, 0, 121, 480));	// right wall
		obstacles.add(new Rectangle2D(0, 0, 121, 480));		// left wall
		obstacles.add(new Rectangle2D(284, 46, 120, 110));	// bookshelf
		obstacles.add(new Rectangle2D(409, 96, 108, 52));	// drawers

		pane.getChildren().addAll(
			table.getImageView(), bedTop.getImageView(), bedBottom.getImageView(),
			leftWall.getImageView(), rightWall.getImageView(), dog.getImageView()
		);

		exits.put(KeyCode.DOWN, GameState.HOME);
	}

	public static Pane getPane() {
		if (player.getXPos() != Game.WINDOW_WIDTH/2 - player.getWidth()/2
				&& player.getYPos() != Game.WINDOW_HEIGHT/2 - player.getHeight()/2) {
			player.setPos(302, 430);
			player.getImageView().setTranslateX(player.getXPos());
			player.getImageView().setTranslateY(player.getYPos());
			pane.getChildren().add(player.getImageView());
		}

		dog.setPos(423, 320);
		dog.getImageView().setTranslateX(dog.getXPos());
		dog.getImageView().setTranslateY(dog.getYPos());
		dog.layLeft();

		Game.getPlayerController().setVals(sprites, obstacles, exits);

		music.play();

		return pane;
	}

	public static void stopMusic() {
		music.stop();
	}

}
