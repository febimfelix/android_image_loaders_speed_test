# android_image_loaders_speed_test
An Android project that tests 5 major image loader library speeds while loading images from server.


Here i used Glide, Picasso, Vollet, Fresco and OKHttp libraries inorder to check the image downloading and caching capability of these libraries.
The stopwatch(Chronometer) used to calculate the time taken works on Activity lifecycle basis. ie, upon user back press .
