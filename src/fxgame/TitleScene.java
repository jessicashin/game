package fxgame;

import fxgame.Game.GameState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TitleScene {

	private static final VBox vbox = new VBox();
	private static final Scene scene = new Scene(vbox, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
	private static final MediaPlayer titleMusic = new MediaPlayer(
		new Media(TitleScene.class.getResource("audio/title.mp3").toString())
	);

	static {
		// Create game title
		StackPane titleStack = new StackPane();
		Text titleBack = new Text("The Cooling");
		titleBack.setFont(Font.loadFont(TitleScene.class.getResourceAsStream("fonts/MonsterFriendBack.otf"), 50));
		titleBack.setFill(Color.WHITE);
		titleBack.setOpacity(0.5);
		Text titleFore = new Text("The Cooling");
		titleFore.setFont(Font.loadFont(TitleScene.class.getResourceAsStream("fonts/MonsterFriendFore.otf"), 50));
		titleFore.setFill(Color.WHITE);
		titleStack.getChildren().addAll(titleBack, titleFore);

		// Create subtitle instructions
		Text subtitle = new Text("[Press Z or ENTER to begin]");
		subtitle.setFont(Font.loadFont(TitleScene.class.getResourceAsStream("fonts/DTM-Mono.otf"), 20));
		subtitle.setFill(Color.LIGHTGRAY);

		vbox.getChildren().addAll(titleStack, subtitle);
		vbox.setMinSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		vbox.setMaxSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		vbox.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		vbox.setSpacing(50);
		VBox.setMargin(titleStack, new Insets(50, 0, 0, 0));
		vbox.setAlignment(Pos.CENTER);
		vbox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		titleMusic.setCycleCount(AudioClip.INDEFINITE);
		titleMusic.setVolume(0.6);
	}


	public static Scene getScene() {
		titleMusic.play();

		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case P:
					TitleScene.stopMusic();
					Game.setCurrentState(GameState.PART_ONE);
					Game.getStage().setScene(PartOne.getScene());
					break;
				case L:
					TitleScene.stopMusic();
					Game.setCurrentState(GameState.LAB_EIGHT);
					Game.getStage().setScene(LabEight.getScene());
					break;

				case ENTER: case Z:
					Game.setCurrentState(GameState.INSTRUCTION);
					Game.getStage().setScene(InstructionScene.getScene());
					break;

				case ESCAPE:
					Platform.exit();

				default: break;
			}
		});

		return scene;
	}

	public static void stopMusic() {
		titleMusic.stop();
	}
}
