package com.example.jjiang.opengltutor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.example.jjiang.opengltutor.shape.Square;
import com.example.jjiang.opengltutor.shape.Triangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import common.TextureHelper;

/**
 * Created by jjiang on 1/20/2017.
 */
public class GLRenderer implements GLSurfaceView.Renderer {


    private Triangle triangle;
    private Square square;
    public volatile float mAngle;
    private Context context;


    public GLRenderer(Context context){
    this.context = context;

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0.2f, 0.2f, 1.0f); //设置清屏颜色

        triangle = new Triangle();
        square = new Square(context);




        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        // Active the texture unit 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);



    }

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0, 0, width, height); //设置绘制区域
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        float ratio = (float) width / height;




        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
     Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);





    }



    @Override
    public void onDrawFrame(GL10 gl) {

        float[] scratch = new float[16];


        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);



        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

       square.drawSquare(mMVPMatrix);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
      //  Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);


        // Draw shape
   //     triangle.drawTriangle(mMVPMatrix);
       // square.drawSquare();






    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float mAngle) {
        this.mAngle = mAngle;
    }
}
