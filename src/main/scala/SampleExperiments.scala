/**
 * *****************************************************************************
 * Copyright 2013 ALeSiA Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ****************************************************************************
 */
import sessl._
import sessl.james._
import sessl.james.tools.CSVFileWriter

/**
 * Some sample SESSL experiments with JAMES II.
 *
 * @author Roland Ewald
 */
object SampleExperiments extends App {

  /**
   * Points at the directory where the model files are.
   * For example, this is necessary to properly configure the launch configuration used for "mvn scala:run".
   */
  val dir = sys.props.getOrElse("sample.resources", "")

  //Simply comment out experiments to ignore: 
  mlrulesExperiment()
  parallelSRExperiment()
  optimizationExperiment()

  /**
   * Simulates ML-Rules model 'EndoExocytosis' until simulation time 0.1 with the reference simulator.
   */
  def mlrulesExperiment() = {
    val modelOutput = CSVFileWriter("./output_EndoExoCytosis.csv")

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

  /**
   * Simulates simple SR sample model in parallel, until simulation time 100.
   */
  def parallelSRExperiment() = {
    val modelOutput = CSVFileWriter("./output_SampleModel.csv")

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

  /**
   * Optimizes the LCS model.
   */
  def optimizationExperiment() = {
    val optOutput = CSVFileWriter("./output_optimization.csv")

    import sessl.optimization._
    import sessl.opt4j._

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

}