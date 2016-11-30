package fxgame;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Luffy extends AnimatedSprite {

	private static final String IMAGE_PATH = "fxgame/images/dog.png";

	private static final int SPRITE_WIDTH = 52;
	private static final int SPRITE_HEIGHT = 40;
	private static final int SPRITE_COUNT = 4;
	private static final int SPRITE_COLUMNS = 4;
	private static final int ANIM_DURATION = 600;

	private static final int FRONT_OFFSET_Y = 0;
	private static final int STAND_LEFT_OFFSET_Y = SPRITE_HEIGHT;
	private static final int LAY_LEFT_OFFSET_Y = SPRITE_HEIGHT*2;
	private static final int WALK_LEFT_OFFSET_Y = SPRITE_HEIGHT*3;
	private static final int WALK_BACK_OFFSET_Y = SPRITE_HEIGHT*4;
	private static final int STAND_RIGHT_OFFSET_Y = SPRITE_HEIGHT*5;
	private static final int LAY_RIGHT_OFFSET_Y = SPRITE_HEIGHT*6;
	private static final int WALK_RIGHT_OFFSET_Y = SPRITE_HEIGHT*7;
	private static final int STAND_BACK_OFFSET_Y = SPRITE_HEIGHT*8;

	private static final Rectangle2D layLeft = new Rectangle2D(0, LAY_LEFT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
	private static final Rectangle2D layRight = new Rectangle2D(0, LAY_RIGHT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
	private static final Rectangle2D faceFront = new Rectangle2D(0, FRONT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);

	private static final Text dialogue = new Text("Woof woof! Woof!");
	private static final Text heartText = new Text("\u2764   \u2764   \u2764");
	private final InteractionBox interactionBox = new InteractionBox(
		new Rectangle2D(0, 0, 4, 4), KeyCode.RIGHT, dialogue, heartText
	);

	Luffy() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT, SPRITE_COUNT, SPRITE_COLUMNS, ANIM_DURATION);
		this.setSpeed(60);
		this.setCBox(8, 18, 36, 3);
		dialogue.setFill(Color.WHITE);
		dialogue.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/Sans.ttf"), 26));
		dialogue.setWrappingWidth(500);
		dialogue.setLineSpacing(6);
		heartText.setFill(Color.WHITE);
		heartText.setFont(new Font(26));
	}


	public void layLeft() {
		getAnimation().stop();
		getImageView().setViewport(layLeft);
	}

	public void layRight() {
		getAnimation().stop();
		getImageView().setViewport(layRight);
	}

	public void standFront() {
		getAnimation().stop();
		getImageView().setViewport(faceFront);
	}

	public void standLeft() {
		getAnimation().setDuration(Duration.millis(ANIM_DURATION));
		getAnimation().setOffsetY(STAND_LEFT_OFFSET_Y);
		getAnimation().play();
	}

	public void standBack() {
		getAnimation().setDuration(Duration.millis(ANIM_DURATION/2));
		getAnimation().setOffsetY(STAND_BACK_OFFSET_Y);
		getAnimation().play();
	}

	public void standRight() {
		getAnimation().setDuration(Duration.millis(ANIM_DURATION));
		getAnimation().setOffsetY(STAND_RIGHT_OFFSET_Y);
		getAnimation().play();
	}

	@Override
	public void walkDown() {
		getAnimation().setDuration(Duration.millis(ANIM_DURATION/2));
		getAnimation().setOffsetY(FRONT_OFFSET_Y);
		getAnimation().play();
		setDirection(KeyCode.DOWN);
	}

	@Override
	public void walkLeft() {
		getAnimation().setDuration(Duration.millis(ANIM_DURATION));
		getAnimation().setOffsetY(WALK_LEFT_OFFSET_Y);
		getAnimation().play();
		setDirection(KeyCode.LEFT);
	}

	@Override
	public void walkUp() {
		getAnimation().setDuration(Duration.millis(ANIM_DURATION/2));
		getAnimation().setOffsetY(WALK_BACK_OFFSET_Y);
		getAnimation().play();
		setDirection(KeyCode.UP);
	}

	@Override
	public void walkRight() {
		getAnimation().setDuration(Duration.millis(ANIM_DURATION));
		getAnimation().setOffsetY(WALK_RIGHT_OFFSET_Y);
		getAnimation().play();
		setDirection(KeyCode.RIGHT);
	}

	public InteractionBox getInteractionBox() {
		return interactionBox;
	}

	public void eatItem(Sprite item) {
		// TODO: add consequence for eating
	}

}
