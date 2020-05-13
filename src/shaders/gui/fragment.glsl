#version 330 core

in vec2 pass_uv;

out vec4 color;

uniform sampler2D tex;

void main(void) {
	
	color = texture(tex, pass_uv);
	
}