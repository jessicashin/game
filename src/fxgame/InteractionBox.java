package fxgame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

// This class handles the collision box and modal message display
// for any sprite interactions

public class InteractionBox {

	private Rectangle2D box;
	private KeyCode direction;
	private Pane modalPane = new StackPane();
	private Insets modalPaneInsets = new Insets(26, 26, 26, 26);
	private TypewriterAnimation typewriter;

	// variables for messages with multiple Text objects
	private boolean animationFinished = false;
	private Timeline typewriterTimeline;
	private TypewriterAnimation[] animations;
	private Text[] texts;
	private String[] fullTexts;

	InteractionBox(Rectangle2D box, KeyCode direction) {
		this.box = box;
		this.direction = direction;
		modalPane.setPrefSize(560, 160);

		Rectangle bg = new Rectangle(560, 160);
		bg.setFill(Color.BLACK);
		bg.setStroke(Color.WHITE);
		bg.setStrokeWidth(6);

		modalPane.getChildren().add(bg);
		modalPane.setLayoutX(40);
		modalPane.setLayoutY(300);
	}

	InteractionBox(Rectangle2D box, KeyCode direction, String message) {
		this(box, direction);
		Text text = new Text();
		text.setFill(Color.WHITE);
		text.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/DTM-Mono.otf"), 26));
		text.setWrappingWidth(500);
		text.setLineSpacing(6);
		typewriter = new TypewriterAnimation(message, text);
		modalPane.getChildren().add(text);
		StackPane.setAlignment(text, Pos.TOP_CENTER);
		StackPane.setMargin(text, modalPaneInsets);
	}

	InteractionBox(Rectangle2D box, KeyCode direction, Text text) {
		this(box, direction);
		text.setFill(Color.WHITE);
		text.setWrappingWidth(500);
		text.setLineSpacing(6);
		typewriter = new TypewriterAnimation(text.getText(), text);
		modalPane.getChildren().add(text);
		StackPane.setAlignment(text, Pos.TOP_CENTER);
		StackPane.setMargin(text, modalPaneInsets);
	}

	InteractionBox(Rectangle2D box, KeyCode direction, Text... texts) {
		this(box, direction);
		VBox vbox = new VBox();
		vbox.setSpacing(10);
		this.texts = texts;
		fullTexts = new String[texts.length];
		animations = new TypewriterAnimation[texts.length];
		typewriterTimeline = new Timeline();
		double duration = 0;
		for (int i = 0; i < texts.length; i++) {
			fullTexts[i] = texts[i].getText();
			texts[i].setFill(Color.WHITE);
			texts[i].setWrappingWidth(500);
			vbox.getChildren().add(texts[i]);
			TypewriterAnimation animation = new TypewriterAnimation(texts[i].getText(), texts[i]);
			animations[i] = animation;
			typewriterTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), e -> animation.play()));
			duration += animation.getCycleDuration().toMillis() + 400;
			texts[i].setText("");
			if (i == texts.length-1) {
				typewriterTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration - 400)));
			}
		}
		typewriterTimeline.setOnFinished(e -> animationFinished = true);

		modalPane.getChildren().add(vbox);
		StackPane.setAlignment(vbox, Pos.TOP_CENTER);
		StackPane.setMargin(vbox, modalPaneInsets);
	}

	public Rectangle2D getBox() {
		return box;
	}

	public void setBox(Rectangle2D box) {
		this.box = box;
	}

	public KeyCode getDirection() {
		return direction;
	}

	public void setDirection(KeyCode direction) {
		this.direction = direction;
	}

	public TypewriterAnimation getTextAnimation() {
		return typewriter;
	}

	public boolean isTextTimelineFinished() {
		return animationFinished;
	}

	public void fastForwardText() {
		if (typewriter != null) {
			if (typewriter.getStatus() == Animation.Status.RUNNING) {
				typewriter.jumpTo("end");
				typewriter.stop();
			}
		}
		else if (typewriterTimeline != null) {
			if (typewriterTimeline.getStatus() == Animation.Status.RUNNING) {
				animationFinished = true;
				typewriterTimeline.stop();
				for (TypewriterAnimation ta : animations) {
					ta.stop();
				}
				for (int i = 0; i < texts.length; i++) {
					texts[i].setText(fullTexts[i]);
				}
			}
		}
	}

	public Pane getModalPaneAndPlay() {
		if (typewriter != null) {
			typewriter.play();
		}
		else if (typewriterTimeline != null){
			animationFinished = false;
			for (Text text : texts) {
				text.setText("");
			}
			typewriterTimeline.play();
		}
		return modalPane;
	}

	public Pane getModalPane() {
		return modalPane;
	}

}
