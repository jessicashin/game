package fxgame;

public class Sign extends Sprite {

	private static final String IMAGE_PATH = "fxgame/images/sign.png";

	private static final int SPRITE_WIDTH = 40;
	private static final int SPRITE_HEIGHT = 40;

	Sign() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.setCBoxOffsetX(0);
		this.setCBoxOffsetY(12);
		this.setCBoxWidth(this.getWidth());
		this.setCBoxHeight(22);
	}

}
