package fxgame;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class Game extends Application {

	public static final int WINDOW_WIDTH = 700;
	public static final int WINDOW_HEIGHT = 500;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setScene(TitleScreen.getScene());

		// Press key on title screen to proceed
		TitleScreen.getScene().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DIGIT2) {
				TitleScreen.stopMusic();

				//stage.setScene(PartOne.getScene());
				stage.setScene(LabEight.getScene());

			}
		});

		stage.setTitle("The Cooling - JavaFX Game");
		stage.setMinWidth(WINDOW_WIDTH);
		stage.setMinHeight(WINDOW_HEIGHT);
		stage.show();

	}

}
