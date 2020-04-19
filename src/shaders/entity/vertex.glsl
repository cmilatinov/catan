#version 330 core

in vec3 pos;
in vec3 normal;
in vec2 uv;

out vec2 pass_UV;

uniform mat4 projViewMatrix;
uniform mat4 modelMatrix;

void main(void) {
	pass_UV = uv;
	gl_Position = projViewMatrix * modelMatrix * vec4(pos, 1.0f);
}