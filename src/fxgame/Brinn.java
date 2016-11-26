package fxgame;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Rectangle2D;

public class Brinn extends AnimatedSprite {

	private static final String IMAGE_PATH = "fxgame/images/girl.png";

	private static final int SPRITE_WIDTH = 37;
	private static final int SPRITE_HEIGHT = 62;

	private static final int SPRITE_COUNT = 4;
	private static final int SPRITE_COLUMNS = 4;

	private static final int ANIM_DURATION = 480;

	private static final int FRONT_OFFSET_Y = 0;
	private static final int LEFT_OFFSET_Y = SPRITE_HEIGHT;
	private static final int BACK_OFFSET_Y = SPRITE_HEIGHT*2;
	private static final int RIGHT_OFFSET_Y = SPRITE_HEIGHT*3;

	private int hearts = 1;
	private int maxHearts = 1; // maxHearts will increase as player levels up
	private int level = 1;

	private List<Sprite> carriedItems = new LinkedList<Sprite>();


	Brinn() {
		super(IMAGE_PATH, SPRITE_WIDTH, SPRITE_HEIGHT, SPRITE_COUNT, SPRITE_COLUMNS, ANIM_DURATION);
		this.setSpeed(150);
		this.setCBox(3, 34, 31, 23);
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


	public int getHearts() {
		return hearts;
	}

	public void setHearts(int hearts) {
		this.hearts = hearts;
	}


	public int getFullHearts() {
		return maxHearts;
	}

	public void setFullHearts(int fullHearts) {
		this.maxHearts = fullHearts;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void aquireItem(Sprite item) {
		carriedItems.add(item);
	}

	public void removeItem(Sprite item) {
		carriedItems.remove(item);
	}

	public void eatItem(Sprite item) {
		removeItem(item);
		// TODO: add consequence for eating
	}

	public void loseAllItems() {
		carriedItems.clear();
	}

}
