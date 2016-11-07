package fxgame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class App extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene titleScene = new Scene(createTitleScreen());

		MediaPlayer titleMusic = new MediaPlayer(
			new Media(getClass().getResource("audio/title.mp3").toString())
		);
		titleMusic.play();
		titleMusic.setCycleCount(AudioClip.INDEFINITE);
		titleMusic.setVolume(0.6);

		// Press key on title screen to proceed
		titleScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DIGIT2) {

//				PartOne p1 = new PartOne();
//				Scene partOneScene = new Scene(p1.createPartOne());
//				partOneScene.getStylesheets().add("fxgame/fxgame.css");
//				stage.setScene(partOneScene);

				LabEight l8= new LabEight();
				Scene labEightScene = l8.createSpriteAnimationGame();
				stage.setScene(labEightScene);

				titleMusic.stop();
			}
		});

		stage.setScene(titleScene);
		stage.setTitle("THECOOLING JAVAFX");
		stage.setMinWidth(700);
		stage.setMinHeight(500);
		stage.show();
	}

	private VBox createTitleScreen() {
		VBox vbox = new VBox();

		// Create game title
		StackPane titleStack = new StackPane();
		Text titleBack = new Text("The Cooling");
		titleBack.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/MonsterFriendBack.otf"), 50));
		titleBack.setFill(Color.WHITE);
		titleBack.setOpacity(0.5);
		Text titleFore = new Text("The Cooling");
		titleFore.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/MonsterFriendFore.otf"), 50));
		titleFore.setFill(Color.WHITE);
		titleStack.getChildren().addAll(titleBack, titleFore);

		// Create subtitle instructions
		Text subtitle = new Text("[Press 2 or ENTER to begin]");
		subtitle.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/DTM-Mono.otf"), 20));
		subtitle.setFill(Color.LIGHTGRAY);

		vbox.getChildren().addAll(titleStack, subtitle);
		vbox.setMinSize(700, 500);
		vbox.setSpacing(50);
		vbox.setAlignment(Pos.CENTER);
		vbox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		return vbox;
	}

}
