package fxgame;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class PartOne {

	// Map of entity name (String) to entity Image for entity selection
	static final Map<String, Sprite> entityHash = new LinkedHashMap<String, Sprite>();

	// Map of entity name (String) to its values needed for animation
	static final Map<String, Map<String, Integer>> animationHash = new HashMap<String, Map<String, Integer>>();

	PartOne() {
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

	public VBox createPartOne() {
		VBox vbox = new VBox();

		// ComboBox populated with all entities to select the sprite to display on click
		ComboBox<String> spriteSelect = new ComboBox<String>();
		spriteSelect.setPromptText("Select a sprite");
		for (Map.Entry<String, Sprite> sprite : entityHash.entrySet()) {
			spriteSelect.getItems().add(sprite.getKey());
		}
		spriteSelect.setStyle("-fx-font-family: 'Determination Sans';");

		// Text input field to get a decimal or integer number for scale percentage
		HBox scalePercentage = new HBox();
		scalePercentage.setAlignment(Pos.CENTER_LEFT);
		scalePercentage.setSpacing(5);
		TextField scaleInput = new TextField();
		scaleInput.setPromptText("Enter the scale percentage");
		scaleInput.setStyle("-fx-font-family: 'Determination Sans';");
		scaleInput.setPrefWidth(200);
		Text percentSign = new Text("%");
		percentSign.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/DTM-Mono.otf"), 16));
		percentSign.setFill(Color.WHITE);
		scalePercentage.getChildren().addAll(scaleInput, percentSign);

		Text instructions = new Text("Click anywhere below to draw the sprite!");
		instructions.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/DTM-Mono.otf"), 16));
		instructions.setFill(Color.WHITE);

		HBox form = new HBox();
		form.getChildren().addAll(spriteSelect, scalePercentage, instructions);
		form.setSpacing(40);
		form.setPadding(new Insets(30, 30, 30, 30));
		form.setAlignment(Pos.CENTER);
		form.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		// Pane to draw sprites on click
		Pane pane = new Pane();
		pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10))));
		pane.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		AudioClip dropSoundEffect = new AudioClip(getClass().getResource("audio/bop.wav").toString());
		AudioClip errorSoundEffect = new AudioClip(getClass().getResource("audio/error.wav").toString());

		pane.setOnMouseClicked(event -> {
			String sprite = spriteSelect.getValue();
			String error = validateForm(spriteSelect.getValue(), scaleInput.getText());
			// If validation fails, flash an error message
			if (!error.isEmpty()) {
				StackPane errorMessage = new StackPane();
				Rectangle errorBg = new Rectangle(600, 200);
				errorBg.setFill(Color.BLACK);
				errorBg.setStroke(Color.WHITE);
				errorBg.setStrokeWidth(5);
				Text errorText = new Text(error);
				errorText.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/DTM-Mono.otf"), 20));
				errorText.setTextAlignment(TextAlignment.CENTER);
				errorText.setFill(Color.WHITE);
				errorMessage.getChildren().addAll(errorBg, errorText);
				errorMessage.setPrefSize(pane.getWidth(), pane.getHeight());
				errorMessage.setAlignment(Pos.CENTER);
				pane.getChildren().add(errorMessage);
				errorSoundEffect.play();
				FadeTransition fade = new FadeTransition(Duration.millis(500), errorMessage);
		        fade.setFromValue(1);
		        fade.setToValue(0);
		        fade.setDelay(Duration.seconds(3));
		        fade.play();
		        fade.setOnFinished(e -> pane.getChildren().remove(errorMessage));
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
					ImageView spriteView = drawAnimation(sprite, scale);
					spriteView.setX(event.getX() - spriteView.getBoundsInParent().getWidth()/2);
					spriteView.setY(event.getY() - spriteView.getBoundsInParent().getHeight()/2);
					pane.getChildren().add(spriteView);
				}
				// Draw entity with JavaFX shapes
				else if (sprite.equals("icemelon")) {
					Group spriteView = createShapeSprite(scale);
					spriteView.relocate(event.getX(), event.getY());
					pane.getChildren().add(spriteView);
					spriteView.setTranslateX(-spriteView.getBoundsInParent().getWidth()/2);
					spriteView.setTranslateY(-spriteView.getBoundsInParent().getHeight()/2);
				}
				// Draw the rest as plain images
				else {
					ImageView spriteView = drawImage(sprite, scale);
					spriteView.setX(event.getX() - spriteView.getBoundsInParent().getWidth()/2);
					spriteView.setY(event.getY() - spriteView.getBoundsInParent().getHeight()/2);
					pane.getChildren().add(spriteView);
				}

				// Play sound effect on click
				dropSoundEffect.play();
			}
		});

		vbox.getChildren().addAll(form, pane);
		vbox.setMinSize(900, 700);
		VBox.setVgrow(pane, Priority.ALWAYS);

		return vbox;
	}

	// Validate sprite selection and scale input
	private String validateForm(String sprite, String scale) {
		String errorMessage = "";
		if (sprite == null) {
			errorMessage = "ERROR:\nYou must select a sprite to draw.";
		}
		if (!scale.isEmpty()) {
			if (!scale.matches("^[1-9]\\d*(\\.\\d+)?$")) {
				if (errorMessage.isEmpty()) {
					errorMessage += "ERROR:";
				}
				errorMessage += "\nYou must enter a valid number for scale.";
			}
			else if (Double.parseDouble(scale) < 20 || Double.parseDouble(scale) > 1000) {
				if (errorMessage.isEmpty()) {
					errorMessage += "ERROR:";
				}
				errorMessage += "\nPlease keep scale between 20% and 1000%";
			}
		}
		return errorMessage;
	}

	// Create ImageView from entity name
	private ImageView drawImage(String name, double scale) {
		Sprite sprite = entityHash.get(name);
		Image image = new Image(sprite.getImagePath(), sprite.getWidth()*scale, sprite.getHeight()*scale, true, false);
		ImageView imageView = new ImageView(image);
		return imageView;
	}

	// Create animated ImageView from entity name
	private ImageView drawAnimation(String name, double scale) {
		Sprite sprite = entityHash.get(name);
		Image image = new Image (
				sprite.getImagePath(), sprite.getWidth()*animationHash.get(name).get("columns")*scale,
				sprite.getHeight()*(animationHash.get(name).get("count")/animationHash.get(name).get("columns"))*scale,
				true, false
		);
		ImageView imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(0, 0, sprite.getWidth()*scale, sprite.getHeight()*scale));
        Animation animation = new SpriteAnimation(
                imageView, Duration.millis(animationHash.get(name).get("duration")),
                animationHash.get(name).get("count"), animationHash.get(name).get("columns"),
                0, 0, sprite.getWidth()*scale, sprite.getHeight()*scale
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        return imageView;
	}

	// Create entity from JavaFX shapes (icemelon)
	private Group createShapeSprite(double scale) {
		Icemelon icemelon = new Icemelon(scale);
		return icemelon.getSprite();
	}
}
