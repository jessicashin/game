package fxgame;

import fxgame.Game.GameState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameOverPane {

	private static final VBox gameOverPane = new VBox();

	private static final Text restartText = new Text("[Press Z or ENTER to restart]");

	private static final AudioClip gameOverSoundEffect = new AudioClip(
		LabEight.class.getResource("audio/gameover.wav").toString()
	);
	private static final MediaPlayer gameOverMusic = new MediaPlayer(
		new Media(LabEight.class.getResource("audio/gameover.mp3").toString())
	);

	static {
		gameOverMusic.setVolume(0.5);

		StackPane gameOverStack = new StackPane();
		Text gameOverBack = new Text("game over");
		gameOverBack.setFill(Color.WHITE);
		gameOverBack.setOpacity(0.5);
		gameOverBack.setFont(Font.loadFont(LabEight.class.getResourceAsStream("fonts/MonsterFriendBack.otf"), 50));
		Text gameOverText = new Text("game over");
		gameOverText.setFill(Color.WHITE);
		gameOverText.setFont(Font.loadFont(LabEight.class.getResourceAsStream("fonts/MonsterFriendFore.otf"), 50));
		gameOverStack.getChildren().addAll(gameOverBack, gameOverText);

		// Instructions for restarting the game (starts out hidden)
		restartText.setFill(Color.BLACK);
		restartText.setFont(Font.loadFont(LabEight.class.getResourceAsStream("fonts/DTM-Mono.otf"), 20));

		gameOverPane.getChildren().addAll(gameOverStack, restartText);
		gameOverPane.setSpacing(50);
		VBox.setMargin(gameOverStack, new Insets(50, 0, 0, 0));
		gameOverPane.setAlignment(Pos.CENTER);
		gameOverPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		gameOverPane.setMinSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		gameOverPane.setMaxSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		gameOverPane.setPrefSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
	}

	public static Pane getPane() {
		Game.setCurrentState(GameState.GAME_OVER);
		gameOverSoundEffect.play();

		// Play music after 3 seconds, once the game over sound effect finishes
		Timeline delayMusic = new Timeline(new KeyFrame(Duration.seconds(3), e -> gameOverMusic.play()));

		// Display the instructions for restarting after 3 seconds
		restartText.setFill(Color.BLACK);
		Timeline delayRestartText = new Timeline(
				new KeyFrame(Duration.seconds(3), e -> restartText.setFill(Color.LIGHTGRAY))
		);

		// Let key press restart game after 3 seconds on the game over pane
		Timeline delayRestart = new Timeline(new KeyFrame(Duration.millis(3100), event -> {
			Game.getScene().setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.Z) {
					gameOverMusic.stop();
					Game.setPane(GameState.ROOM);
					Game.getPlayerController().start();
				}
				else if (e.getCode() == KeyCode.ESCAPE) {
					Platform.exit();
				}
			});
		}));

		delayMusic.play();
		delayRestartText.play();
		delayRestart.play();

		return gameOverPane;
	}

}
