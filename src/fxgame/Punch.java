package fxgame;

import javafx.scene.image.ImageView;

public class Punch extends Sprite {

	private static final ImageView IMAGE_UP = new ImageView("fxgame/images/punchup.png");
	private static final ImageView IMAGE_RIGHT = new ImageView("fxgame/images/punchright.png");
	private static final ImageView IMAGE_DOWN = new ImageView("fxgame/images/punchdown.png");
	private static final ImageView IMAGE_LEFT = new ImageView("fxgame/images/punchleft.png");

	private static final ImageView[] images = { IMAGE_UP, IMAGE_RIGHT, IMAGE_DOWN, IMAGE_LEFT };

	Punch() {
		super("fxgame/images/punchdown.png", 16, 36);
	}

	public void punchUp() {
		this.setImageView(IMAGE_UP);
		this.setXPos(Game.getPlayer().getXPos() + 22);
		this.setYPos(Game.getPlayer().getYPos() + 10);
		this.setWidth(12);
		this.setHeight(30);
		this.setCBox(-2, -4, getWidth()+4, getHeight()+4);
	}

	public void punchRight() {
		this.setImageView(IMAGE_RIGHT);
		this.setXPos(Game.getPlayer().getXPos() + 25);
		this.setYPos(Game.getPlayer().getYPos() + 38);
		this.setWidth(30);
		this.setHeight(12);
		this.setCBox(0, -2, getWidth()+2, getHeight()+4);
	}

	public void punchDown() {
		this.setImageView(IMAGE_DOWN);
		this.setXPos(Game.getPlayer().getXPos() + 9);
		this.setYPos(Game.getPlayer().getYPos() + 41);
		this.setWidth(12);
		this.setHeight(30);
		this.setCBox(-2, 0, getWidth()+4, getHeight()+2);
	}

	public void punchLeft() {
		this.setImageView(IMAGE_LEFT);
		this.setXPos(Game.getPlayer().getXPos() - 16);
		this.setYPos(Game.getPlayer().getYPos() + 38);
		this.setWidth(30);
		this.setHeight(12);
		this.setCBox(-2, -2, getWidth()+2, getHeight()+4);
	}

	public ImageView[] getAllImages() {
		return images;
	}

}
