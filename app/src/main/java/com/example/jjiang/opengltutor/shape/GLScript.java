package com.example.jjiang.opengltutor.shape;

/**
 * Created by jjiang on 1/26/2017.
 */

public class GLScript {


    public GLScript() {
    }

    public static final String vertex1 = "attribute vec4 mPosition;"  +

    "void main(){"

    + "gl_Position = mPosition;" +
    "}";

    public static final String fragment1 = "precision mediump float;" +
    "uniform vec4 mColor;" +
    "void main() {"
     +   "gl_FragColor = mColor;" +
    "}";

    public static final String vertex2 = "uniform mat4 uMVPMatrix;"+
            "attribute vec3 aPosition;"
    + "attribute vec2 aTexCoor;"
    + "varying vec2 vTextureCoord;"
    + "void main() {"
            + "gl_Position = uMVPMatrix * vec4(aPosition, 1);"
        + "vTextureCoord = aTexCoor;}";

    public static final String fragment2 = "precision mediump float;"
    + "varying vec2 vTextureCoord;"
    + "uniform sampler2D sTexture;"
    + "void main() {"
        + "vec2 coord = vTextureCoord;"
        + "coord.s = coord.s * 0.5;"
        //其实是去图像的一半，向量缩小了
        + "gl_FragColor = texture2D(sTexture, coord);"
    + "}";




    public static final String vertexShaderCode =
            "attribute vec4 aPosition;" +
                    "attribute vec2 aTexCoor;"+
                    "uniform mat4 uMVPMatrix;" +
                    "varying vec2  vTextureCoord;"+
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * aPosition ;" +
                    "   vTextureCoord = aTexCoor;"+
                    "}";

    public static final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D sTexture;" +  // u_Texture
                    "varying vec2 vTextureCoord;" +
                    "void main() {" +
                     "vec2 coord = vTextureCoord;"
                    +
                    "  gl_FragColor = texture2D(sTexture,coord);" +
                    "}";

}




