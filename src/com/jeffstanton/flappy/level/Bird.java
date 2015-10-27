package com.jeffstanton.flappy.level;

import static org.lwjgl.glfw.GLFW.*;

import com.jeffstanton.flappy.graphics.Shader;
import com.jeffstanton.flappy.graphics.Texture;
import com.jeffstanton.flappy.graphics.VertexArray;
import com.jeffstanton.flappy.input.Input;
import com.jeffstanton.flappy.math.Matrix4f;
import com.jeffstanton.flappy.math.Vector3f;

public class Bird {
	
	private float SIZE = 1.0f;
	private VertexArray mesh;
	private Texture texture;
	
	private Vector3f position = new Vector3f();
	private float rotation;
	private float delta = 0.0f;
	
	public Bird() {
		float[] vertices = new float[] {
				-SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
				-SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
				 SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
				 SIZE / 2.0f, -SIZE / 2.0f, 0.2f
		};
		
		byte[] indices = new byte[] {
				0, 1, 2,
				2, 3, 0
		};
		
		float[] textureCoordinates = new float[] {
				0, 1,
				0, 0,
				1, 0, 
				1, 1
		};
		
		mesh = new VertexArray(vertices, indices, textureCoordinates);
		texture = new Texture("res/bird.png");
	}  // end constructor
	
	public void update(boolean control) {
		position.y -= delta;
		if(Input.isKeyDown(GLFW_KEY_SPACE) && control)
			delta = -0.15f;
		else
			delta += 0.01f;
		
		rotation = -delta * 90.0f;
	}  // end update

	public void fall() {
		delta = 0.15f;
	}
	
	public void render() {
		Shader.BIRD.enable();
		Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation)));
		texture.bind();
		mesh.render();
		Shader.BIRD.disable();
	}   // end render

	public float getY() {
		return position.y;
	}  // end getY
	
	public float getSize() {
		return SIZE;
	}  // end getSize

}  // end Bird
