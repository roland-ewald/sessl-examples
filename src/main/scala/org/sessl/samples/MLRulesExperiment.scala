package org.sessl.samples

/**
 * Sample experiment for ML-Rules.
 */
object MLRulesExperiment extends App {

  import sessl._
  import sessl.james._

  val modelOutput = sessl.james.tools.CSVFileWriter("./output_EndoExoCytosis.csv")

  execute {
    new Experiment with Observation {
      model = "file-mlrj:/." + dir + "/EndoExoCytosis.mlrj"
      simulator = MLRulesReference() //Leave out if specific simulator implementation does not matter
      stopTime = 0.1
      observe("A", "B") //Species A and B are observed...	        
      observeAt(range(0.0, 0.001, 0.095)) //... in interval [0,0.95], with step size 0.001 
      withRunResult { results =>
        modelOutput << results.trajectory("B") //Store results to file          
      }
    }
  }
}