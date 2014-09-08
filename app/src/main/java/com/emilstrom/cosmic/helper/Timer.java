package com.emilstrom.cosmic.helper;

import com.emilstrom.cosmic.game.Game;

/**
 * Created by Emil on 2014-03-19.
 */
public class Timer {
	float timer, timerMax;

	public Timer(float currentTimer, boolean startFinished) {
		this.timerMax = currentTimer;
		if (startFinished) timer = timerMax;
		else timer = 0;
	}

	public boolean isDone() { return timer >= timerMax; }
	public float percentageDone() { return Math.min(1f, timer / timerMax); }

	public void reset() {
		timer = 0;
	}

	public void reset(float max) {
		timerMax = max;
		reset();
	}

	public void logic() {
		if (isDone()) return;

		timer += Game.updateTime;
	}

	public void logic(float factor) {
		if (isDone()) return;

		timer += Game.updateTime * factor;
	}
}
