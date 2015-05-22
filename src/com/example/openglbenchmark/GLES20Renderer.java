package com.example.openglbenchmark;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Shape.Shape;
import Shape.Triangle;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.Display;
import android.view.WindowManager;

public class GLES20Renderer extends GLRenderer {

	private ArrayList<Shape> Shapes = new ArrayList<Shape>();
	
	/** This will be used to pass in the transformation matrix. */
	private int mMVPMatrixHandle;
	 
	/** This will be used to pass in model position information. */
	private int mPositionHandle;
	 
	/** This will be used to pass in model color information. */
	private int mColorHandle;
	
	// Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];
   
    // Our screenresolution
    float   mScreenWidth = 1280;
    float   mScreenHeight = 768;
 
    // Misc
    Context mContext;
    long mLastTime;
    int mProgram;
	
    public GLES20Renderer (Context c) {
    	 mContext = c;
         mLastTime = System.currentTimeMillis() + 100;
         
		 Shapes.add(new Triangle (
			new float [] {
				-0.5f, -0.25f, 0.0f,
	            1.0f, 0.0f, 0.0f, 1.0f,
	 
	            0.5f, -0.25f, 0.0f,
	            0.0f, 0.0f, 1.0f, 1.0f,
	 
	            0.0f, 0.559016994f, 0.0f,
	            0.0f, 1.0f, 0.0f, 1.0f
			}, new short [] {
				0, 1, 2	
			}
		 ).init());
    }
    
    public void onPause() {
    	
    }
 
    public void onResume() {
        mLastTime = System.currentTimeMillis();
    }
    
    @Override
    public void onDrawFrame(GL10 unused) {
        long now = System.currentTimeMillis();
        
        if (mLastTime > now) return;
        
        Render(mtrxProjectionAndView);
        mLastTime = now;
    }
    
    private void Render(float[] m) {
    	
    	for (Shape s : Shapes) {
    		
    		System.out.println ("Render");
    		
    		// Pass in the position information
    	    GLES20.glVertexAttribPointer(
	    		mPositionHandle, 
	    		3, 
	    		GLES20.GL_FLOAT, 
	    		false,
	            7 * 4, 
	            s.getVertexBuffer()
    	    );
    	 
    	    GLES20.glEnableVertexAttribArray(mPositionHandle);
    	 
    	    // Pass in the color information
    	    s.getVertexBuffer().position(3);
    	    GLES20.glVertexAttribPointer(
	    		mColorHandle, 
	    		4, 
	    		GLES20.GL_FLOAT, 
	    		false,
	            7 * 4, 
	            s.getVertexBuffer()
            );
    	 
    	    GLES20.glEnableVertexAttribArray(mColorHandle);
    	 
    	    // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
    	    // (which currently contains model * view).
    	    Matrix.multiplyMM(s.getModelMatrix(), 0, mtrxView, 0, s.getModelMatrix(), 0);
    	 
    	    // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
    	    // (which now contains model * view * projection).
    	    Matrix.multiplyMM(s.getModelMatrix(), 0, mtrxProjection, 0, s.getMVPMatrix(), 0);
    	 
    	    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, s.getMVPMatrix(), 0);
    	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    	}
 
    }	

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
 
        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int)width, (int)height);
        
        Matrix.frustumM(
    		mtrxProjection, 
    		0, 
    		-(float) width / height, //left
    		(float) width / height, //right
    		-1.0f, //bottom
    		1.0f, //top
    		1.0f, //near
    		10.0f //far
        );
        
    }
 
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
 
    	Matrix.setLookAtM(
    		mtrxView, 
    		0, 
    		0.0f, //x pos 
    		0.0f, //y pos
    		1.5f, //z pos
    		0.0f, //x dir 
    		0.0f, //y dir
    		-5.0f, //z dir
    		0.0f, //up x 
    		1.0f, //up y
    		0.0f //up z
    	);
    	
        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
 
        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
         
        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, riGraphicTools.vertexShader);
         
            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);
         
            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
         
            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }
         
        if (vertexShaderHandle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }
        
        // Load in the vertex shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
         
        if (fragmentShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, riGraphicTools.fragmentShader);
         
            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);
         
            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
         
            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(fragmentShaderHandle);
                vertexShaderHandle = 0;
            }
        }
         
        if (fragmentShaderHandle == 0)
        {
            throw new RuntimeException("Error creating fragment shader.");
        }
        
        int programHandle = GLES20.glCreateProgram();
        
        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
         
            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
         
            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
         
            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);
         
            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
         
            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
         
        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }
        
        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
     
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
    }
    
    public void clearBuffers () {
    	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

	@Override
	public void onCreate(int width, int height, boolean contextLost) {
		
	}

	@Override
	public void onDrawFrame(boolean firstDraw) {
		// TODO Auto-generated method stub
		
	}
    
}
