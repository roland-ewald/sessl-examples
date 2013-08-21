package org.sessl.samples

/**
 * Sample experiment for chemical reaction networks.
 */
object SRExperiment extends App {

  import sessl._
  import sessl.james._

  val modelOutput = sessl.james.tools.CSVFileWriter("./output_SampleModel.csv")

  execute {
    new Experiment with Observation with ParallelExecution {
      model = "file-sr:/." + dir + "/SampleModel.sr"
      scan("Translation" <~ range(5.0, 1.0, 15.0)) // Scan parameter 'Translation' for values in [5.0, 10.0], step size 1.0
      replications = 2 //Simulate each parameter configuration two times
      stopTime = 100
      observe("P2") //Species P2 is observed...
      observeAt(range(0, 1, 99)) //... at times 0,1,2, ... 99
      withRunResult { results =>
        modelOutput << results.values("P2") //Only the *amounts* of P2 (without time stamps) are written
      }
      parallelThreads = -1 //Leave one core idle
    }
  }

}