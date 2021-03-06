package fxgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game extends Application {

	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 480;

	private static Stage stage;
	private static Scene scene;

	// Each of these is a pane representing one part of the world view
	// (PART_ONE and LAB_EIGHT are scenes)
	public static enum GameState {
		TITLE, INSTRUCTION, PART_ONE, LAB_EIGHT,
		ROOM, HOME, EXIT_HOME, EXIT_HOME2, FORKED_PATH,
		SKELETONS, RIGHT_CLIFF, SNOWMANS,
		GAME_OVER
	}

	private static GameState currentState;

	// These are static across the whole game
	// The pane classes refer to these instances
	private static final Brinn player = new Brinn();
	private static final Punch playerPunch = new Punch();
	private static final Luffy dog = new Luffy();
	private static final Controller controller = new Controller();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Game.stage = stage;
		currentState = GameState.TITLE;
		stage.setScene(TitleScene.getScene());

		stage.setTitle("The Cooling");
		stage.getIcons().add(new Image("fxgame/images/icon.png"));
		stage.getScene().setFill(Color.BLACK);
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
			case RIGHT_CLIFF: scene.setRoot(RightCliffPane.getPane()); break;
			case SNOWMANS: scene.setRoot(SnowmansPane.getPane()); break;
			case GAME_OVER: scene.setRoot(GameOverPane.getPane()); HomePane.stopMusic(); break;
			default: break;
		}
	}

	public static Brinn getPlayer() {
		return player;
	}

	public static Luffy getDog() {
		return dog;
	}

	public static Controller getPlayerController() {
		return controller;
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

	public static Punch getPlayerPunch() {
		return playerPunch;
	}

}
