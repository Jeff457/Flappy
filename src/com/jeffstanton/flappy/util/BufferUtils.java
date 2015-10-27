package com.jeffstanton.flappy.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {
	
	private BufferUtils () {
	}  // private utility class
	
	public static ByteBuffer createByteBuffer (byte[] array) {
		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		result.put(array).flip();
		return result;
	}  // end createByteBuffer
	
	public static FloatBuffer createFloatBuffer (float[] array) {
		FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.put(array).flip();
		return result;
	}  // end createFloatBuffer
	
	public static IntBuffer createIntBuffer (int[] array) {
		IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
		result.put(array).flip();
		return result;
	}  // end createIntBuffer

}  // end BufferUtils
