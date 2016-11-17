package fxgame;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public class Sprite implements Comparable<Sprite> {

	private String imagePath;
	private ImageView sprite;

	private int width;
	private int height;

	private double xPos;
	private double yPos;

	private int speed;
	private int xVelocity;
	private int yVelocity;

	// Collision bounding box values
	private int cBoxOffsetX;
	private int cBoxOffsetY;
	private int cBoxWidth;
	private int cBoxHeight;

	Sprite(String imagePath, int width, int height) {
		this(width, height);
		this.imagePath = imagePath;
		sprite = new ImageView(imagePath);
	}

	Sprite(int width, int height) {
		this.width = width;
		this.height = height;
		xVelocity = 0;
		yVelocity = 0;
	}


	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setImageView(ImageView imageView) {
		sprite = imageView;
	}

	public ImageView getImageView() {
		return sprite;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getXPos() {
		return xPos;
	}

	public void setXPos(double x) {
		xPos = x;
	}

	public double getYPos() {
		return yPos;
	}

	public void setYPos(double y) {
		yPos = y;
	}

	public void setPos(double x, double y) {
		setXPos(x);
		setYPos(y);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getXVelocity() {
		return xVelocity;
	}

	public void setXVelocity(int velocity) {
		this.xVelocity = velocity;
	}

	public int getYVelocity() {
		return yVelocity;
	}

	public void setYVelocity(int velocity) {
		this.yVelocity = velocity;
	}

	public void setVelocity(int x, int y) {
		setXVelocity(x);
		setYVelocity(y);
	}

	public int getCBoxOffsetX() {
		return cBoxOffsetX;
	}

	public void setCBoxOffsetX(int offsetX) {
		cBoxOffsetX = offsetX;
	}

	public int getCBoxOffsetY() {
		return cBoxOffsetY;
	}

	public void setCBoxOffsetY(int offsetY) {
		cBoxOffsetY = offsetY;
	}

	public int getCBoxWidth() {
		return cBoxWidth;
	}

	public void setCBoxWidth(int width) {
		cBoxWidth = width;
	}

	public int getCBoxHeight() {
		return cBoxHeight;
	}

	public void setCBoxHeight(int height) {
		cBoxHeight = height;
	}

	public void setCBox(int offsetX, int offsetY, int width, int height) {
		cBoxOffsetX = offsetX;
		cBoxOffsetY = offsetY;
		cBoxWidth = width;
		cBoxHeight = height;
	}

	public Rectangle2D getCBox() {
		return new Rectangle2D(xPos + cBoxOffsetX, yPos + cBoxOffsetY, cBoxWidth, cBoxHeight);
	}

	@Override
	public int compareTo(Sprite sprite) {
		// Compares the top of each collision box (for sorting z-order of nodes)
		return Double.compare(this.yPos + this.cBoxOffsetY, sprite.yPos + sprite.cBoxOffsetY);
	}

}
