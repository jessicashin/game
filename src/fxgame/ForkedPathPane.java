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
import javafx.util.Duration;

public class ForkedPathPane {

	private static final Pane pane = new Pane();
	private static final Image bgImage = new Image("fxgame/images/forkedpath.png");

	private static final Brinn player = Game.getPlayer();
	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<AnimatedSprite> monsters = new ArrayList<AnimatedSprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final List<InteractionBox> interactions = new ArrayList<InteractionBox>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();

	static {
		sprites.add(player);

		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		Sprite sign = new Sign();
		sign.setPos(510, 190);
		sprites.add(sign);

		InteractionBox readSign = new InteractionBox(
			new Rectangle2D(520, 230, 20, 2), KeyCode.UP,
			"Remember! Press [C] or [SPACE]\nto fight monsters."
		);
		interactions.add(readSign);

		Sprite bottomTrees = new Sprite("fxgame/images/forkedpathtrees.png", 640, 200);
		bottomTrees.setCBox(0, 172, bottomTrees.getWidth(), bottomTrees.getHeight() - 172);
		bottomTrees.setPos(0, 280);
		sprites.add(bottomTrees);

		pane.getChildren().addAll(sign.getImageView(), bottomTrees.getImageView());

		// Left trees
		obstacles.add(new Rectangle2D(0, 0, 350, 108));
		obstacles.add(new Rectangle2D(0, 108, 310, 40));
		obstacles.add(new Rectangle2D(0, 148, 270, 40));
		obstacles.add(new Rectangle2D(0, 188, 230, 40));
		obstacles.add(new Rectangle2D(0, 228, 190, 40));
		obstacles.add(new Rectangle2D(0, 268, 150, 44));

		// Right trees
		obstacles.add(new Rectangle2D(490, 0, 150, 148));
		obstacles.add(new Rectangle2D(532, 148, 108, 40));
		obstacles.add(new Rectangle2D(572, 188, 68, 40));
		obstacles.add(new Rectangle2D(612, 228, 28, 40));

		// Bottom trees
		obstacles.add(new Rectangle2D(308, 412, 220, 40));
		obstacles.add(new Rectangle2D(348, 372, 140, 40));
		obstacles.add(new Rectangle2D(388, 332, 60, 40));

		exits.put(KeyCode.UP, GameState.EXIT_HOME2);
		exits.put(KeyCode.RIGHT, GameState.SKELETONS);
		exits.put(KeyCode.LEFT, GameState.SNOWMANS);

	}

	public static Pane getPane() {
		pane.setOpacity(0);
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);

		if (Game.getCurrentState() == GameState.EXIT_HOME2)
			player.setYPos(-Controller.OFFSCREEN_Y);
		else if (Game.getCurrentState() == GameState.SKELETONS)
			player.setXPos(Game.WINDOW_WIDTH - Controller.OFFSCREEN_X);
		else if (Game.getCurrentState() == GameState.SNOWMANS)
			player.setXPos(-Controller.OFFSCREEN_X);

		pane.getChildren().add(player.getImageView());

		Game.getPlayerController().setVals(pane, sprites, monsters, obstacles, interactions, exits);
		Game.setCurrentState(GameState.FORKED_PATH);

		fadeTransition.play();

		return pane;
	}

}
