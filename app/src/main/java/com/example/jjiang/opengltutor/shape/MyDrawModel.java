package com.example.jjiang.opengltutor.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.jjiang.opengltutor.GLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by jjiang on 1/26/2017.
 */

public class MyDrawModel {
    private int programId;
    private int mVPMatrixHandle; // 总变换矩阵引用id
    private int positionHandle; // 顶点位置id
    private int texCoorHandle; // 顶点纹理坐标id

    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoorBuffer;
    private ShortBuffer drawListBuffer;



    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices


    public MyDrawModel() {
    }

    public void init() {
        initData();
        int vertexsharder = MyOpenGLUtils.loadShader(GLES20.GL_VERTEX_SHADER,
                GLScript.vertexShaderCode);
        int fragmentsharder = MyOpenGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER,
                GLScript.fragmentShaderCode);
        programId = MyOpenGLUtils.linkAttach(vertexsharder, fragmentsharder);
      //  boolean isOK = GLHelper.checkProgram(programId);
        positionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        texCoorHandle = GLES20.glGetAttribLocation(programId, "aTexCoor");
        mVPMatrixHandle = GLES20.glGetUniformLocation(programId, "uMVPMatrix");



     /*   Log.d(OPENGL, positionHandle: + positionHandle + ;texCoorHand11le:
        + texCoorHandle + ;mVPMatrixHandle: + mVPMatrixHandle + ;
        + isOK);*/
    }

    private void initData() {

        //X,Y,Z,绘画的顶点
        float vertices[] = new float[] {
                -0.8f,  -0.6f, 0.0f,   // top left
                -0.8f,  -0.8f, 0.0f,   // bottom left
                -0.6f,  -0.8f, 0.0f,   // bottom right
                -0.6f,  -0.6f, 0.0f  // top right
        };

        ByteBuffer vb = ByteBuffer.allocateDirect(vertices.length * 4);
        vb.order(ByteOrder.nativeOrder());
        vertexBuffer = vb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);


/*
        float vertices[] = new float[] {
                -0.8f,  -0.6f, 0.0f,   // top left
                -0.8f,  -0.8f, 0.0f,   // bottom left
                -0.6f,  -0.8f, 0.0f,   // bottom right
                -0.6f,  -0.6f, 0.0f  // top right
        };*/

        //纹理空间坐标 S,T
        float texCoor[] = new float[] {
                0.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f,
                1.0f,1.0f,
        };



        ByteBuffer cb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cb.order(ByteOrder.nativeOrder());
        texCoorBuffer = cb.asFloatBuffer();
        texCoorBuffer.put(texCoor);
        texCoorBuffer.position(0);



        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

    }

    public void drawFrame(int textureId,float[] mViewPjMatrix) {

        GLES20.glUseProgram(programId);


        GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, mViewPjMatrix, 0);

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(texCoorHandle,  2, GLES20.GL_FLOAT, false, 2 * 4, texCoorBuffer);

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(texCoorHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
       // GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);//六个定点，绘制三角形

        GLES20.glDrawElements(GLES20.GL_TRIANGLES,drawOrder.length,GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

    }

    public void drawFinish(){
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoorHandle);
    }
}
