#version 330 core

layout(origin_upper_left) in vec4 gl_FragCoord;

in vec4 pass_dimensions;
in vec4 pass_color;
in float pass_borderRadius;

out vec4 finalColor;

bool isInRange(vec2 pixel, vec4 range) {
	if (pixel.x >= range.x && 
		pixel.x <= range.x + range.z && 
		pixel.y >= range.y - 1 && 
		pixel.y <= range.y + range.w)
		return true;
	return false;
}

void main(void) {
    
	vec2 border = vec2(pass_borderRadius, pass_borderRadius);

	vec4 topLeft = vec4(pass_dimensions.xy, border);
	vec4 topRight = vec4(pass_dimensions.x + pass_dimensions.z - pass_borderRadius, pass_dimensions.y, border);
	vec4 bottomLeft = vec4(pass_dimensions.x, pass_dimensions.y + pass_dimensions.w - pass_borderRadius, border);
	vec4 bottomRight = vec4(pass_dimensions.x + pass_dimensions.z - pass_borderRadius, pass_dimensions.y + pass_dimensions.w - pass_borderRadius, border);

	if (isInRange(gl_FragCoord.xy, topLeft)) {
		if(distance(gl_FragCoord.xy, topLeft.xy + topLeft.zw) > pass_borderRadius)
			discard;
	} else if (isInRange(gl_FragCoord.xy, topRight)) {
		if(distance(gl_FragCoord.xy, topRight.xy + vec2(0, topRight.w)) > pass_borderRadius)
			discard;
	} else if (isInRange(gl_FragCoord.xy, bottomLeft)) {
		if(distance(gl_FragCoord.xy, bottomLeft.xy + vec2(bottomLeft.z, 0)) > pass_borderRadius)
			discard;
	} else if (isInRange(gl_FragCoord.xy, bottomRight)) {
		if(distance(gl_FragCoord.xy, bottomRight.xy) > pass_borderRadius)
			discard;
	}

	finalColor = pass_color;
    
}