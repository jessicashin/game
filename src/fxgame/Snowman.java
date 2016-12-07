package fxgame;

import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;

public class Snowman extends AnimatedSprite {

	private static final Random RANDOM = new Random();

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
		this.setCBox(7, 37, 40, 36);
		this.setSpeed(100);
	}


	private void stand() {
		setVelocity(0, 0);
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
		setXVelocity(0);
		setYVelocity(getSpeed());
		getImageView().setViewport(faceFront);
		setDirection(KeyCode.DOWN);
	}

	@Override
	public void walkLeft() {
		setXVelocity(-getSpeed());
		setYVelocity(0);
		getImageView().setViewport(faceLeft);
		setDirection(KeyCode.LEFT);
	}

	@Override
	public void walkUp() {
		setXVelocity(0);
		setYVelocity(-getSpeed());
		getImageView().setViewport(faceBack);
		setDirection(KeyCode.UP);
	}

	@Override
	public void walkRight() {
		setXVelocity(getSpeed());
		setYVelocity(0);
		getImageView().setViewport(faceRight);
		setDirection(KeyCode.RIGHT);
	}

	public Snowball throwSnowball() {
		Snowball snowball = new Snowball();
		switch(getDirection()) {
			case UP:
				snowball.setYVelocity(-snowball.getSpeed());
				snowball.setPos(getXPos() + getWidth()/2 - snowball.getWidth()/2, getYPos() - snowball.getHeight());
				break;
			case RIGHT:
				snowball.setXVelocity(snowball.getSpeed());
				snowball.setPos(getXPos() + getWidth(), getYPos() + getHeight()/2 - snowball.getHeight()/2);
				break;
			case DOWN:
				snowball.setYVelocity(snowball.getSpeed());
				snowball.setPos(getXPos() + getWidth()/2 - snowball.getWidth()/2, getYPos() + getHeight());
				break;
			case LEFT:
				snowball.setXVelocity(-snowball.getSpeed());
				snowball.setPos(getXPos() - snowball.getWidth(), getYPos() + getHeight()/2 - snowball.getHeight()/2);
				break;
			default: break;
		}
		return snowball;
	}

	@Override
	public Sprite itemDrop() {
		// 50% chance of dropping a winterfruit
		int randomChance = RANDOM.nextInt(2);
		if (randomChance == 0) {
			Winterfruit fruit = new Winterfruit();
			fruit.setPos(getXPos() + getWidth()/2 - fruit.getWidth()/2,
					getYPos() + getHeight() - fruit.getHeight());
			return fruit;
		}

		// Lower chance of dropping an icemelon (rare)
		// Higher drop rate than for skeleton
		randomChance = RANDOM.nextInt(5);
		if (randomChance == 0) {
			Icemelon fruit = new Icemelon();
			fruit.setPos(getXPos() + getWidth()/2 - fruit.getWidth()/2,
					getYPos() + getHeight() - fruit.getHeight());
			return fruit;
		}

		return null;
	}

}
