#version 330 core

in vec3 pos;
in vec2 uv;

out vec2 pass_uv;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

void main(void) {
	pass_uv = uv;
	gl_Position = projectionMatrix * modelMatrix * vec4(pos, 1);
}