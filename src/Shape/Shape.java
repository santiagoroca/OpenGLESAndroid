package Shape;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public interface Shape {
	
	public Shape init ();
	
	public float [] getVertices ();
	
	public short [] getIndices ();
	
	public FloatBuffer getVertexBuffer ();
	
	public ShortBuffer getDrawListBuffer ();
	
	public float [] getMVPMatrix ();
	
	public float [] getModelMatrix ();
	
}
