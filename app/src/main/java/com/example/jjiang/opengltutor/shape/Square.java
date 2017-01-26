package com.example.jjiang.opengltutor.shape;

import android.content.Context;
import android.opengl.GLES20;

import com.example.jjiang.opengltutor.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import common.TextureHelper;

/**
 * Created by jjiang on 1/23/2017.
 */

public class Square {

    private FloatBuffer vertexBuffer, texturebuffer;
    private ShortBuffer drawListBuffer;


    /** This will be used to pass in the texture. */
    private int mTextureUniformHandle;

    /** This will be used to pass in model texture coordinate information. */
    private int mTextureCoordinateHandle;

    /** Size of the texture coordinate data in elements. */
    private final int mTextureCoordinateDataSize = 2;



    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            0.5f,  1.0f, 0.0f,   // top left
            0.5f,  0.5f, 0.0f,   // bottom left
            1.0f,  0.5f, 0.0f,   // bottom right
            1.0f,  1.0f, 0.0f }; // top right
  /* static float squareCoords[] = {
           0.0f,  0.5f, 0.0f,   // top left
           0.0f, 0.0f, 0.0f,   // bottom left
           0.5f, 0.0f, 0.0f,   // bottom right
           0.5f,  0.5f, 0.0f }; // top right*/

    private Context context;

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices


    private final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };

    private float[] color = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };


/*    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";*/


    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec2 vCoordinte;"+
                    "uniform mat4 uMVPMatrix;" +
                    "varying vec2 aCoordinate;"+
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition ;" +
                    "  aCoordinate = vCoordinate;"+
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D vTexture;" +
                    "varying vec2 aCoordinate;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(vTexture,aCoordinate);" +
                    "}";

    public Square(Context context) {
       /* // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);*/
        this.context = context;
        vertexBuffer = MyOpenGLUtils.getBuffer(squareCoords);
        texturebuffer = MyOpenGLUtils.getBuffer(sCoord);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        int vertexShader = MyOpenGLUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyOpenGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // 创建一个空的OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // 将vertex shader添加到program
        GLES20.glAttachShader(mProgram, fragmentShader); // 将fragment shader添加到program
        GLES20.glLinkProgram(mProgram);                  // 创建可执行的 OpenGL ES program

    }

    int mProgram;


    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int texture;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    public void drawSquare(float[] mvpMatrix){





        // 将program加入OpenGL ES环境中
        GLES20.glUseProgram(mProgram);

        // 获取指向vertex shader的成员vPosition的 handle
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用一个指向三角形的顶点数组的handle
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // 获取指向fragment shader的成员vColor的handle
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // 设置三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        texture = MyOpenGLUtils.loadTexture(context, R.drawable.bumpy_bricks_public_domain);


        if(texture != -1 && texturebuffer != null){
            GLES20.glEnable(GLES20.GL_TEXTURE_2D);
            ;
        }

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyOpenGLUtils.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyOpenGLUtils.checkGlError("glUniformMatrix4fv");

        // 画正方形
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,drawOrder.length,GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // 禁用指向三角形的顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);



    }


}
