package com.example.jjiang.opengltutor.shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by jjiang on 1/23/2017.
 */

public class MyOpenGLUtils {





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

}
