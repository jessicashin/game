package fxgame;

public class Tree extends Sprite {

	private static final String IMAGE_PATH = "fxgame/images/tree.png";

	private static final int SPRITE_WIDTH = 38;
	private static final int SPRITE_HEIGHT = 56;

	private int age = 0;
	private boolean hasFruit = false;

	Tree() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.setCBoxOffsetX(0);
		this.setCBoxOffsetY(33);
		this.setCBoxWidth(SPRITE_WIDTH);
		this.setCBoxHeight(SPRITE_HEIGHT - this.getCBoxOffsetY());
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
