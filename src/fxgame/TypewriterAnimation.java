package fxgame;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TypewriterAnimation extends Transition {

	private String fullText;
	private Text displayedText;
	private static final AudioClip textSoundEffect = new AudioClip(
		TypewriterAnimation.class.getResource("audio/text.wav").toString()
	);

	public TypewriterAnimation(String string, Text text) {
		textSoundEffect.setVolume(1.5);
		fullText = string;
		displayedText = text;
		setCycleDuration(Duration.millis(string.length()*30));
        setInterpolator(Interpolator.LINEAR);
	}

	@Override
	protected void interpolate(double frac) {
		final int length = fullText.length();
        final int n = Math.round(length * (float) frac);
        textSoundEffect.play();
        displayedText.setText(fullText.substring(0, n));

        if (displayedText.getText().equals(fullText)) {
        	stop();
        }
	}

	public void clearDisplayedText() {
		displayedText.setText("");
	}

}
