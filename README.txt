SESSL Sample Experiments
========================

Some sample experiments to get you started with SESSL (http://sessl.org).
All samples are currently executed with JAMES II (http://jamesii.org).

Prerequisites:
==============
1. Java 1.7 (http://jdk7.java.net)
2. Maven 3 (http://maven.apache.org)

How to use from command line:
=============================
1. hg clone https://bitbucket.org/alesia/sessl-samples
2. cd sessl-samples
3. mvn scala:run

This should execute the experiment in src/main/scala/org/sessl/samples/MLRulesExperiment and generate a CSV file with simulation output.

You can run other experiments with the following commands:

"mvn scala:run -Dlauncher=sr" (executes .../samples/SRExperiment)
"mvn scala:run -Dlauncher=optimization" (executes .../samples/OptimizationExperiment)

How to adapt and deploy:
========================

Each experiment is specified in a single file.
For example, src/main/scala/org/sessl/samples/MLRulesExperiment contains the experiment that is executed per default.

You can create zip and tar.gz files that contain all dependencies and thus can be executed on any computer where Java 1.7 is installed.

To do so, execute 

"mvn package"

which generates these files in the directory 'target'.

The scripts deployed to start these experiments (copied from directory _files_to_package) start the default experiment (MLRulesExperiment).
You can let them point to your own experiment, or add your own scripts.

Model location:
===============

Note that model locations should be define as a relative path, using the value 'dir' (defined in the org.sessl.samples package object), e.g.:

model = "file-mlrj:/." + dir + "/EndoExoCytosis.mlrj"

This is currently necessary so that both the deployed run scripts (from _files_to_package) and the launcher invoked by "mvn scala:run" find the model files (located in src/main/resources).
Use the same scheme for other resources, which should be located in src/main/resources as well.