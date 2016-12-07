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
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class SnowmansPane {

	private static final Random RANDOM = new Random();

	private static final Pane pane = new Pane();
	private static final Image bgImage = new Image("fxgame/images/snowmanspane.png");

	private static final Brinn player = Game.getPlayer();
	private static final List<Sprite> sprites = new ArrayList<Sprite>();
	private static final List<AnimatedSprite> monsters = new ArrayList<AnimatedSprite>();
	private static final List<Rectangle2D> obstacles = new ArrayList<Rectangle2D>();
	private static final List<InteractionBox> interactions = new ArrayList<InteractionBox>();
	private static final Map<KeyCode, GameState> exits = new HashMap<KeyCode, GameState>();

	private static final AudioClip wooshSound = new AudioClip(
		SnowmansPane.class.getResource("audio/woosh.wav").toString()
	);

	private static final Timeline monstersTimeline = new Timeline(
		new KeyFrame(
				Duration.ZERO, e -> {
					for (AnimatedSprite monster : monsters) {
						int randomDirection = RANDOM.nextInt(4);
						switch(randomDirection) {
							case 0:
								monster.walkDown();
								if (monster.getYPos() < Game.getPlayer().getYPos()
										&& monster.getXPos() != -500)
									throwSnowball((Snowman) monster);
								break;
							case 1:
								monster.walkUp();
								if (monster.getYPos() > Game.getPlayer().getYPos()
										&& monster.getXPos() != -500)
									throwSnowball((Snowman) monster);
								break;
							case 2:
								monster.walkRight();
								if (monster.getXPos() < Game.getPlayer().getXPos()
										&& monster.getXPos() != -500)
									throwSnowball((Snowman) monster);
								break;
							case 3:
								monster.walkLeft();
								if (monster.getXPos() > Game.getPlayer().getXPos()
										&& monster.getXPos() != -500)
									throwSnowball((Snowman) monster);
						}
					}
				}
		),
		new KeyFrame(Duration.seconds(2))
	);

	private static InteractionBox blockExits = new InteractionBox(null, null,
			"Don't want to wander... The\nworld is dangerous.");

	static {
		sprites.add(player);

		monstersTimeline.setCycleCount(Timeline.INDEFINITE);

		pane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

		createMonsters();

		Sprite sign = new Sign();
		sign.setPos(547, 306);
		sprites.add(sign);
		pane.getChildren().add(sign.getImageView());

		InteractionBox readSign = new InteractionBox(
			new Rectangle2D(557, 344, 20, 2), KeyCode.UP,
			"Watch out for snowballs!"
		);
		interactions.add(readSign);

		// Bottom trees
		Sprite trees = new Sprite("fxgame/images/snowmansptrees.png", 99, 80);
		trees.setCBox(4, 52, trees.getWidth() - 4, trees.getHeight() - 52);
		trees.setPos(541, 400);
		sprites.add(trees);

		pane.getChildren().add(trees.getImageView());

		// Top trees
		obstacles.add(new Rectangle2D(545, 0, 95, 308));

		exits.put(KeyCode.RIGHT, GameState.FORKED_PATH);
	}

	public static Pane getPane() {
		pane.setOpacity(0);
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), pane);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);

		player.setXPos(Game.WINDOW_WIDTH - Controller.OFFSCREEN_X);
		player.setYPos(350); //temp

		pane.getChildren().add(player.getImageView());

		for (Sprite monster : monsters) {
			if (!pane.getChildren().contains(monster.getImageView()))
				pane.getChildren().add(monster.getImageView());
		}

		initMonsterPos();
		monstersTimeline.play();

		Game.getPlayerController().setVals(pane, sprites, monsters, obstacles, interactions, exits);
		Game.setCurrentState(GameState.SNOWMANS);

		fadeTransition.play();

		return pane;
	}

	// Initialize monsters and set their initial positions on the pane
	private static void createMonsters() {
		for (int i = 1; i <= 7; i++) {
			Snowman snowman = new Snowman();
			pane.getChildren().add(snowman.getImageView());
			sprites.add(snowman);
			monsters.add(snowman);
		}
		initMonsterPos();
	}

	// Set initial positions on the pane for each monster
	private static void initMonsterPos() {
		int i = 1;
		for (AnimatedSprite monster : monsters) {
			switch (i)
			{
			case 1: monster.setPos(50, 54); break;
			case 2: monster.setPos(178, 126); break;
			case 3: monster.setPos(300, 70); break;
			case 4: monster.setPos(436, 26); break;
			case 5: monster.setPos(66, 280); break;
			case 6: monster.setPos(198, 268); break;
			case 7: monster.setPos(390, 238); break;
			}
			i++;
		}
	}

	private static void throwSnowball(Snowman snowman) {
		Game.getPlayerController().addSnowball(snowman.throwSnowball());
		wooshSound.play();
	}

	public static InteractionBox getExitsInteractionBox() {
		blockExits.getTextAnimation().clearDisplayedText();
		return blockExits;
	}

	public static void pauseTimeline() {
		monstersTimeline.pause();
	}
	public static void stopTimeline() {
		monstersTimeline.stop();
	}
	public static void restartTimeline() {
		monstersTimeline.playFrom(Duration.millis(1900));
	}

}
