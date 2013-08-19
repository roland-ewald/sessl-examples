/**
 * Necessary because 'mvn scala:run' will execute with the classpath for tests, 
 * and JAMES II will throw an exception during start-up if a directory on the class-path is missing.
 */
object Stub