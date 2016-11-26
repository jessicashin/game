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

	private static final Brinn player = Game.getPlayer();
	private static final Luffy dog = Game.getDog();

	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<AnimatedSprite> monsters = new ArrayList<AnimatedSprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();
	private static final List<InteractionBox> interactions = new ArrayList<InteractionBox>();

	static {
		pane.setMinSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setMaxSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		music.setVolume(0.6);
		music.setCycleCount(AudioClip.INDEFINITE);

		player.setPos(Game.WINDOW_WIDTH/2 - player.getWidth()/2, Game.WINDOW_HEIGHT/2 - player.getHeight()/2);

		sprites.add(player);
		sprites.add(dog);

		Sprite table = new Sprite("fxgame/images/table.png", 70, 116);
		table.setCBox(0, 14, table.getWidth()-3, 84);
		table.setPos(131, 270);

		Sprite bedTop = new Sprite("fxgame/images/bedtop.png", 72, 42);
		bedTop.setCBox(4, 14, 64-3, 24);
		bedTop.setPos(131, 133);

		Sprite bedBottom = new Sprite("fxgame/images/bedbottom.png", 72, 18);
		bedBottom.setCBox(4, 1, 64-3, 9);
		bedBottom.setPos(131, 223);

		Sprite leftWall = new Sprite("fxgame/images/roomleftwall.png", 281, 60);
		leftWall.setCBox(0, 5, leftWall.getWidth(), leftWall.getHeight()-5);
		leftWall.setPos(0, 420);

		Sprite rightWall = new Sprite("fxgame/images/roomrightwall.png", 281, 60);
		rightWall.setCBox(0, 5, rightWall.getWidth(), rightWall.getHeight()-5);
		rightWall.setPos(359, 420);

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

		InteractionBox bookshelf = new InteractionBox(
			new Rectangle2D(300, 166, 90, 1), KeyCode.UP,
			"You have many books. They are\nwell worn and loved."
		);
		InteractionBox drawers = new InteractionBox(
			new Rectangle2D(424, 158, 78, 1), KeyCode.UP,
			"Your drawers are empty!"
		);
		interactions.add(bookshelf);
		interactions.add(drawers);

		exits.put(KeyCode.DOWN, GameState.HOME);
	}

	public static Pane getPane() {
		if (Game.getCurrentState() == GameState.TITLE || Game.getCurrentState() == GameState.GAME_OVER) {
			player.setPos(Game.WINDOW_WIDTH/2 - player.getWidth()/2, Game.WINDOW_HEIGHT/2 - player.getHeight()/2);
			player.standFront();
		}

		else player.setPos(302, Game.WINDOW_HEIGHT - Controller.OFFSCREEN_Y);

		pane.getChildren().add(player.getImageView());

		// Position Luffy on dog bed
		dog.setPos(423, 320);
		dog.layLeft();

		Game.getPlayerController().setVals(sprites, monsters, obstacles, interactions, exits);
		Game.setCurrentState(GameState.ROOM);

		music.play();

		return pane;
	}

	public static void addModalPane(Pane modalPane) {
		pane.getChildren().add(modalPane);
	}

	public static void removeModalPane(Pane modalPane) {
		pane.getChildren().remove(modalPane);
	}

	public static void stopMusic() {
		music.stop();
	}

}
