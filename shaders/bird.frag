#version 330 core

layout (location = 0) out vec4 color;

in DATA
{
	vec2 textureCoordinate;
} fs_in;

uniform sampler2D tex;

void main() {
	color = texture(tex, fs_in.textureCoordinate);
	if (color.w < 1.0)
		discard;
}