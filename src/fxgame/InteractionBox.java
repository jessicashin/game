package fxgame;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InteractionBox {

	private Rectangle2D box;
	private KeyCode direction;
	private Pane modalPane = new StackPane();

	InteractionBox(Rectangle2D box, KeyCode direction) {
		this.box = box;
		this.direction = direction;
		modalPane.setPrefSize(560, 160);
		modalPane.setPadding(new Insets(20, 20, 20, 20));

		Rectangle bg = new Rectangle(560, 160);
		bg.setFill(Color.BLACK);
		bg.setStroke(Color.WHITE);
		bg.setStrokeWidth(6);

		modalPane.getChildren().add(bg);
		modalPane.setLayoutX(20);
		modalPane.setLayoutY(280);
	}

	InteractionBox(Rectangle2D box, KeyCode direction, String message) {
		this(box, direction);
		Text text = new Text(message);
		text.setFill(Color.WHITE);
		text.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/DTM-Mono.otf"), 26));
		text.setWrappingWidth(500);
		text.setLineSpacing(6);
		modalPane.getChildren().add(text);
	}

	InteractionBox(Rectangle2D box, KeyCode direction, Text... texts) {
		this(box, direction);
		for (Text text : texts) {
			text.setFill(Color.WHITE);
			text.setWrappingWidth(500);
			modalPane.getChildren().add(text);
		}
	}

	public Rectangle2D getBox() {
		return box;
	}

	public KeyCode getDirection() {
		return direction;
	}

	public Pane getModalPane() {
		return modalPane;
	}

}
