#version 330 core

in vec3 pos;
in vec3 normal;
in vec2 uv;

out vec3 pass_pos;
out vec3 pass_normal;
out vec2 pass_uv;

uniform mat4 projViewMatrix;
uniform mat4 modelMatrix;

void main(void) {
	pass_pos = (modelMatrix * vec4(pos, 1)).xyz;
	pass_normal = (modelMatrix * vec4(normal, 0)).xyz;
	pass_uv = uv;
	gl_Position = projViewMatrix * modelMatrix * vec4(pos, 1.0f);
}