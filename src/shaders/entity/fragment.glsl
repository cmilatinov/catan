#version 330 core

in vec3 pass_pos;
in vec3 pass_normal;
in vec2 pass_uv;

out vec4 color;

const int MAX_LIGHTS = 10;
const float AMBIENT = 0.1;

uniform sampler2D tex;

uniform vec3 lightPositions[MAX_LIGHTS];
uniform vec3 lightColors[MAX_LIGHTS];
uniform int numLights;

uniform vec3 cameraPos;

void main(void) {
	
	vec3 diffuseColor = texture(tex, pass_uv).xyz;
	vec3 lighting = diffuseColor * AMBIENT;
	for(int i = 0; i < numLights; i++){
		
		vec3 lightDir = normalize(lightPositions[i] - pass_pos);
		vec3 norm = normalize(pass_normal);

		lighting += max(dot(lightDir, norm), 0) * diffuseColor * lightColors[i];

	}

	color = vec4(lighting, 1);
	
}