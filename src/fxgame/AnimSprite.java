package fxgame;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

public abstract class AnimSprite extends Sprite {

	private SpriteAnimation animation;

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
		setXvelocity(0);
		setYvelocity(0);
		animation.stop();
	}
	public abstract void standFront();
	public abstract void standLeft();
	public abstract void standBack();
	public abstract void standRight();

	public void walkDown() {
		setXvelocity(0);
		setYvelocity(getSpeed());
		animation.play();
	}
	public void walkLeft() {
		setXvelocity(-getSpeed());
		setYvelocity(0);
		animation.play();
	}
	public void walkUp() {
		setXvelocity(0);
		setYvelocity(-getSpeed());
		animation.play();
	}
	public void walkRight() {
		setXvelocity(getSpeed());
		setYvelocity(0);
		animation.play();
	}
	public SpriteAnimation getAnimation() {
		return animation;
	}

}
