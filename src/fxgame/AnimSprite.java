package fxgame;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public abstract class AnimSprite extends Sprite {

	private SpriteAnimation animation;
	private KeyCode direction = KeyCode.DOWN;

	AnimSprite(String imagePath, int width, int height, int count, int columns, int animDuration) {
		this(imagePath, width, height);
		animation = new SpriteAnimation(
				getImageView(), Duration.millis(animDuration),
				count, columns, 0, 0, width, height
		);
		animation.setCycleCount(Animation.INDEFINITE);
	}

	AnimSprite(String imagePath, int width, int height) {
		super(imagePath, width, height);
		getImageView().setViewport(new Rectangle2D(0, 0, width, height));
	}

	public void stopMoving() {
		setXVelocity(0);
		setYVelocity(0);
		animation.stop();
	}
	public abstract void standFront();
	public abstract void standLeft();
	public abstract void standBack();
	public abstract void standRight();

	public void walkDown() {
		setXVelocity(0);
		setYVelocity(getSpeed());
		animation.play();
		direction = KeyCode.DOWN;
	}
	public void walkLeft() {
		setXVelocity(-getSpeed());
		setYVelocity(0);
		animation.play();
		direction = KeyCode.LEFT;
	}
	public void walkUp() {
		setXVelocity(0);
		setYVelocity(-getSpeed());
		animation.play();
		direction = KeyCode.UP;
	}
	public void walkRight() {
		setXVelocity(getSpeed());
		setYVelocity(0);
		animation.play();
		direction = KeyCode.RIGHT;
	}

	public SpriteAnimation getAnimation() {
		return animation;
	}

	public KeyCode getDirection() {
		return direction;
	}
	public void setDirection(KeyCode direction) {
		this.direction = direction;
	}
	public boolean isFacingUp() {
		return (direction == KeyCode.UP);
	}
	public boolean isFacingRight() {
		return (direction == KeyCode.RIGHT);
	}
	public boolean isFacingDown() {
		return (direction == KeyCode.DOWN);
	}
	public boolean isFacingLeft() {
		return (direction == KeyCode.LEFT);
	}

}
