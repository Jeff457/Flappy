package com.jeffstanton.flappy.level;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

import com.jeffstanton.flappy.graphics.Shader;
import com.jeffstanton.flappy.graphics.Texture;
import com.jeffstanton.flappy.graphics.VertexArray;
import com.jeffstanton.flappy.input.Input;
import com.jeffstanton.flappy.math.Matrix4f;
import com.jeffstanton.flappy.math.Vector3f;

public class Level {
	
	private VertexArray background, fade;
	private Texture bgTexture;
	
	private int xScroll = 0;
	private int map = 0;
	
	private Bird bird;

	private Pipe[] pipes = new Pipe[5 * 2];  // 5 on the top, 5 on the bottom
	private int index = 0;
	private float OFFSET = 5.0f;
	private boolean control = true, reset = false;
	
	private Random random = new Random();
	
	private float time = 0.0f;
	
	
	public Level() {
		float[] vertices = new float[] {
				-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
				-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
				  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
				  0.0f, -10.0f * 9.0f / 16.0f, 0.0f,
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
		
		fade = new VertexArray(6);
		background = new VertexArray(vertices, indices, textureCoordinates);
		bgTexture = new Texture("res/bg.jpeg");
		bird = new Bird();
		
		createPipes();
	}  // end constructor
	
	private void createPipes() {
		Pipe.create();
		for (int i = 0; i < 5 * 2; i += 2) {
			pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
			pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 12.5f);
			index += 2;
		}
	}  // end createPipes
	
	private void updatePipes() {
		pipes[index % 10] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
		pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 12.5f);
		index += 2;
	}  // end updatePipes
	
	public void update() {
		if (control) {
			xScroll--;
			if (-xScroll % 335 == 0) map++;
			if (-xScroll > 250 && -xScroll % 120 == 0) updatePipes();
		}
		
		bird.update(control);
		
		if (control && collision()) {
			bird.fall();
			control = false;
		}
		
		if (!control && Input.isKeyDown(GLFW_KEY_ENTER))
			reset = true;
		
		time += 0.01f;
	}  // end update
	
	private void renderPipes() {
		Shader.PIPE.enable();
		Shader.PIPE.setUniform2f("bird", 0, bird.getY());
		Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
		Pipe.getTexture().bind();
		Pipe.getMesh().bind();
		
		for (int i = 0; i < 5 * 2; i++) {
			Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
			Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
			Pipe.getMesh().draw();
		}
		Pipe.getMesh().unbind();
		Pipe.getTexture().unbind();
	}  // end renderPipes
	
	private boolean collision() {
		for (int i = 0; i < 5 * 2; i++) {
			float birdX = -xScroll * 0.05f;
			float birdY = bird.getY();
			float pipesX = pipes[i].getX();
			float pipesY = pipes[i].getY();
			
			float birdXLeft = birdX - bird.getSize() / 2.0f;
			float birdXRight = birdX + bird.getSize() / 2.0f;
			float birdYTop = birdY - bird.getSize() / 2.0f;
			float birdYBottom = birdY + bird.getSize() / 2.0f;
			
			float pipeXLeft = pipesX;
			float pipeXRight = pipesX + Pipe.getWidth();
			float pipeYTop = pipesY;
			float pipeYBottom = pipesY + Pipe.getHeight();
			
			if (birdXRight > pipeXLeft && birdXLeft < pipeXRight) {
				if (birdYBottom > pipeYTop && birdYTop < pipeYBottom) {
					return true;
				}
			}
		}
		return false;
	}  // end collision
	
	public boolean isGameOver() {
		return reset;
	}  // end gameOver
	
	public void render() {
		bgTexture.bind();
		Shader.BACKGROUND.enable();
		Shader.BACKGROUND.setUniform2f("bird", 0, bird.getY());
		background.bind();
		for (int i = map; i < map + 4; i++) {
			Shader.BACKGROUND.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
			background.draw();
		}
		Shader.BACKGROUND.disable();
		bgTexture.unbind();
		
		renderPipes();
		bird.render();
		
		Shader.FADE.enable();
		Shader.FADE.setUniform1f("time", time);
		fade.render();
		Shader.FADE.disable();
	}  // end render

}  // end Level
