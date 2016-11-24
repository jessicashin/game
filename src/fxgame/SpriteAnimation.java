package fxgame;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private int count;
    private int columns;
    private double offsetX;
    private double offsetY;
    private final double width;
    private final double height;

    private int lastIndex;

    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count,   int columns,
            double offsetX, double offsetY,
            double width,   double height) {
        this.imageView = imageView;
        this.count     = count;
        this.columns   = columns;
        this.offsetX   = offsetX;
        this.offsetY   = offsetY;
        this.width     = width;
        this.height    = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            final double x = (index % columns) * width  + offsetX;
            final double y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }

    public void setCount(int count) {
    	this.count = count;
    }

    public void setColumns(int columns) {
    	this.columns = columns;
    }

    public void setOffsetX(double offset) {
    	offsetX = offset;
    }

    public void setOffsetY(double offset) {
    	offsetY = offset;
    }

    public void setDuration(Duration duration) {
    	if (getCycleDuration() != duration)
    		setCycleDuration(duration);
    }
}
