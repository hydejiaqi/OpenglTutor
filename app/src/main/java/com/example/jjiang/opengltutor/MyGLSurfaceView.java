package com.example.jjiang.opengltutor;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jjiang on 1/24/2017.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private GLRenderer mRenderer;


    public MyGLSurfaceView(Context context) {
        super(context);

        mRenderer = new GLRenderer();
        setEGLContextClientVersion(2);  // Pick an OpenGL ES 2.0 context.
        setRenderer(mRenderer);
        //      glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRenderer = new GLRenderer();
        setEGLContextClientVersion(2);  // Pick an OpenGL ES 2.0 context.
        setRenderer(mRenderer);
        //      glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }


    @Override
    public boolean onTouchEvent(MotionEvent e){

        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;



        return true;
    }
}