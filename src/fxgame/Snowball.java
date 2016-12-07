package fxgame;

public class Snowball extends Sprite {

	private static final String IMAGE_PATH = "fxgame/images/snowball.png";

	private static final int SPRITE_WIDTH = 32;
	private static final int SPRITE_HEIGHT = 24;

	Snowball() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.setSpeed(200);
		this.setVelocity(0, 0);
	}

}
