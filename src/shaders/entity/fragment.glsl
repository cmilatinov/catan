#version 330 core

in vec3 pass_pos;

out vec4 fragColor;

void main(void) {
	fragColor = vec4(pass_pos, 1);
}