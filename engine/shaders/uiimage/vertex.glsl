#version 330 core

in vec2 pos;
in vec2 uv;

out vec2 pass_uv;

uniform mat4 modelMatrix;

void main(void) {
    pass_uv = uv;
    gl_Position = modelMatrix * vec4(pos, 0, 1);
}