package fxgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxgame.Game.GameState;
import javafx.animation.FadeTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class HomePane {

	private static final Pane pane = new Pane();
	private static final Image bgImage = new Image("fxgame/images/home.png");

	private static final MediaPlayer music = new MediaPlayer(
		new Media(LabEight.class.getResource("audio/intro.mp3").toString())
	);

	private static Brinn player;
	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<AnimatedSprite> monsters = new ArrayList<AnimatedSprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final List<InteractionBox> interactions = new ArrayList<InteractionBox>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();

	static {
		player = Game.getPlayer();
		sprites.add(player);

		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		obstacles.add(new Rectangle2D(368, 320, 272, 160));		// right trees
		obstacles.add(new Rectangle2D(0, 320, 226, 160));		// left trees
		obstacles.add(new Rectangle2D(195, 267, 200, 100));		// igloo

		exits.put(KeyCode.DOWN, GameState.EXIT_HOME);

		music.setVolume(0.6);
		music.setCycleCount(AudioClip.INDEFINITE);
	}

	public static Pane getPane() {
		pane.setOpacity(0);
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);

		if (Game.getCurrentState() == GameState.ROOM)
			player.setPos(272, 336); // igloo door
		else player.setYPos(Game.WINDOW_HEIGHT - Controller.OFFSCREEN_Y);

		pane.getChildren().add(player.getImageView());

		Game.getPlayerController().setVals(pane, sprites, monsters, obstacles, interactions, exits);
		Game.setCurrentState(GameState.HOME);

		music.play();
		fadeTransition.play();

		return pane;
	}

	public static void stopMusic() {
		music.stop();
	}

}
