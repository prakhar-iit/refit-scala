import org.apache.flink.api.common.functions.{ReduceFunction}
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.util.Collector

import scala.collection.immutable.ListMap

object Main extends Refit {

  def main(args: Array[String])
  {
    val n = 2 //n-grams n = 2: bigrams, n = 3: Trigrams

    //Create a execution environment for Stream processing using Flink Datastream APIs
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //Taking a stream of words from socket connection in which a streaming-client is setup using python server on port 50012
    val stream = env.socketTextStream("localhost", 50012, '\n').map(_.toLowerCase())

    /**
     * Stream of words -> Create a window of n grams -> process ngrams for predicting next 5 words and counting the words
     */
    stream.countWindowAll(n,1).reduce(new ReduceFunction[String] {
      override def reduce(value1: String, value2: String): String = value1 + " " + value2
    }).map((_,1.0)).keyBy(0).sum(1).process(new ProcessFunction[(String, Double), (String, Double)] {
      override def processElement(value: (String, Double),
                                  ctx: ProcessFunction[(String, Double), (String, Double)]#Context,
                                  out: Collector[(String, Double)]): Unit = {
        var nGram = value._1.split(" ").toList
        val nMinus1Gram = nGram.take(n-1).mkString(" ") //Find n-1 gram
        curr_word = nGram.takeRight(1).mkString //Find current word
        var wordCount = countWords(curr_word)
        if(n==2){
          trainingByBigram(nMinus1Gram, value._2)
          if(totalWordStreamed>minimumTrainingWords && wordCount>1){
            var predictedWords = predict_next_5_words()
            val token = predictedWords.head
            println("Token is " + token, "   Count is " + wordCount, "   Next 5 words are " + predictedWords.takeRight(5))
          }
        }/*else {
          //Generalise it later
        }*/
        out.collect(value)
      }
    })
    env.execute()
  }

  /**
   * A function to predict the next word of a given word
   * @param word
   * @return word or null if that word doesn't occur before in our training dataset
   */
  def predict_next_word(word: String): String = {
    var predictedNextWords: Map[String, Double] = Map.empty[String, Double]
    predictedNextWords = nextWordDistribution.getOrElse(word, null)
    if(predictedNextWords!=null){
      val sortedMap = ListMap(predictedNextWords.toSeq.sortWith(_._2 > _._2):_*)
      return sortedMap.keys.head
    }
    return null;
  }

  /**
   * Method to predict next 5 words using Bigram Probability Distribution
   * @return
   */
  def predict_next_5_words(): List[String] ={
    val predWord_1 = predict_next_word(curr_word)
    val predWord_2 = predict_next_word(predWord_1)
    val predWord_3 = predict_next_word(predWord_2)
    val predWord_4 = predict_next_word(predWord_3)
    val predWord_5 = predict_next_word(predWord_4)
    return List(curr_word, predWord_1, predWord_2, predWord_3, predWord_4, predWord_5)
  }

  /**
   * Method to count the words of each type. This is equivalent to unigram Count.
   * @param value
   */
  def countWords(value: String): Double = {
    totalWordStreamed = totalWordStreamed+1;
    var wordCount: Double = unigramCount.getOrElse(value, 0.0)
    wordCount = wordCount + 1.0
    unigramCount = unigramCount + (value -> wordCount)
    return wordCount
  }

  /**
   * Calculation of Probability Distribution of each ngram
   * @param nMinus1Gram
   * @param value
   */
  def trainingByBigram(nMinus1Gram: String, value: Double): Unit = {
    val unigramCountVar: Double = unigramCount.getOrElse(nMinus1Gram, 1.0)
    val prob = value/unigramCountVar
    var probableNextWordsDistribution = nextWordDistribution.getOrElse(nMinus1Gram, Map.empty[String, Double])
    probableNextWordsDistribution = probableNextWordsDistribution + (curr_word -> prob)
    nextWordDistribution = nextWordDistribution + (nMinus1Gram ->probableNextWordsDistribution)
  }
}
