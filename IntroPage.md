# Introduction #

This project is my attempt at building a small (mini? micro? nano? pico?) version of Google's or Stanford's self-driving car, based on a cheap radio-controlled toy. This work is inspired mainly by Sebastian Thrun and Peter Norvig's free, [online course on artificial intelligence](https://www.ai-class.com/) (autumn 2011) and Thrun's [Udacity CS373](http://www.udacity.com/overview/Course/cs373) Artificial Intelligence for Robotics, a.k.a. Programming a Robotic Car, (spring 2012).

Those two free online classes provide the motivation and the basics for me to start doing something in this area. But the main "trigger" was David Singleton's [neural network RC car project](http://blog.davidsingleton.org/nnrccar). A very impressive project, indeed. So, credits to those people.

My objective is rather simple: make a car that autonomously follows a set of programmed GPS waypoints. It sounds pretty easy but I think it's not. The problem is GPS data are not so accurate and they are noisy. Try getting GPS data from a fixed (not moving) receiver, you'll find the figures "jump" from time to time.

Here's my example. Within one second, the fixed GPS coordinate change from (48.100030N, 11.284338E) to (48.100030N, 11.284332E). A seemingly very tiny change, but it translates to a distance of about 40 cm. And the receiver is not even moving! For a car with a size of 20 cm, this is a lot. The uncertainty in the knowledge of the actual car's position will translate to erratic control signals (see e.g. [this video](http://www.youtube.com/watch?v=VSub2p-j6V8); I suspect no filtering/smoothing is applied there to the GPS data). So some localization techniques like the Kalman or particle filter are needed.

My first milestone is to program the car to drive a straight-line path in a controllable manner, using a smoothened localization data obtained from applying Kalman filter to the GPS measurements, and by applying a PID control algorithm to steer the car to minimize the path error along the path between waypoints.

As a starting point, I defer hacking directly to the car's electronics, and prefer to tap to the remote control instead. Also, for the moment I don't feel it's really necessary to integrate more sensors (e.g. laser or ultrasonic rangefinders) than what an Android smart phone already provides.

Everything is still work in progress. Only the Android app that gathers sensor data and broadcast them over the local wireless network is currently completed.

Many parts of the codes are my adaptations of other people's work:

  * Android code to read sensor data is from [here](http://www.cse.nd.edu/~cpoellab/teaching/cse40815/android_sensors.pdf). What I did was integrating all GPS, accelerometer, and orientation sensor data measurements into one "Activity".
  * The Android code to broadcast sensor data over UDP socket was broadly based on the codes [here](http://code.google.com/p/boxeeremote/).
  * The Arduino sketch to interface with the RC car controller is almost 100% from [David's code](https://github.com/dps/nnrccar).

Many thanks also to bunch of "anonymous people" at stackoverflow... you guys rock!

This work is released under BSD license, so all codes are provided "as is", feel free to use and distribute, but use them at your own risk.