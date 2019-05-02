# Project-AI
project AI

Some problems have complicated solutions where the calculations for the most optimized solution can take a very long time. In this project we will be creating an application for such a problem. In this problem people can request a rental car. It is our job to place these cars in a city that is divided in multiple zones to optimize the profit, and thus lower the cost. We have to account for the amount of available cars, their type, the location and timing of the requests. For example, a person can make a request for a vehicle at a certain time for which we have to provide that car in that zone or neighbouring zone. That same car can then be used in the same location for another request at a later time.

When we try to create a 'normal' solution for such a problem we can quickly notice the complexity that makes the calculation time very big. To find a suitable solution in a more practical way we will use a heuristic method, namely a meta-heuristic method called simulated annealing.

The goal of this project is to create an algorithm that can find a suitable and correct solution in a short timespan (5 minutes) in which we have to minimize the cost. To make this happen we have to make sure we spend enough time planning about optimization, the algorithm and efficient functions to be able to run as many iterations as possible. We will also have to find the most optimal settings in which to run this algorithm.

Read Labo-pdf in englisch or dutch for further explanation.

How to run jar: java -jar input output seconds seed #threads
example: java -jar SimulatedAnnealing.jar examples/210_5_44_25.csv solution.csv 300 10 4
