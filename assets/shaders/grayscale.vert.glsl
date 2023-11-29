#version 120

attribute vec3 inPosition;
attribute vec3 inColor;
varying vec3 fragColor;
uniform mat4 u_projectionViewMatrix;


void main()
{
    gl_Position = vec4(inPosition, 1.0);
    fragColor = inColor; // Pass the color to the fragment shader
}
