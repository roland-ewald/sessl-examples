SESSL Sample Experiments
========================

Some sample experiments to get you started with SESSL (http://sessl.org).
All experiments are currently executed with JAMES II (http://jamesii.org).

Prerequisites:
==============
1. Java 1.7 (http://jdk7.java.net)
2. Maven 3 (http://maven.apache.org)

How to use from command line:
=============================
1. hg clone https://bitbucket.org/alesia/sessl-samples
2. cd sessl-samples
3. mvn scala:run

This should execute the experiments and generate several .csv files with simulation and optimization output.

The experiments are specified in a single file: src/main/scala/SampleExperiments.