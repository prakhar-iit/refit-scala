trait Refit {
  //A way to define some Global Variables
  var unigramCount: Map[String, Double] = Map.empty[String, Double]
  var nextWordDistribution: Map[String, Map[String, Double]] = Map.empty[String, Map[String, Double]]
  var totalWordStreamed: Int = 0
  val minimumTrainingWords: Int = 200000
  var curr_word: String = ""
}
