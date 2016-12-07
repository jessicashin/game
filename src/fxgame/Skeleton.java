package fxgame;

import java.util.Random;

import javafx.geometry.Rectangle2D;

public class Skeleton extends AnimatedSprite {

	private static final Random RANDOM = new Random();

	private static final String IMAGE_PATH = "fxgame/images/skeleton.png";

	private static final int SPRITE_WIDTH = 48;
	private static final int SPRITE_HEIGHT = 62;

	private static final int SPRITE_COUNT = 4;
	private static final int SPRITE_COLUMNS = 4;

	private static final int ANIM_DURATION = 500;

	private static final int FRONT_OFFSET_Y = 0;
	private static final int LEFT_OFFSET_Y = SPRITE_HEIGHT;
	private static final int BACK_OFFSET_Y = SPRITE_HEIGHT*2;
	private static final int RIGHT_OFFSET_Y = SPRITE_HEIGHT*3;

	Skeleton() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT, SPRITE_COUNT, SPRITE_COLUMNS, ANIM_DURATION);
		this.setSpeed(90);
		this.setCBox(7, 27, 34, 28);
	}


	public void standFront() {
		stopMoving();
		getImageView().setViewport(new Rectangle2D(0, FRONT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT));
	}

	public void standLeft() {
		stopMoving();
		getImageView().setViewport(new Rectangle2D(0, LEFT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT));
	}

	public void standBack() {
		stopMoving();
		getImageView().setViewport(new Rectangle2D(0, BACK_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT));
	}

	public void standRight() {
		stopMoving();
		getImageView().setViewport(new Rectangle2D(0, RIGHT_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT));
	}

	@Override
	public void walkDown() {
		super.walkDown();
		this.getAnimation().setOffsetY(FRONT_OFFSET_Y);
	}

	@Override
	public void walkLeft() {
		super.walkLeft();
		this.getAnimation().setOffsetY(LEFT_OFFSET_Y);
	}

	@Override
	public void walkUp() {
		super.walkUp();
		this.getAnimation().setOffsetY(BACK_OFFSET_Y);
	}

	@Override
	public void walkRight() {
		super.walkRight();
		this.getAnimation().setOffsetY(RIGHT_OFFSET_Y);
	}

	@Override
	public Sprite itemDrop() {
		// 1 in 3 chance of dropping a winterfruit
		int randomChance = RANDOM.nextInt(3);
		if (randomChance == 0) {
			Winterfruit fruit = new Winterfruit();
			fruit.setPos(getXPos() + getWidth()/2 - fruit.getWidth()/2,
					getYPos() + getHeight() - fruit.getHeight());
			return fruit;
		}

		return null;
	}

}
