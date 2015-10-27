package com.jeffstanton.flappy.math;

import static java.lang.Math.*;

import java.nio.FloatBuffer;

import com.jeffstanton.flappy.util.BufferUtils;

public class Matrix4f {
	
	public static final int SIZE = 4 * 4;
	public float[] matrix = new float[SIZE];  // 4x4 matrix
	
	public Matrix4f() {
		
	}
	
	// Create identity matrix
	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();
		
		for (int i = 0; i < SIZE; i++) {
			result.matrix[i] = 0.0f;
		}
		
		result.matrix[0 + 0 * 4] = 1.0f;  // Row + Column * width of Matrix
		result.matrix[1 + 1 * 4] = 1.0f;  
		result.matrix[2 + 2 * 4] = 1.0f;  
		result.matrix[3 + 3 * 4] = 1.0f;  

		return result;
	}  // end identity
	
	public Matrix4f multiply (Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		
		// Go through each element in the rows and columns
		// Multiply them together and add to the overall sum of resulting element
		for (int y = 0; y < 4; y++) { 
			for (int x = 0; x < 4; x++) {
				float sum = 0.0f;
				for (int e = 0; e < 4; e++) {
					sum += this.matrix[ x + e * 4 ] * matrix.matrix[ e + y * 4 ];
				}  // end nested for
				result.matrix[ x + y * 4 ] = sum;
			}  // end nested for
		}  // end for
		return result;
	}  // end multiply
	
	public static Matrix4f translate (Vector3f vector) {
		Matrix4f result = identity();
		
		result.matrix[ 0 + 3 * 4 ] = vector.x;
		result.matrix[ 1 + 3 * 4 ] = vector.y;
		result.matrix[ 2 + 3 * 4 ] = vector.z;
		
		return result;
	}  // end translate
	
	
	// Rotation around z-axis
	public static Matrix4f rotate (float angle) {
		Matrix4f result = identity();
		float r = (float) toRadians(angle);
		float cos = (float) cos(r);
		float sin = (float) sin(r);
		
		result.matrix[ 0 + 0 * 4 ] = cos;
		result.matrix[ 1 + 0 * 4 ] = sin;
		
		result.matrix[ 0 + 1 * 4 ] = -sin;
		result.matrix[ 1 + 1 * 4 ] = cos;
		
		return result;
	}  // end rotate
	
	// How objects will appear in-game
	// @param left, right, top, bottom - only render inside these margins
	// @param near, far - distance with which objects are rendered
	public static Matrix4f orthographic (float left, float right, float bottom, float top, float near, float far) {
		Matrix4f result = identity();
		
		result.matrix[ 0 + 0 * 4 ] = 2.0f / (right - left);
		
		result.matrix[ 1 + 1 * 4 ] = 2.0f / (top - bottom);
		
		result.matrix[ 2 + 2 * 4 ] = 2.0f / (near - far);
		
		result.matrix[ 0 + 3 * 4 ] = (left + right) / (left - right);
		result.matrix[ 1 + 3 * 4 ] = (bottom + top) / (bottom - top);
		result.matrix[ 2 + 3 * 4 ] = (far + near) / (far - near);
		
		return result;
	}  // end orthographic
	
	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(matrix);
	}
	
}  // end Matrix4f
