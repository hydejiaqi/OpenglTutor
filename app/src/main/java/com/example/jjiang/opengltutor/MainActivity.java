package com.example.jjiang.opengltutor;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean supportsEs2;
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            glSurfaceView = new GLSurfaceView(this);
            glSurfaceView.setEGLContextClientVersion(2);  // Pick an OpenGL ES 2.0 context.
            glSurfaceView.setRenderer(new GLRenderer());
            glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            setContentView(glSurfaceView);

    }



    @Override
    protected void onPause() {
        super.onPause();
        if (glSurfaceView != null) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glSurfaceView != null) {
            glSurfaceView.onResume();
        }
    }
}
