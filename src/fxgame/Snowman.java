package fxgame;

import javafx.geometry.Rectangle2D;

public class Snowman extends AnimSprite {

	private static final String IMAGE_PATH = "fxgame/images/snowman.png";

	private static final int SPRITE_WIDTH = 54;
	private static final int SPRITE_HEIGHT = 80;

	private static final int FRONT_OFFSET_X = 0;
	private static final int LEFT_OFFSET_X = SPRITE_WIDTH;
	private static final int BACK_OFFSET_X = SPRITE_WIDTH*2;
	private static final int RIGHT_OFFSET_X = SPRITE_WIDTH*3;

	private final Rectangle2D faceFront = new Rectangle2D(FRONT_OFFSET_X, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
	private final Rectangle2D faceLeft = new Rectangle2D(LEFT_OFFSET_X, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
	private final Rectangle2D faceBack = new Rectangle2D(BACK_OFFSET_X, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
	private final Rectangle2D faceRight = new Rectangle2D(RIGHT_OFFSET_X, 0, SPRITE_WIDTH, SPRITE_HEIGHT);

	Snowman() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.setSpeed(110);
	}


	private void stand() {
		setXvelocity(0);
		setYvelocity(0);
	}

	public void standFront() {
		stand();
		getImageView().setViewport(faceFront);
	}

	public void standLeft() {
		stand();
		getImageView().setViewport(faceLeft);
	}

	public void standBack() {
		stand();
		getImageView().setViewport(faceBack);
	}

	public void standRight() {
		stand();
		getImageView().setViewport(faceRight);
	}

	@Override
	public void walkDown() {
		setXvelocity(0);
		setYvelocity(getSpeed());
		getImageView().setViewport(faceFront);
	}

	@Override
	public void walkLeft() {
		setXvelocity(-getSpeed());
		setYvelocity(0);
		getImageView().setViewport(faceLeft);
	}

	@Override
	public void walkUp() {
		setXvelocity(0);
		setYvelocity(-getSpeed());
		getImageView().setViewport(faceBack);
	}

	@Override
	public void walkRight() {
		setXvelocity(getSpeed());
		setYvelocity(0);
		getImageView().setViewport(faceRight);
	}

}
