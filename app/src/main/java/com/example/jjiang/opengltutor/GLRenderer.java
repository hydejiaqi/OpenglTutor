package com.example.jjiang.opengltutor;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by jjiang on 1/20/2017.
 */
public class GLRenderer implements GLSurfaceView.Renderer {

    private float[] mTriangleArray = {
            0.0f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    private FloatBuffer mTriangleBuffer;

    public GLRenderer(){
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(mTriangleArray.length * 4);
        //以本机字节顺序来修改此缓冲区的字节顺序
        bb.order(ByteOrder.nativeOrder());
        mTriangleBuffer = bb.asFloatBuffer();
        //将给定float[]数据从当前位置开始，依次写入此缓冲区
        mTriangleBuffer.put(mTriangleArray);
        //设置此缓冲区的位置。如果标记已定义并且大于新的位置，则要丢弃该标记。
        mTriangleBuffer.position(0);

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0.2f, 0.2f, 1.0f); //设置清屏颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);




    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {


        // 创建顶点着色器
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        String vertexShaderSource =
                "attribute vec4 vPosition; \n"
                        + "void main() \n"
                        + "{ \n"
                        + " gl_Position = vPosition; \n"
                        + "} \n";

        GLES20.glShaderSource(vertexShader, vertexShaderSource);
        GLES20.glCompileShader(vertexShader);

        // 创建片元着色器
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        String fragmentShaderSource =
                "precision mediump float; \n"
                        + "void main() \n"
                        + "{ \n"
                        + " gl_FragColor = vec4 ( 1.0, 0.0, 0.0, 1.0 ); \n"
                        + "} \n";
        GLES20.glShaderSource(fragmentShader, fragmentShaderSource);
        GLES20.glCompileShader(fragmentShader);

        // 创建程序容器并连接
        int programObject = GLES20.glCreateProgram();
        GLES20.glAttachShader(programObject, vertexShader);
        GLES20.glAttachShader(programObject, fragmentShader);
        GLES20.glBindAttribLocation(programObject, 0, "vPosition");
        GLES20.glLinkProgram(programObject);


        // 释放着色器
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);

        // 运行程序
        GLES20.glViewport(0, 0, width, height); //设置绘制区域
        GLES20.glUseProgram(programObject);

        // 释放程序
        GLES20.glDeleteProgram(programObject);


        // 将顶点坐标传递到顶点着色器源码的vPosition变量中
        int mPositionHandle = GLES20.glGetAttribLocation(programObject, "vPosition");
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        // 画图
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);




    }
}
