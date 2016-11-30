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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SkeletonsPane {

	private static final Random RANDOM = new Random();

	private static final Pane pane = new Pane();
	private static final Image bgImage = new Image("fxgame/images/skeletonspane.png");

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

		monstersTimeline.setCycleCount(Timeline.INDEFINITE);
		monstersTimeline.play();

		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		createMonsters();

		// Bottom trees
		Sprite trees = new Sprite("fxgame/images/skeletonsptrees.png", 640, 120);
		trees.setCBox(0, 92, trees.getWidth(), trees.getHeight() - 92);
		trees.setPos(0, 360);
		sprites.add(trees);

		pane.getChildren().add(trees.getImageView());

		// Wooden boards
		obstacles.add(new Rectangle2D(448, 82, 80, 50));

		// Top trees
		obstacles.add(new Rectangle2D(0, 0, Game.WINDOW_WIDTH, 108));
		obstacles.add(new Rectangle2D(0, 108, 392, 44));
		obstacles.add(new Rectangle2D(0, 152, 352, 40));
		obstacles.add(new Rectangle2D(0, 192, 312, 40));
		obstacles.add(new Rectangle2D(0, 232, 272, 40));
		obstacles.add(new Rectangle2D(132, 272, 102, 40));
		obstacles.add(new Rectangle2D(536, 108, 102, 40));
		obstacles.add(new Rectangle2D(574, 148, 66, 40));
		obstacles.add(new Rectangle2D(614, 188, 26, 38));

		// Bottom trees
		obstacles.add(new Rectangle2D(138, 412, 76, 40));
		obstacles.add(new Rectangle2D(538, 412, 102, 40));

		exits.put(KeyCode.LEFT, GameState.FORKED_PATH);
	}

	public static Pane getPane() {
		pane.setOpacity(0);
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);

		if (Game.getCurrentState() == GameState.FORKED_PATH)
			player.setXPos(-Controller.OFFSCREEN_X);

		pane.getChildren().add(player.getImageView());

		for (ImageView image : Game.getPlayerPunch().getAllImages()) {
			pane.getChildren().remove(image);
		}

		for (Sprite monster : monsters) {
			if (!pane.getChildren().contains(monster.getImageView()))
				pane.getChildren().add(monster.getImageView());
		}

		initMonsterPos();

		Game.getPlayerController().setVals(pane, sprites, monsters, obstacles, interactions, exits);
		Game.setCurrentState(GameState.SKELETONS);

		fadeTransition.play();

		return pane;
	}

	// Initialize monsters and set their initial positions on the pane
	private static void createMonsters() {
		for (int i = 1; i <= 5; i++) {
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
				case 1: monster.setPos(280, 300); break;
				case 2: monster.setPos(384, 250); break;
				case 3: monster.setPos(408, 154); break;
				case 4: monster.setPos(468, 330); break;
				case 5: monster.setPos(502, 210); break;
			}
			i++;
		}
	}

}
