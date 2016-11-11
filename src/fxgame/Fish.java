package fxgame;

public class Fish extends Sprite {

	private static final String IMAGE_PATH = "fxgame/images/fish.png";

	private static final int SPRITE_WIDTH = 32;
	private static final int SPRITE_HEIGHT = 24;

	Fish() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.setSpeed(200);
		this.setVelocity(-this.getSpeed(), 0); // Fish swim from right to left
	}

}
