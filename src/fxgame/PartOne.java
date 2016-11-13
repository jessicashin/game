package fxgame;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class PartOne {

	private static final VBox vbox = new VBox();
	private static final Scene scene = new Scene(vbox);

	private static final MediaPlayer introMusic = new MediaPlayer(
		new Media(PartOne.class.getResource("audio/intro.mp3").toString())
	);

	// Form at top of scene with sprite selection and scale input
	private static final HBox form = new HBox();
	// ComboBox populated with all entities
	private static final ComboBox<String> spriteSelect = new ComboBox<String>();
	// Text input field for scale percentage
	private static final TextField scaleInput = new TextField();

	// Pane to draw sprites on click
	private static final Pane drawingPane = new Pane();
	private static final Rectangle drawingPaneBorder = new Rectangle();

	// Map of entity name (String) to entity Image for entity selection
	private static final Map<String, Sprite> entityHash = new LinkedHashMap<String, Sprite>();

	// Map of entity name (String) to its values needed for animation
	private static final Map<String, Map<String, Integer>> animationHash = new HashMap<String, Map<String, Integer>>();

	private static final AudioClip dropSoundEffect = new AudioClip(
		PartOne.class.getResource("audio/bop.wav").toString()
	);
	private static final AudioClip errorSoundEffect = new AudioClip(
		PartOne.class.getResource("audio/error.wav").toString()
	);

	static {
		initEntityHash();
		initAnimationHash();

		scene.getStylesheets().add("fxgame/fxgame.css");

		initForm();
		initDrawingPane();

		vbox.getChildren().addAll(form, drawingPane);

		introMusic.setVolume(0.6);
		introMusic.setCycleCount(AudioClip.INDEFINITE);

		startClickEventHandler();

		vbox.setMinSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		VBox.setVgrow(drawingPane, Priority.ALWAYS);
	}

	public static Scene getScene() {
		introMusic.play();
		return scene;
	}

	private static void initEntityHash() {
		entityHash.put("Brinn", new Brinn());
		entityHash.put("Luffy", new Luffy());
		entityHash.put("heart", new Heart());
		entityHash.put("skeleton", new Skeleton());
		entityHash.put("snowman", new Snowman());
		entityHash.put("snowball", new Snowball());
		entityHash.put("fish", new Fish());
		entityHash.put("tree", new Tree());
		entityHash.put("winterfruit", new Winterfruit());
		entityHash.put("icemelon", new Icemelon());
	}

	private static void initAnimationHash() {
		// Create maps of animation values for each entity image
		final Map<String, Integer> girlAnimVals = new HashMap<String, Integer>();
		girlAnimVals.put("count", 32);
		girlAnimVals.put("columns", 8);
		girlAnimVals.put("duration", 5000);
		final Map<String, Integer> dogAnimVals = new HashMap<String, Integer>();
		dogAnimVals.put("count", 64);
		dogAnimVals.put("columns", 8);
		dogAnimVals.put("duration", 9000);
		final Map<String, Integer> skeletonAnimVals = new HashMap<String, Integer>();
		skeletonAnimVals.put("count", 32);
		skeletonAnimVals.put("columns", 8);
		skeletonAnimVals.put("duration", 6500);
		final Map<String, Integer> snowmanAnimVals = new HashMap<String, Integer>();
		snowmanAnimVals.put("count", 4);
		snowmanAnimVals.put("columns", 4);
		snowmanAnimVals.put("duration", 2000);

		animationHash.put("Brinn", girlAnimVals);
		animationHash.put("Luffy", dogAnimVals);
		animationHash.put("skeleton", skeletonAnimVals);
		animationHash.put("snowman", snowmanAnimVals);
	}

	// Create the form for sprite selection and scale input
	private static void initForm() {
		spriteSelect.setPromptText("Select sprite");
		for (Map.Entry<String, Sprite> sprite : entityHash.entrySet()) {
			spriteSelect.getItems().add(sprite.getKey());
		}
		spriteSelect.setStyle("-fx-font-family: 'Determination Sans';");

		// Text input field to get a decimal or integer number for scale percentage
		HBox scalePercentage = new HBox();
		scalePercentage.setAlignment(Pos.CENTER_LEFT);
		scalePercentage.setSpacing(5);
		scaleInput.setPromptText("Enter scale");
		scaleInput.setStyle("-fx-font-family: 'Determination Sans';");
		scaleInput.setPrefWidth(100);
		Text percentSign = new Text("%");
		percentSign.setFont(Font.loadFont(PartOne.class.getResourceAsStream("fonts/DTM-Mono.otf"), 16));
		percentSign.setFill(Color.WHITE);
		scalePercentage.getChildren().addAll(scaleInput, percentSign);

		Text instructions = new Text("Click anywhere below to draw the sprite!");
		instructions.setFont(Font.loadFont(PartOne.class.getResourceAsStream("fonts/DTM-Sans.otf"), 16));
		instructions.setFill(Color.WHITE);

		form.getChildren().addAll(spriteSelect, scalePercentage, instructions);
		form.setSpacing(30);
		form.setPadding(new Insets(20, 20, 15, 20));
		form.setAlignment(Pos.CENTER);
		form.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
	}

	// Create the pane where the user clicks to draw sprites
	private static void initDrawingPane() {
		drawingPane.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY, null, null)));
		Rectangle clipOverflow = new Rectangle();
		clipOverflow.widthProperty().bind(drawingPane.widthProperty());
		clipOverflow.heightProperty().bind(drawingPane.heightProperty());
		drawingPane.setClip(clipOverflow);

		drawingPaneBorder.widthProperty().bind(drawingPane.widthProperty());
		drawingPaneBorder.heightProperty().bind(drawingPane.heightProperty());
		drawingPaneBorder.setY(form.getHeight());
		drawingPaneBorder.setStroke(Color.BLACK);
		drawingPaneBorder.setStrokeWidth(20);
		drawingPaneBorder.setFill(Color.TRANSPARENT);
		drawingPane.getChildren().add(drawingPaneBorder);
	}

	// Validate sprite selection and scale input
	private static String validateForm(String sprite, String scale) {
		String errorMessage = "";
		if (sprite == null) {
			errorMessage = "\nYou must select a sprite to draw.";
		}
		if (!scale.isEmpty()) {
			if (!scale.matches("^[1-9]\\d*(\\.\\d+)?$")) {
				errorMessage += "\nYou must enter a valid number for scale.";
			}
			else if (Double.parseDouble(scale) < 20 || Double.parseDouble(scale) > 1000) {
				errorMessage += "\nPlease keep scale between 20% and 1000%";
			}
		}
		return errorMessage;
	}

	private static void startClickEventHandler() {
		drawingPane.setOnMouseClicked(event -> {
			String sprite = spriteSelect.getValue();
			String errorMessage = validateForm(spriteSelect.getValue(), scaleInput.getText());
			// If validation fails, flash an error message
			if (!errorMessage.isEmpty()) {
				drawErrorPane(errorMessage);
			}
			else {
				// If no input for scale, default to 100%
				double scale = 1;
				// Otherwise set the scale to the input
				if (!scaleInput.getText().isEmpty()) {
					scale = Double.parseDouble(scaleInput.getText())/100;
				}

				// For animated sprites, draw animation
				if (animationHash.containsKey(sprite)) {
					drawAnimation(sprite, scale, event.getX(), event.getY());
				}

				// Draw entity with JavaFX shapes
				else if (sprite.equals("icemelon")) {
					drawShapeSprite(scale, event.getX(), event.getY());
				}

				// Draw the rest as plain images
				else {
					drawImage(sprite, scale, event.getX(), event.getY());
				}

				drawingPaneBorder.toFront();
				dropSoundEffect.play();
			}
		});
	}

	private static void drawErrorPane(String message) {
		StackPane errorPane = new StackPane();
		Rectangle errorBg = new Rectangle(500, 200);
		errorBg.setFill(Color.BLACK);
		errorBg.setStroke(Color.WHITE);
		errorBg.setStrokeWidth(5);
		VBox error = new VBox();
		error.setAlignment(Pos.CENTER);
		Text errorText = new Text("ERROR:");
		errorText.setFont(Font.loadFont(PartOne.class.getResourceAsStream("fonts/DTM-Mono.otf"), 24));
		errorText.setFill(Color.WHITE);
		Text errorMsgText = new Text(message);
		errorMsgText.setFont(Font.loadFont(PartOne.class.getResourceAsStream("fonts/DTM-Mono.otf"), 17));
		errorMsgText.setTextAlignment(TextAlignment.CENTER);
		errorMsgText.setFill(Color.WHITE);
		error.getChildren().addAll(errorText, errorMsgText);
		errorPane.getChildren().addAll(errorBg, error);
		errorPane.setPrefSize(drawingPane.getWidth(), drawingPane.getHeight());
		errorPane.setAlignment(Pos.CENTER);

		errorSoundEffect.play();
		drawingPane.getChildren().add(errorPane);

		FadeTransition fade = new FadeTransition(Duration.millis(500), errorPane);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDelay(Duration.seconds(3));
        fade.play();
        fade.setOnFinished(e -> drawingPane.getChildren().remove(errorPane));
	}

	// Create ImageView from entity name
	private static void drawImage(String name, double scale, double mouseX, double mouseY) {
		Sprite sprite = entityHash.get(name);
		ImageView imageView;
		if (scale >=1) {
			Image image = new Image(sprite.getImagePath(), sprite.getWidth()*scale, sprite.getHeight()*scale, true, false);
			imageView = new ImageView(image);
		}
		else {
			imageView = new ImageView(sprite.getImagePath());
			imageView.setScaleX(scale);
			imageView.setScaleY(scale);
		}
		imageView.setX(mouseX - imageView.getBoundsInLocal().getWidth()/2);
		imageView.setY(mouseY - imageView.getBoundsInLocal().getHeight()/2);
		drawingPane.getChildren().add(imageView);
	}

	// Create animated ImageView from entity name
	private static void drawAnimation(String name, double scale, double mouseX, double mouseY) {
		Sprite sprite = entityHash.get(name);
		ImageView imageView;
		Animation animation;
		if (scale >= 1) {
			Image image = new Image (
					sprite.getImagePath(), sprite.getWidth()*animationHash.get(name).get("columns")*scale,
					sprite.getHeight()*(animationHash.get(name).get("count")/animationHash.get(name).get("columns"))*scale,
					true, false
			);
			imageView = new ImageView(image);
	        imageView.setViewport(new Rectangle2D(0, 0, sprite.getWidth()*scale, sprite.getHeight()*scale));
	        animation = new SpriteAnimation (
	                imageView, Duration.millis(animationHash.get(name).get("duration")),
	                animationHash.get(name).get("count"), animationHash.get(name).get("columns"),
	                0, 0, sprite.getWidth()*scale, sprite.getHeight()*scale
	        );
		}
		else {
			imageView = new ImageView(sprite.getImagePath());
			imageView.setViewport(new Rectangle2D(0, 0, sprite.getWidth(), sprite.getHeight()));
			imageView.setScaleX(scale);
			imageView.setScaleY(scale);
			animation = new SpriteAnimation (
					imageView, Duration.millis(animationHash.get(name).get("duration")),
	                animationHash.get(name).get("count"), animationHash.get(name).get("columns"),
	                0, 0, sprite.getWidth(), sprite.getHeight()
			);
		}
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        imageView.setX(mouseX - imageView.getBoundsInLocal().getWidth()/2);
        imageView.setY(mouseY - imageView.getBoundsInLocal().getHeight()/2);
        drawingPane.getChildren().add(imageView);
	}

	// Create entity from JavaFX shapes (icemelon)
	private static void drawShapeSprite(double scale, double mouseX, double mouseY) {
		Icemelon icemelon = new Icemelon(scale);
		icemelon.getSprite().relocate(mouseX, mouseY);
		drawingPane.getChildren().add(icemelon.getSprite());
		icemelon.getSprite().setTranslateX(-icemelon.getSprite().getBoundsInParent().getWidth()/2);
		icemelon.getSprite().setTranslateY(-icemelon.getSprite().getBoundsInParent().getHeight()/2);
	}
}
