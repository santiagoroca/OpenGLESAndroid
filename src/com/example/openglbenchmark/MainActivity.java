package com.example.openglbenchmark;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private GLSurfaceView mSurfaceView;
    private GLSurfaceView mGLView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setPreserveEGLContextOnPause(true);
        mGLView.setRenderer(new GLES20Renderer(this));
        
        setContentView(mGLView);
    }

    @Override
    public void onResume () {
    	super.onResume();
        if (mSurfaceView != null) {
            mSurfaceView.onResume();
        }
    }
    
    @Override
    public void onPause () {
    	super.onPause();
        if (mSurfaceView != null) {
            mSurfaceView.onPause();
        }
    }
    
}
