package com.ferullogaming.countercraft.game.tasks;

import java.util.TimerTask;

public class RunnableTask extends TimerTask {

	Runnable runnable = null;

	public RunnableTask(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void run() {
		runnable.run();
	}

}
