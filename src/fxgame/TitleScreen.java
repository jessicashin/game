package fxgame;

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

public class TitleScreen {

	private static Scene scene;
	private static final MediaPlayer titleMusic = new MediaPlayer(
		new Media(TitleScreen.class.getResource("audio/title.mp3").toString())
	);

	static {
		VBox vbox = new VBox();

		// Create game title
		StackPane titleStack = new StackPane();
		Text titleBack = new Text("The Cooling");
		titleBack.setFont(Font.loadFont(TitleScreen.class.getResourceAsStream("fonts/MonsterFriendBack.otf"), 50));
		titleBack.setFill(Color.WHITE);
		titleBack.setOpacity(0.5);
		Text titleFore = new Text("The Cooling");
		titleFore.setFont(Font.loadFont(TitleScreen.class.getResourceAsStream("fonts/MonsterFriendFore.otf"), 50));
		titleFore.setFill(Color.WHITE);
		titleStack.getChildren().addAll(titleBack, titleFore);

		// Create subtitle instructions
		Text subtitle = new Text("[Press 2 or ENTER to begin]");
		subtitle.setFont(Font.loadFont(TitleScreen.class.getResourceAsStream("fonts/DTM-Mono.otf"), 20));
		subtitle.setFill(Color.LIGHTGRAY);

		vbox.getChildren().addAll(titleStack, subtitle);
		vbox.setMinSize(700, 500);
		vbox.setSpacing(50);
		vbox.setAlignment(Pos.CENTER);
		vbox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		scene = new Scene(vbox);

		titleMusic.setCycleCount(AudioClip.INDEFINITE);
		titleMusic.setVolume(0.6);
	}


	public static Scene getScene() {
		titleMusic.play();
		return scene;
	}

	public static void stopMusic() {
		titleMusic.stop();
	}
}
