package fxgame;

public class Tree extends Sprite {

	private static final String IMAGE_PATH = "fxgame/images/tree.png";

	private static final int SPRITE_WIDTH = 108;
	private static final int SPRITE_HEIGHT = 120;

	private boolean hasFruit = false;

	Tree() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.setCBoxOffsetX(10);
		this.setCBoxOffsetY(52);
		this.setCBoxWidth(88);
		this.setCBoxHeight(62);
	}

	public boolean hasFruit() {
		return hasFruit;
	}

	public void growFruit() {
		hasFruit = true;
	}

	public void pickFruit() {
		hasFruit = false;
	}

}
