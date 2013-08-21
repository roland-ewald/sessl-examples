package org.sessl

/**
 * Contains SESSL sample experiments.
 */
package object samples {

  /**
   * Points at the directory where the model files are.
   * For example, this is necessary to properly configure the launch configuration used for "mvn scala:run".
   */
  val dir = sys.props.getOrElse("sample.resources", "")

}