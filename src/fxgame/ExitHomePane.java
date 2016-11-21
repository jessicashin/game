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

public class ExitHomePane {

	private static final Pane pane = new Pane();
	private static final Image bgImage = new Image("fxgame/images/exithome.png");

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
		sign.setPos(217, 183);
		sprites.add(sign);

		Sprite trees = new Sprite("fxgame/images/exithometrees.png", 479, 200);
		trees.setCBox(0, 52, trees.getWidth(), trees.getHeight() - 52);
		trees.setPos(0, 280);
		sprites.add(trees);

		pane.getChildren().addAll(sign.getImageView(), trees.getImageView());

		obstacles.add(new Rectangle2D(0, 0, 226, 194));		// left trees
		obstacles.add(new Rectangle2D(368, 0, 272, 330));	// right trees

		exits.put(KeyCode.UP, GameState.HOME);
		exits.put(KeyCode.LEFT, GameState.EXIT_HOME2);
	}

	public static Pane getPane() {
		// If coming from home, position player at top of pane
		if (player.getXPos() < Game.WINDOW_WIDTH - player.getWidth())
			player.setYPos(-PlayerController.OFFSCREEN_Y);
		// If coming from exit home 2, position player at left
		else
			player.setXPos(-PlayerController.OFFSCREEN_X);

		pane.getChildren().add(player.getImageView());

		Game.getPlayerController().setVals(sprites, monsters, obstacles, exits);

		return pane;
	}

}
