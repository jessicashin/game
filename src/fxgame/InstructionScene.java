package fxgame;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class InstructionScene {

	private static final Pane pane = new Pane();
	private static final Scene scene = new Scene(pane, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
	private static final Image bgImage = new Image("fxgame/images/instructions.png");
	private static final Image luffyImage = new Image ("fxgame/images/dog.png", 416*2.5, 360*2.5, true, false);
	private static final ImageView luffy = new ImageView(luffyImage);
	private static final SpriteAnimation luffyAnimation = new SpriteAnimation(
		luffy, Duration.millis(400), 4, 4, 0.0, 200*2.5, 52*2.5, 40*2.5
	);

	static {
		pane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));
		luffy.setViewport(new Rectangle2D(0, 200*2.5, 52*2.5, 40*2.5));
		luffyAnimation.setCycleCount(Animation.INDEFINITE);
		luffyAnimation.play();
		luffy.setX(382);
		luffy.setY(316);
		pane.getChildren().add(luffy);
	}

	public static Scene getScene() {
		scene.setOnKeyPressed(e -> {
			switch(e.getCode()) {
				case ENTER: case Z:
					TitleScene.stopMusic();
					Game.setScene(new Scene(RoomPane.getPane(), Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT));
					Game.getPlayerController().start();
					Game.getScene().setFill(Color.BLACK);
					Game.getStage().setScene(Game.getScene());
					break;

				case ESCAPE:
					Platform.exit();
				default: break;
			}
		});
		return scene;
	}
}
