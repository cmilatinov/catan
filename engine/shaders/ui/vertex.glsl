#version 330 core

in vec2 pos;

// Instanced
in mat4 modelMatrix;
in vec4 dimensions;
in vec4 color;
in float borderRadius;
in float rotation;

out vec4 pass_dimensions;
out vec4 pass_color;
out float pass_borderRadius;
out mat2 rotationTransform;
out vec2 center;

mat2 rotationMatrix(float degrees) {
	return mat2(
		cos(radians(degrees)), sin(radians(degrees)),
		-sin(radians(degrees)), cos(radians(degrees))
	);
}

void main(void) {

	pass_dimensions = dimensions;
	pass_color = color;
	pass_borderRadius = borderRadius;
	rotationTransform = rotationMatrix(rotation);
	center = vec2(dimensions.x + dimensions.z / 2, dimensions.y + dimensions.w / 2);

	gl_Position = modelMatrix * vec4(pos, 0, 1);

}