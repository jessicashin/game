package fxgame;

public class Tree extends Sprite {

	private static final String IMAGE_PATH = "fxgame/images/tree.png";

	private static final int SPRITE_WIDTH = 38;
	private static final int SPRITE_HEIGHT = 56;

	Tree() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.setCBoxOffsetX(0);
		this.setCBoxOffsetY(33);
		this.setCBoxWidth(SPRITE_WIDTH);
		this.setCBoxHeight(SPRITE_HEIGHT - this.getCBoxOffsetY());
	}

}
