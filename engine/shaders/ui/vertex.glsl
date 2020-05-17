#version 330 core

in vec2 pos;

// Instanced
in mat4 modelMatrix;
in vec4 dimensions;
in vec4 color;
in float borderRadius;

out vec4 pass_dimensions;
out vec4 pass_color;
out float pass_borderRadius;

void main(void) {
	pass_dimensions = dimensions;
	pass_color = color;
	pass_borderRadius = borderRadius;
	gl_Position = modelMatrix * vec4(pos, 0, 1);
}