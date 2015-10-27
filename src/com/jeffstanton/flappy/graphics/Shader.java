package com.jeffstanton.flappy.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import com.jeffstanton.flappy.math.Matrix4f;
import com.jeffstanton.flappy.math.Vector3f;
import com.jeffstanton.flappy.util.ShaderUtils;

public class Shader {
	
	public static final int VERTEX_ATTRIB = 0;
	public static final int TEXCOORDINATES_ATTRIB = 1;
	
	public static Shader BACKGROUND, BIRD, PIPE, FADE;
	
	private boolean enabled = false;
	
	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	public Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}  // end constructor
	
	public static void loadAll() {
		BACKGROUND = new Shader("shaders/bg.vert", "shaders/bg.frag");
		BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
		PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
		FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
	}
	
	public int getUniform(String name) {
		if (locationCache.containsKey(name))
			return locationCache.get(name);
		
		int uniformLocation = glGetUniformLocation(ID, name);
		if (uniformLocation == -1)
			System.err.println("Could not find uniform variable '" + name + "'!");
		else
			locationCache.put(name, uniformLocation);
		
		return uniformLocation;
	}  // end getUniform
	
	public void setUniform1i(String name, int value) {
		if(!enabled) enable();
		glUniform1i(getUniform(name), value);
	}  // end setUniform1i
	
	public void setUniform1f(String name, float value) {
		if(!enabled) enable();
		glUniform1f(getUniform(name), value);
	}  // end setUniform1f
	
	public void setUniform2f(String name, float x, float y) {
		if(!enabled) enable();
		glUniform2f(getUniform(name), x, y);
	}  // end setUniform2f
	
	public void setUniform3f(String name, Vector3f vector) {
		if(!enabled) enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}  // end setUniform3f
	
	public void setUniformMat4f(String name, Matrix4f matrix) {
		if(!enabled) enable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}  // end setUniformMat4f
	
	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}  // end enable
	
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}  // end disable

}  // end Shader
