#version 330 core

layout(origin_upper_left) in vec4 gl_FragCoord;

in vec4 pass_dimensions;
in vec4 pass_color;
in float pass_borderRadius;
in mat2 rotationTransform;
in vec2 center;

out vec4 finalColor;

bool isInRange(vec2 pixel, vec4 range) {
	if (pixel.x >= range.x - 1 &&
		pixel.x <= range.x + range.z + 1 &&
		pixel.y >= range.y - 1 &&
		pixel.y <= range.y + range.w + 1)
		return true;
	return false;
}

void main(void) {

	vec2 pixel = rotationTransform * (gl_FragCoord.xy - center) + center;
	vec2 border = vec2(pass_borderRadius, pass_borderRadius);
	vec4 topLeft = vec4(pass_dimensions.xy, border);
	vec4 topRight = vec4(pass_dimensions.x + pass_dimensions.z - pass_borderRadius, pass_dimensions.y, border);
	vec4 bottomLeft = vec4(pass_dimensions.x, pass_dimensions.y + pass_dimensions.w - pass_borderRadius, border);
	vec4 bottomRight = vec4(pass_dimensions.x + pass_dimensions.z - pass_borderRadius, pass_dimensions.y + pass_dimensions.w - pass_borderRadius, border);
	float alpha = 1;

	if (isInRange(pixel, topLeft)) {
		float dist = distance(pixel, topLeft.xy + topLeft.zw);
		if(dist > pass_borderRadius + 1)
			discard;
		else
			alpha = 1 - max(dist - pass_borderRadius, 0);
	} else if (isInRange(pixel, topRight)) {
		float dist = distance(pixel, topRight.xy + vec2(0, topRight.w));
		if(dist > pass_borderRadius + 1)
			discard;
		else
			alpha = 1 - max(dist - pass_borderRadius, 0);
	} else if (isInRange(pixel, bottomLeft)) {
		float dist = distance(pixel, bottomLeft.xy + vec2(bottomLeft.z, 0));
		if(dist > pass_borderRadius + 1)
			discard;
		else
			alpha = 1 - max(dist - pass_borderRadius, 0);
	} else if (isInRange(pixel, bottomRight)) {
		float dist = distance(pixel, bottomRight.xy);
		if(dist > pass_borderRadius + 1)
			discard;
		else
			alpha = 1 - max(dist - pass_borderRadius, 0);
	}

	finalColor = vec4(pass_color.rgb,  pass_color.a * alpha);
    
}