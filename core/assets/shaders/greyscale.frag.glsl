#version 120

varying vec3 fragColor;
void main()
{
    // Convert color to grayscale by averaging the RGB components
    float grayscale = (fragColor.r + fragColor.g + fragColor.b) / 3.0;
    gl_FragColor = vec4(grayscale, grayscale, grayscale, 1.0);
}
