#version 330 core

layout (location = 0) out vec4 color;

in DATA
{
	vec2 textureCoordinate;
	vec3 position;
} fs_in;

uniform vec2 bird;
uniform sampler2D tex;
uniform int top;

void main() 
{
	vec2 myTC = vec2(fs_in.textureCoordinate.x, fs_in.textureCoordinate.y);
	if (top == 1)
		myTC.y = 1 - myTC.y;
		
	color = texture(tex, fs_in.textureCoordinate);
	if (color.w < 1.0)
		discard;
	
	color *= 3.0 / (length(bird - fs_in.position.xy) + 1.5) + 0.6;
	color.w = 1;
}