**Project Status**
My project currently has an hourly report system for a simulated number of cars coming into, and then leaving, each cluster of lanes (north, south, east and west). It includes algorithms to closely model real world data, using a distribution model to generate cars,a light modeling algorithm to time lights and a queueing system to maintain balanced lanes based on length. It also has a few data collectors, namely the number of cars coming and going, the total number of cars passing through any direction, the average acceleration of each lane, the average “cars per hour” calculation, and the green split display for each ‘hour’.

Future implementation will see my project converted to multithreading, allowing it to move away from an hourly report system and instead have a real time simulation generating and passing cars through it. I will also implement the webster formula for better light handling, accident event generation to model emergency vehicles, a handling of the left lane queue, and more in depth data collectors, namely critical flow values, bottlenecking results, and optimal light split reporting.

A few changes have been made in regards to my original proposal, namely in the use of more foundational math models, queuing and light systems grounded in real world traffic science and behavioral modeling, and real world information in regards to traffic data at an actual intersection. This means that my model is specifically looking at a busy intersection local to me, as it has the most data points that I can model after, allowing for more realistic numbers and vehicle calculations to simulate.

**Installation Instructions**
To run my model, you will need an IDE capable of running Java 24.0.2 with Maven architecture. The only dependency used in my project comes in the form of the Apache Common Math library, as its implementation of statistical models, specifically its Poisson Distribution, was much more accurate than my original algorithm.

To setup:
Install the “src” directory and XML file from my github.
Open in your preferred IDE, make sure all files have been loaded and compiled properly.
Run the main class file to begin the simulation.

Troubleshooting common issues:
An issue you may find comes in the form of a dependency not found. Simply refresh the xml file after properly copying or downloading my supplied xml file. Once refreshed, the dependency should be found and all files should be properly working.

**Usage**
After loading the source directory and xml file into your given IDE, simply run the main class file. It will prompt you for an hour input (0-23). This corresponds with the real world time, where 0 is 12 am and 23 is 11pm. Similarly, it will also prompt you for an end time. This end time should be after the input time. If you only want to look at 1 hour, simply input the same number twice.

The expected output currently will be an hourly report of cars arriving, then cars leaving. It will do this for each hour, supplying some data points throughout. If any cars remain in the lane at the end of each hour, they will carry onto the next hour.

**Architecture Overview**
The main components in my model come in the form of the lane, vehicle and intersection class. The lane class manages queuing and dequeuing, simulating cars arriving and going through an intersection. It also handles vehicle generation. The vehicle class handles the logistics behind vehicles, namely accounting for various sizes and speeds. Its main function beyond the vehicles is calculating how fast vehicles can proceed through an intersection. Finally, the intersection class builds the intersection itself. It manages light timing and the dimensions, with goals to implement more adaptive cycles. Finally, the sieve class will be the full data collection class, processing all information gathered by the other classes, and distilling it into traffic statistics.

My project design has changed a fair bit from my UML design. Most notably, the controller class has been completely removed. Instead, most of the logic happens between the intersection and lane classes. The lane class also no longer extends the intersection class. Otherwise the structures of the classes are the same, namely that lane objects still contain a queue system, the vehicle class still has all the same attributes. The largest change from UML to implementation comes in the form of the light system. When designing the UML, I assumed a boolean system for green and red lights would be used. However instead a more mathematical approach has been implemented, taking the calculated time for each green light and using vehicles speed and the intersections dimensions in order to determine if they will successfully make the green light.


