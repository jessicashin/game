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

public class HomePane {

	private static final Pane pane = new Pane();

	private static final Image bgImage = new Image("fxgame/images/home.png");

	private static final MediaPlayer music = new MediaPlayer(
		new Media(LabEight.class.getResource("audio/intro.mp3").toString())
	);

	private static Brinn player;
	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();

	static {
		player = Game.getPlayer();
		sprites.add(player);

		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		obstacles.add(new Rectangle2D(366, 320, 274, 160));		// right trees
		obstacles.add(new Rectangle2D(0, 320, 234, 160));		// left trees
		obstacles.add(new Rectangle2D(195, 267, 200, 100));		// igloo

		music.setVolume(0.6);
		music.setCycleCount(AudioClip.INDEFINITE);
	}

	public static Pane getPane() {
		player.setPos(271, 337);
		player.getImageView().setTranslateX(player.getXPos());
		player.getImageView().setTranslateY(player.getYPos());
		pane.getChildren().add(player.getImageView());

		Game.getPlayerController().setVals(sprites, obstacles, exits);

		music.play();
		return pane;
	}

	public static void stopMusic() {
		music.stop();
	}

}