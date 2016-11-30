package fxgame;

import javafx.scene.image.ImageView;

public class Punch extends Sprite {

	private static final ImageView IMAGE_UP = new ImageView("fxgame/images/punchup.png");
	private static final ImageView IMAGE_RIGHT = new ImageView("fxgame/images/punchright.png");
	private static final ImageView IMAGE_DOWN = new ImageView("fxgame/images/punchdown.png");
	private static final ImageView IMAGE_LEFT = new ImageView("fxgame/images/punchleft.png");

	public void punchUp() {
		this.setImageView(IMAGE_UP);
		this.getImageView().setTranslateX(Game.getPlayer().getXPos() + 18);
		this.getImageView().setTranslateY(Game.getPlayer().getYPos() + 10);
		this.setWidth(16);
		this.setHeight(36);
		this.setCBox(0, 0, getWidth(), getHeight());
	}

	public void punchRight() {
		this.setImageView(IMAGE_RIGHT);
		this.getImageView().setTranslateX(Game.getPlayer().getXPos() + 16);
		this.getImageView().setTranslateY(Game.getPlayer().getYPos() + 38);
		this.setWidth(39);
		this.setHeight(14);
		this.setCBox(0, 0, getWidth(), getHeight());
	}

	public void punchDown() {
		this.setImageView(IMAGE_DOWN);
		this.getImageView().setTranslateX(Game.getPlayer().getXPos() + 5);
		this.getImageView().setTranslateY(Game.getPlayer().getYPos() + 35);
		this.setWidth(16);
		this.setHeight(36);
		this.setCBox(0, 0, getWidth(), getHeight());
	}

	public void punchLeft() {
		this.setImageView(IMAGE_LEFT);
		this.getImageView().setTranslateX(Game.getPlayer().getXPos() - 15);
		this.getImageView().setTranslateY(Game.getPlayer().getYPos() + 36);
		this.setWidth(39);
		this.setHeight(14);
		this.setCBox(0, 0, getWidth(), getHeight());
	}

}
