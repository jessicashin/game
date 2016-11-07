package fxgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Sprite made with JavaFX shapes
public class Icemelon extends Sprite {

	private Group sprite;
	private static final int SPRITE_WIDTH = 22;
	private static final int SPRITE_HEIGHT = 24;

	Icemelon() {
		super(SPRITE_WIDTH, SPRITE_HEIGHT);
		sprite = createSpriteWithShapes(1);
	}

	Icemelon(double scale) {
		super(SPRITE_WIDTH, SPRITE_HEIGHT);
		sprite = createSpriteWithShapes(scale);
	}

	private Group createSpriteWithShapes(double scale) {
		sprite = new Group();

		Rectangle borderRect1 = new Rectangle(10*scale, 24*scale);
		borderRect1.setTranslateX(6*scale);
		Rectangle borderRect2 = new Rectangle(14*scale, 20*scale);
		borderRect2.setTranslateX(4*scale);
		borderRect2.setTranslateY(2*scale);
		Rectangle borderRect3 = new Rectangle(18*scale, 16*scale);
		borderRect3.setTranslateX(2*scale);
		borderRect3.setTranslateY(4*scale);
		Rectangle borderRect4 = new Rectangle(22*scale, 12*scale);
		borderRect4.setTranslateY(6*scale);

		Rectangle dGreenRect1 = new Rectangle(18*scale, 12*scale);
		dGreenRect1.setTranslateX(2*scale);
		dGreenRect1.setTranslateY(6*scale);
		dGreenRect1.setFill(Color.web("rgb(26,115,144)"));

		Rectangle greenRect1 = new Rectangle(10*scale, 20*scale);
		greenRect1.setTranslateX(6*scale);
		greenRect1.setTranslateY(2*scale);
		greenRect1.setFill(Color.web("rgb(31,167,211)"));

		Rectangle dGreenVertical1 = new Rectangle(2*scale, 20*scale);
		dGreenVertical1.setTranslateX(8*scale);
		dGreenVertical1.setTranslateY(2*scale);
		dGreenVertical1.setFill(Color.web("rgb(26,115,144)"));

		Rectangle dGreenVertical2 = new Rectangle(2*scale, 20*scale);
		dGreenVertical2.setTranslateX(12*scale);
		dGreenVertical2.setTranslateY(2*scale);
		dGreenVertical2.setFill(Color.web("rgb(26,115,144)"));

		Rectangle greenRect2 = new Rectangle(14*scale, 16*scale);
		greenRect2.setTranslateX(4*scale);
		greenRect2.setTranslateY(4*scale);
		greenRect2.setFill(Color.web("rgb(31,167,211)"));

		Rectangle dGreenVertical3 = new Rectangle(2*scale, 16*scale);
		dGreenVertical3.setTranslateX(6*scale);
		dGreenVertical3.setTranslateY(4*scale);
		dGreenVertical3.setFill(Color.web("rgb(26,115,144)"));

		Rectangle dGreenVertical4 = new Rectangle(2*scale, 16*scale);
		dGreenVertical4.setTranslateX(10*scale);
		dGreenVertical4.setTranslateY(4*scale);
		dGreenVertical4.setFill(Color.web("rgb(26,115,144)"));

		Rectangle dGreenVertical5 = new Rectangle(2*scale, 16*scale);
		dGreenVertical5.setTranslateX(14*scale);
		dGreenVertical5.setTranslateY(4*scale);
		dGreenVertical5.setFill(Color.web("rgb(26,115,144)"));


		sprite.getChildren().addAll(borderRect1, borderRect2, borderRect3, borderRect4,
				dGreenRect1, greenRect1, dGreenVertical1, dGreenVertical2, greenRect2,
				dGreenVertical3, dGreenVertical4, dGreenVertical5);

		return sprite;
	}

	public Group getSprite() {
		return sprite;
	}

}
