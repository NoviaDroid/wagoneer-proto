#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_threshold;
uniform float u_smoothing;

varying vec4 v_color;
varying vec2 v_texCoord;


void main() {
    float distance = texture2D(u_texture, v_texCoord).r;
    float alpha = smoothstep(u_threshold - u_smoothing, u_threshold + u_smoothing, distance);
    gl_FragColor = vec4(v_color.rgb, alpha);
}

