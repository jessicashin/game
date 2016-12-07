package fxgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fxgame.Game.GameState;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class RightCliffPane {

	private static final Random RANDOM = new Random();

	private static final Pane pane = new Pane();
	private static final Image bgImage = new Image("fxgame/images/rightcliff.png");

	private static final Brinn player = Game.getPlayer();
	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<AnimatedSprite> monsters = new ArrayList<AnimatedSprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final List<InteractionBox> interactions = new ArrayList<InteractionBox>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();

	private static final Timeline monstersTimeline = new Timeline(
		new KeyFrame(
				Duration.ZERO, e -> {
					for (AnimatedSprite monster : monsters) {
						int randomDirection = RANDOM.nextInt(4);
						switch(randomDirection) {
							case 0: monster.walkDown(); break;
							case 1: monster.walkUp(); break;
							case 2: monster.walkRight(); break;
							case 3: monster.walkLeft();
						}
					}
				}
		),
		new KeyFrame(Duration.seconds(2))
	);

	static {
		sprites.add(player);

		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		monstersTimeline.setCycleCount(Timeline.INDEFINITE);
		monstersTimeline.play();
		createMonsters();

		Sprite sign = new Sign();
		sign.setPos(126, 252);
		sprites.add(sign);

		InteractionBox readSign = new InteractionBox(
			new Rectangle2D(136, 292, 20, 2), KeyCode.UP,
			"World expansion coming soon!"
		);
		interactions.add(readSign);

		Sprite trees = new Sprite("fxgame/images/rightclifftrees.png", 640, 120);
		trees.setCBox(0, 52, trees.getWidth(), trees.getHeight() - 52);
		trees.setPos(0, 360);
		sprites.add(trees);

		pane.getChildren().addAll(sign.getImageView(), trees.getImageView());

		// Top left trees
		obstacles.add(new Rectangle2D(0, 0, 91, 188));
		obstacles.add(new Rectangle2D(0, 188, 57, 38));

		// Top behind cliff
		obstacles.add(new Rectangle2D(96, 0, 18, 80));
		obstacles.add(new Rectangle2D(114, 0, 16, 92));
		obstacles.add(new Rectangle2D(120, 0, 510, 102));

		// Right side of cliff
		obstacles.add(new Rectangle2D(548, 110, 10, 6));
		obstacles.add(new Rectangle2D(562, 116, 86, 16));
		obstacles.add(new Rectangle2D(572, 132, 76, 8));
		obstacles.add(new Rectangle2D(582, 140, 66, 22));
		obstacles.add(new Rectangle2D(610, 0, 40, 480));

		// Bottom edge of cliff
		obstacles.add(new Rectangle2D(568, 358, 8, 122));
		obstacles.add(new Rectangle2D(576, 352, 12, 128));
		obstacles.add(new Rectangle2D(588, 340, 8, 140));
		obstacles.add(new Rectangle2D(596, 334, 10, 146));
		obstacles.add(new Rectangle2D(606, 324, 8, 156));

		exits.put(KeyCode.LEFT, GameState.SKELETONS);
	}

	public static Pane getPane() {
		pane.setOpacity(0);
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);

		player.setXPos(-Controller.OFFSCREEN_X);

		for (Sprite monster : monsters) {
			if (!pane.getChildren().contains(monster.getImageView()))
				pane.getChildren().add(monster.getImageView());
		}
		initMonsterPos();

		pane.getChildren().add(player.getImageView());

		Game.getPlayerController().setVals(pane, sprites, monsters, obstacles, interactions, exits);
		Game.setCurrentState(GameState.RIGHT_CLIFF);

		fadeTransition.play();

		return pane;
	}

	// Initialize monsters and set their initial positions on the pane
	private static void createMonsters() {
		for (int i = 1; i <= 2; i++) {
			Skeleton skeleton = new Skeleton();
			pane.getChildren().add(skeleton.getImageView());
			sprites.add(skeleton);
			monsters.add(skeleton);
		}
		initMonsterPos();
	}

	// Set initial positions on the pane for each monster
	private static void initMonsterPos() {
		int i = 1;
		for (AnimatedSprite monster : monsters) {
			switch (i)
			{
			case 1: monster.setPos(280, 180); break;
			case 2: monster.setPos(470, 220); break;
			}
			i++;
		}
	}
}
