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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ExitHomePane {

	private static final Pane pane = new Pane();
	private static final Image bgImage = new Image("fxgame/images/exithome.png");

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
		sign.setPos(217, 183);
		sprites.add(sign);

		Text signText = new Text("THIS IS YOUR FINAL\nWARNING. BEWARE OF DOG");
		signText.setFont(Font.loadFont(ExitHome2Pane.class.getResourceAsStream("fonts/Papyrus.ttf"), 28));
		InteractionBox readSign = new InteractionBox(
			new Rectangle2D(227, 223, 20, 2), KeyCode.UP, signText
		);
		interactions.add(readSign);

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
		if (Game.getCurrentState() == GameState.HOME) player.setYPos(-Controller.OFFSCREEN_Y);
		else player.setXPos(-Controller.OFFSCREEN_X);

		pane.getChildren().add(player.getImageView());

		Game.getPlayerController().setVals(sprites, monsters, obstacles, interactions, exits);
		Game.setCurrentState(GameState.EXIT_HOME);

		return pane;
	}

	public static void addModalPane(Pane modalPane) {
		pane.getChildren().add(modalPane);
	}

	public static void removeModalPane(Pane modalPane) {
		pane.getChildren().remove(modalPane);
	}

}
