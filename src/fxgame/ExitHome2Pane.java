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

	private static Brinn player;
	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();

	static {
		player = Game.getPlayer();
		sprites.add(player);

		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		Sprite sign = new Sprite("fxgame/images/sign.png", 40, 40);
		sign.setCBox(0, 12, sign.getWidth(), 22);
		sign.setPos(324, 174);
		sign.getImageView().setX(sign.getXPos());
		sign.getImageView().setY(sign.getYPos());
		sprites.add(sign);

		Sprite trees = new Sprite("fxgame/images/exithome2trees.png", 234, 200);
		trees.setCBox(0, 30, trees.getWidth(), 170);
		trees.setPos(406, 280);
		trees.getImageView().setX(trees.getXPos());
		trees.getImageView().setY(trees.getYPos());
		sprites.add(trees);

		pane.getChildren().addAll(sign.getImageView(), trees.getImageView());

		obstacles.add(new Rectangle2D(0, 0, 312, 480));		// left trees
		obstacles.add(new Rectangle2D(256, 0, 384, 194));	// top trees

		exits.put(KeyCode.RIGHT, GameState.EXIT_HOME);
	}

	public static Pane getPane() {
		player.setXPos(604);
		player.getImageView().setTranslateX(player.getXPos());
		player.getImageView().setTranslateY(player.getYPos());
		pane.getChildren().add(player.getImageView());

		Game.getPlayerController().setVals(sprites, obstacles, exits);

		return pane;
	}

}
