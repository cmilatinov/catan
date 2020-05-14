#version 330 core

in vec3 pass_pos;

out vec4 color;

uniform samplerCube tex;

void main(void) {
	
	color = texture(tex, pass_pos);
	
}