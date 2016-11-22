package fxgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application {

	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 480;

	private static Stage stage;
	private static Scene scene;

	public static enum GameState {
		TITLE, PART_ONE, LAB_EIGHT,
		ROOM, HOME, EXIT_HOME, EXIT_HOME2, FORKED_PATH,
		SKELETONS,
		GAME_OVER
	}

	private static GameState currentState;

	private static final Brinn player = new Brinn();
	private static final PlayerController pController = new PlayerController();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Game.stage = stage;
		currentState = GameState.TITLE;
		stage.setScene(TitleScene.getScene());

		stage.setTitle("The Cooling - JavaFX Game");
		stage.setResizable(false);
		stage.show();

	}

	public static void setPane(GameState state) {
		switch (state) {
			case ROOM: scene.setRoot(RoomPane.getPane()); HomePane.stopMusic(); break;
			case HOME: scene.setRoot(HomePane.getPane()); RoomPane.stopMusic(); break;
			case EXIT_HOME: scene.setRoot(ExitHomePane.getPane()); break;
			case EXIT_HOME2: scene.setRoot(ExitHome2Pane.getPane()); break;
			case FORKED_PATH: scene.setRoot(ForkedPathPane.getPane()); break;
			case SKELETONS: scene.setRoot(SkeletonsPane.getPane()); break;
			case GAME_OVER: scene.setRoot(GameOverPane.getPane()); HomePane.stopMusic(); break;
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

	public static void setScene(Scene scene) {
		Game.scene = scene;
	}

	public static GameState getCurrentState() {
		return currentState;
	}

	public static void setCurrentState(GameState state) {
		currentState = state;
	}

	public static Stage getStage() {
		return stage;
	}

}
