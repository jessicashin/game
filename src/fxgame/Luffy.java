package fxgame;

import javafx.geometry.Rectangle2D;

public class Luffy extends AnimSprite {

	private static final String IMAGE_PATH = "fxgame/images/dog.png";

	private static final int SPRITE_WIDTH = 52;
	private static final int SPRITE_HEIGHT = 40;
	private static final int SPRITE_COUNT = 4;
	private static final int SPRITE_COLUMNS = 4;
	private static final int ANIM_DURATION = 500;

	private static final int FRONT_OFFSET_Y = 0;
	private static final int STAND_LEFT_OFFSET_Y = SPRITE_HEIGHT;
	private static final int LAY_LEFT_OFFSET_Y = SPRITE_HEIGHT*2;
	private static final int WALK_LEFT_OFFSET_Y = SPRITE_HEIGHT*3;
	private static final int BACK_OFFSET_Y = SPRITE_HEIGHT*4;
	private static final int STAND_RIGHT_OFFSET_Y = SPRITE_HEIGHT*5;
	private static final int LAY_RIGHT_OFFSET_Y = SPRITE_HEIGHT*6;
	private static final int WALK_RIGHT_OFFSET_Y = SPRITE_HEIGHT*7;

	private final Rectangle2D layLeft = new Rectangle2D(0, LAY_LEFT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
	private final Rectangle2D layRight = new Rectangle2D(0, LAY_RIGHT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
	private final Rectangle2D faceFront = new Rectangle2D(0, FRONT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
	private final Rectangle2D faceLeft = new Rectangle2D(0, STAND_LEFT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
	private final Rectangle2D faceBack = new Rectangle2D(0, BACK_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
	private final Rectangle2D faceRight = new Rectangle2D(0, STAND_RIGHT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);

	private SpriteAnimation animation;

	Luffy() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT, SPRITE_COUNT, SPRITE_COLUMNS, ANIM_DURATION);
		this.setSpeed(100);
	}


	public void layLeft() {
		stopMoving();
		getImageView().setViewport(layLeft);
	}

	public void layRight() {
		stopMoving();
		getImageView().setViewport(layRight);
	}

	public void standFront() {
		stopMoving();
		getImageView().setViewport(faceFront);
	}

	public void standLeft() {
		stopMoving();
		getImageView().setViewport(faceLeft);
	}

	public void standBack() {
		stopMoving();
		getImageView().setViewport(faceBack);
	}

	public void standRight() {
		stopMoving();
		getImageView().setViewport(faceRight);
	}

	@Override
	public void walkDown() {
		super.walkDown();
		animation.setOffsetY(FRONT_OFFSET_Y);
	}

	@Override
	public void walkLeft() {
		super.walkLeft();
		animation.setOffsetY(WALK_LEFT_OFFSET_Y);
	}

	@Override
	public void walkUp() {
		super.walkUp();
		animation.setOffsetY(BACK_OFFSET_Y);
	}

	@Override
	public void walkRight() {
		super.walkRight();
		animation.setOffsetY(WALK_RIGHT_OFFSET_Y);
	}

	public void eatItem(Sprite item) {
		// TODO: add consequence for eating
	}

}
