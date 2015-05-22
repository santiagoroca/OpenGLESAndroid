package Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.Matrix;

public class Triangle implements Shape{

    private float vertices[];
    private short indices[];
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private float[] mMVPMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
	
    public Triangle (float vertices [], short indices []) {
    	this.vertices = vertices;
        this.indices = indices;
    }
    
	public Shape init()
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
 
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
        
        Matrix.setIdentityM(mModelMatrix, 0);
        
        return this;
    }

	@Override
	public float [] getVertices() {
		return this.vertices;
	}

	@Override
	public short [] getIndices() {
		return this.indices;
	}

	@Override
	public FloatBuffer getVertexBuffer() {
		return this.vertexBuffer;
	}

	@Override
	public ShortBuffer getDrawListBuffer() {
		return this.drawListBuffer;
	}

	@Override
	public float[] getMVPMatrix() {
		return this.mMVPMatrix;
	}

	@Override
	public float[] getModelMatrix() {
		return this.mModelMatrix;
	}
	
}
