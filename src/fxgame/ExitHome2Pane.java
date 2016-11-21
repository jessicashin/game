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

public class ExitHome2Pane {

	private static final Pane pane = new Pane();

	private static final Image bgImage = new Image("fxgame/images/exithome2.png");

	private static final Brinn player = Game.getPlayer();
	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<AnimSprite> monsters = new ArrayList<AnimSprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();

	static {
		sprites.add(player);

		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		Sprite sign = new Sign();
		sign.setPos(378, 174);
		sprites.add(sign);

		Sprite trees = new Sprite("fxgame/images/exithome2trees.png", 180, 200);
		trees.setCBox(4, 52, trees.getWidth() - 4, trees.getHeight() - 52);
		trees.setPos(460, 280);
		sprites.add(trees);

		pane.getChildren().addAll(sign.getImageView(), trees.getImageView());

		obstacles.add(new Rectangle2D(0, 0, 366, 480));		// left trees
		obstacles.add(new Rectangle2D(256, 0, 384, 194));	// top trees

		exits.put(KeyCode.RIGHT, GameState.EXIT_HOME);
		exits.put(KeyCode.DOWN, GameState.FORKED_PATH);
	}

	public static Pane getPane() {
		// If coming from exit home, position player at right
		if (player.getXPos() < 0) { player.setXPos(604); }
		// If coming from forked path, position player at bottom
		else {
			if (player.getXPos() <= 366)
				player.setXPos(366);
			else if (player.getXPos() + player.getWidth() >= 464)
				player.setXPos(464 - player.getWidth());
			player.setYPos(Game.WINDOW_HEIGHT - PlayerController.OFFSCREEN_Y);
		}

		pane.getChildren().add(player.getImageView());

		Game.getPlayerController().setVals(sprites, monsters, obstacles, exits);

		return pane;
	}

}
