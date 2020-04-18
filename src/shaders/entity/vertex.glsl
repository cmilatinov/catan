#version 330 core

in vec3 pos;

out vec3 pass_pos;

uniform mat4 projViewMatrix;
uniform mat4 modelMatrix;

void main(void) {
	pass_pos = pos;
	gl_Position = projViewMatrix * modelMatrix * vec4(pos, 1.0f);
}