The high-level system design.

# Introduction #

System overview:

![http://self-driving-rccar.googlecode.com/files/system_overview.png](http://self-driving-rccar.googlecode.com/files/system_overview.png)

The Android phone obtains GPS position, accelerometer, and orientation sensor data, and broadcast them using UDP over the local WiFi.

The software in the laptop computes the position and heading estimate from the measurement using the Kalman filter. It also compares the position-heading estimate with the target waypoint position and the intended heading, and compute the required control signal using PID controller.

The control signal (steer, throttle) is sent to the Arduino over USB. The Arduino is connected to the switches inside the remote control. When a switch (e.g. forward motor) is turned on, the corresponding RF signal is sent to the car and thus the required motor actions are executed.

And here's roughly the "functional architecture":

![http://self-driving-rccar.googlecode.com/files/functional_architecture.png](http://self-driving-rccar.googlecode.com/files/functional_architecture.png)

# Components to build #

  * Sensor reading app
  * Interface to the radio controller via Arduino
  * Car physics model (tests)
  * Kalman filter algorithm implementation
  * PID control algorithm implementation
  * Waypoint programming interface