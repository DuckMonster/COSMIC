package com.emilstrom.cosmic;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.emilstrom.cosmic.game.Game;
import com.emilstrom.cosmic.helper.InputHelper;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class GLSurface extends GLSurfaceView {
	Game game;

	public GLSurface(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 8);

		game = new Game();
		setRenderer(game);

		setRenderMode(GLSurface.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float w = getWidth(), h = getHeight();

		int pointerIndex = e.getActionIndex(),
				pointerID = e.getPointerId(pointerIndex);

		switch(e.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				InputHelper.setPressed(pointerID, true);
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				InputHelper.setPressed(pointerID, false);
				break;
		}

		for(int p=0; p<e.getPointerCount(); p++) {
			int id = e.getPointerId(p);

			float x = (e.getX(p) - w/2) / (w/2);
			float y = (e.getY(p) - h/2) / (-h/2);

			InputHelper.setPosition(id, x, y);
		}

		return true;
	}
}