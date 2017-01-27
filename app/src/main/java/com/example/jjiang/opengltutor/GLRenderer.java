package com.example.jjiang.opengltutor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import com.example.jjiang.opengltutor.shape.MyDrawModel;
import com.example.jjiang.opengltutor.shape.MyOpenGLUtils;
import com.example.jjiang.opengltutor.shape.Square;
import com.example.jjiang.opengltutor.shape.Triangle;

import java.io.InputStream;
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


    public static float[] projMatrix = new float[16];// 投影
    public static float[] viewMatrix = new float[16];// 相机
    public static float[] mViewPjMatrix = new float[16];// 总变换矩阵
    public static int textureId = -1;
    public int testid = -1;
    Context context;
    MyDrawModel drawModel,test;

    public GLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);


        float[] matrixs = new float[16];
        // // 初始化矩阵
        Matrix.setRotateM(matrixs, 0, 0, 1, 0, 0);
        Matrix.translateM(matrixs, 0, 0, 0, 1);

        //矩阵转换 ,投影矩阵，摄像机矩阵，模型矩阵
        Matrix.multiplyMM(mViewPjMatrix, 0, viewMatrix,0, matrixs, 0);
        Matrix.multiplyMM(mViewPjMatrix, 0, projMatrix,0, mViewPjMatrix, 0);

        test.drawFrame(testid,mViewPjMatrix);
        test.drawFinish();


        drawModel.drawFrame(textureId, mViewPjMatrix);
        drawModel.drawFinish();


    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
        float ratio = (float) w / h;
        Matrix.frustumM(projMatrix, 0, -ratio, ratio, -1, 1, 1, 10);//投影矩阵设置
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0, 0, 0, 0.0f, 1.0f, 0.0f);//摄像机坐标设置
    }

    @Override
    public void onSurfaceCreated(GL10 g, EGLConfig eglConfig) {
        GLES20.glClearColor(0.5f,0.5f,0.5f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        drawModel = new MyDrawModel();
        test = new MyDrawModel();
        test.init();
        drawModel.init();

        textureId = MyOpenGLUtils.loadTexture(context,R.drawable.icon_activity_camera_header_home);
        testid = MyOpenGLUtils.loadTexture(context,R.drawable.bumpy_bricks_public_domain);
       /* try {
            ins = context.getAssets().open(house.jpg);
            textureId = TextureUtils.createTexture(ins);
            Log.e(, textureId: + textureId);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }
}
