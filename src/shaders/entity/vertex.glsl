#version 330 core

in vec3 pos;
in vec3 normal;
in vec2 uv;

uniform mat4 projViewMatrix;
uniform mat4 modelMatrix;

void main(void) {
	gl_Position = vec4(pos, 1.0f);
}