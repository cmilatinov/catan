#version 330 core

const int IMAGE_FORMAT_RGBA = 0;
const int IMAGE_FORMAT_ARGB = 1;

in vec2 pass_uv;

out vec4 color;

uniform sampler2D tex;
uniform int textureFormat;

void main() {
    switch(textureFormat) {
        case IMAGE_FORMAT_RGBA:
            color = texture(tex, pass_uv);
            break;
        case IMAGE_FORMAT_ARGB:
            color = texture(tex, pass_uv).bgra;
            break;
    }
}
