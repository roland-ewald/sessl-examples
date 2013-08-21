package org.sessl.samples

/**
 * Sample experiment for simulation-based optimization.
 */
object OptimizationExperiment extends App {

  import sessl._
  import sessl.optimization._

  import sessl.james._
  import sessl.opt4j._

  val optOutput = sessl.james.tools.CSVFileWriter("./output_optimization.csv")

  maximize { (params, objective) => // Maximize the following function
    execute {
      new Experiment with Observation with ParallelExecution {
        model = "java://examples.sr.LinearChainSystem"
        set("propensity" <~ params("p")) //Set model parameters as defined by optimizer
        set("numOfInitialParticles" <~ params("n"))
        stopTime = 1.0
        replications = 10
        observe("S0", "S1")
        observeAt(0.8)
        withReplicationsResult(results => {
          objective <~ results.mean("S0") //Store value of objective function
        })
      }
    }
  } using (new Opt4JSetup {
    param("p", 1, 1, 15) // Optimization parameter bounds
    param("n", 10000, 100, 15000)
    optimizer = sessl.opt4j.SimulatedAnnealing(iterations = 20) //Configure optimizer
    // showViewer = true //Switches on Opt4J GUI
    withIterationResults { results =>
      optOutput << results
    }
    withOptimizationResults { results =>
      println("Overall results: " + results(0)) //print results
    }
  })

}