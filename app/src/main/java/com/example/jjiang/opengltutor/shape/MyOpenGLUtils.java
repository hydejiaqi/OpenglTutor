package com.example.jjiang.opengltutor.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by jjiang on 1/23/2017.
 */

public class MyOpenGLUtils {


    private static final String TAG = "OpenGL Error";


    public static int loadShader(int type, String shaderCode){

        // 创建一个vertex shader类型(GLES20.GL_VERTEX_SHADER)
        // 或fragment shader类型(GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // 将源码添加到shader并编译之
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    public static FloatBuffer getBuffer(float[] coords){

        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes er float)
                 coords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        FloatBuffer buffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        buffer.put(coords);
        // set the buffer to read the first coordinate
        buffer.position(0);

        return buffer;
    }


    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }


    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;	// No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }


    public static int linkGL(){
        int programId = GLES20.glCreateProgram();//创建一个程序
        if (programId == 0) {
            Log.e("OPENGL", "Error Create Link Program");
            return 0;
        }
        return programId;
    }

    public static int linkAttach(int vertexsharder,int fragmentsharder){
        int programId = linkGL();
        GLES20.glAttachShader(programId, vertexsharder); //和着色器进行关联
        GLES20.glAttachShader(programId, fragmentsharder);//和着色器进行关联
        GLES20.glLinkProgram(programId); //把program链接起来
        int status[] = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, status, 0); //这地方一样是检查是否有错误发生。
        Log.d("OPENGL","linkAttach link status is " + GLES20.glGetProgramInfoLog(programId));
        if (status[0] == 0) {
            Log.e("OPENGL","link status is error.");
            GLES20.glDeleteProgram(programId);
            return 0;
        }
        return programId;
    }



}
