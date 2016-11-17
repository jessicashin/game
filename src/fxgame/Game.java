package fxgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Game extends Application {

	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 480;

	private static Scene scene;

	public static enum GameState {
		TITLE, PART_ONE, LAB_EIGHT,
		ROOM, HOME
	}

	private static GameState currentState;

	private static final Brinn player = new Brinn();
	private static final PlayerController pController = new PlayerController();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		currentState = GameState.TITLE;
		stage.setScene(TitleScene.getScene());

		// Press key on title screen to proceed
		TitleScene.getScene().setOnKeyPressed(e -> {
			TitleScene.stopMusic();
			switch (e.getCode()) {
				case P:
					currentState = GameState.PART_ONE;
					stage.setScene(PartOne.getScene());
					PartOne.getScene().setOnKeyPressed(event -> {
						if (event.getCode() == KeyCode.BACK_SPACE) {
							PartOne.stopMusic();
							currentState = GameState.TITLE;
							stage.setScene(TitleScene.getScene());
						}
					});
					break;
				case L:
					currentState = GameState.LAB_EIGHT;
					stage.setScene(LabEight.getScene());
					break;

				case ENTER: case DIGIT2:
					currentState = GameState.ROOM;
					scene = new Scene(RoomPane.getPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
					pController.start();
					stage.setScene(scene);
				default: break;
			}
		});

		stage.setTitle("The Cooling - JavaFX Game");
		stage.setWidth(WINDOW_WIDTH);
		stage.setHeight(WINDOW_HEIGHT);
		stage.setResizable(false);
		stage.show();

	}

	public static void setPane(GameState state) {
		switch (currentState) {
			case ROOM: RoomPane.stopMusic(); break;
			case HOME: HomePane.stopMusic(); break;
			default: break;
		}

		currentState = state;
		switch (state) {
			case ROOM: scene.setRoot(RoomPane.getPane()); break;
			case HOME: scene.setRoot(HomePane.getPane()); break;
			default: break;
		}
	}

	public static Brinn getPlayer() {
		return player;
	}

	public static PlayerController getPlayerController() {
		return pController;
	}

	public static Scene getScene() {
		return scene;
	}

	public static GameState getCurrentState() {
		return currentState;
	}

}
