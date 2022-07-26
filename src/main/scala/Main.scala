import org.apache.flink.api.common.functions.{MapFunction, ReduceFunction}
import org.apache.flink.api.common.state.MapState
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.util.Collector

object Main extends Refit {

  def main(args: Array[String])
  {
    val n = 2 // n-grams n = 2: bigrams, n = 3: Trigrams
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val stream = env.socketTextStream("localhost", 50012, '\n').map(_.toLowerCase())
    stream.process(new ProcessFunction[String, String] {
      override def processElement(value: String,
                                  ctx: ProcessFunction[String, String]#Context,
                                  out: Collector[String]): Unit = {
        var wordCount: Double = mp.getOrElse(value, 0.0)
        wordCount = wordCount + 1.0
        mp = mp + (value -> wordCount)
        if(wordCount>1.0){
          println(value)
        }

        out.collect(value)
      }
    }).countWindowAll(n,1).reduce(new ReduceFunction[String] {
      override def reduce(value1: String, value2: String): String = value1 + " " + value2
    }).map((_,1.0)).keyBy(0).sum(1).process(new ProcessFunction[(String, Double), (String, Double)] {
      override def processElement(value: (String, Double),
                                  ctx: ProcessFunction[(String, Double), (String, Double)]#Context,
                                  out: Collector[(String, Double)]): Unit = {
        val unigram = value._1.split(" ").toList.take(n-1).mkString(" ")
        val unigramCount: Double = mp.getOrElse(unigram, 1.0)
        val prob = value._2/unigramCount
        println(prob)
        out.collect(value)
      }
    })

    //val text: DataStream[String]  = env.readTextFile("file:///Users/prakharrastogi/Desktop/refit-scala/src/main/scala/raw_text_1.txt")
    //text.flatMap(_.split(" ")).filter(_!=" ").filter(_!="\n").keyBy().print()
    //text.flatMap{_.toLowerCase().split("\\W+")filter{_.nonEmpty}}.map{(_, 1)}.keyBy(0).sum(1).print()
    //text.map(_.toLowerCase()).map(_.replaceAll("\\W", " ").trim.replaceAll(" +", " ")).flatMap(_.split(" ")).print()
    /*val wordCounts: DataStream[(String, Int)] = text.map(_.toLowerCase()).map(_.replaceAll("\\W", " ").trim.replaceAll(" +", " ")).flatMap(_.split("\\W")).map{(_,1)}.keyBy(_._1).countWindow(2).reduce(new ReduceFunction[(String, Int)] {
      override def reduce(value1: (String, Int), value2: (String, Int)): (String, Int) =    (value1._1, value1._2 + value2._2)
    })
*/
    /*stream.map{(_,1)}.keyBy(_._1).reduce(new ReduceFunction[(String, Int)] {
      override def reduce(value1: (String, Int), value2: (String, Int)): (String, Int) =    (value1._1, value1._2 + value2._2)
    }).print()*/
    //Word Count and Unigrams are same
    //val wordCount = stream.map((_,1)).keyBy(0).sum(1).filter(_._2>1) //Task 2: Print Count of Word if already arrived in the past



    //Unigrams Equivalent to word count
    /*val n_1_grams = stream.countWindowAll(n-1,1).reduce(new ReduceFunction[String] {
      override def reduce(value1: String, value2: String): String = value1 + " " + value2
    }).map((_,1)).keyBy(0).sum(1)
*/
    //Bigrams
    /*val ngrams: DataStream[(String, Double)] = stream.countWindowAll(n,1).reduce(new ReduceFunction[String] {
      override def reduce(value1: String, value2: String): String = value1 + " " + value2
    }).map((_,1.0)).keyBy(0).sum(1).process(new ProcessFunction[(String, Double), (String, Double)] {
      override def processElement(value: (String, Double),
                                  ctx: ProcessFunction[(String, Double), (String, Double)]#Context,
                                  out: Collector[(String, Double)]): Unit = {
        val prob = value._2/mp.get(value._1.split(" ").toList.take(n-1).toString())
        println(prob)
        out.collect(value)
      }
    })
*/
    //ngrams.map{case (k, v) => v/mp.get(k.split(" ")(n-1))}
    /*
    val sumIndex = ngrams.groupBy { case (k, v) => k.take(n - 1) }.mapValues(_.values.sum)

    val ngramWithProbabilityFaster = ngramWithCount.map { case (k, v) =>
      (k, v.toDouble / sumIndex(k.take(n - 1)))
    }

*/
    //Print next 5 words
    /*
    1. Set up a python client to stream words
    2. Take stream of words in scala
    3. Aggregate sum of words and print them if word comes before.
    4. Create a window of n-grams
     */
    /*  val filteredWordCounts = wordCounts.filter(str => str._2>1)
    wordCounts.print()
    filteredWordCounts.print("------")
  */
    /* n-grams
   val n = 3
    text.map(_.toLowerCase()).map(_.replaceAll("\\W", " ").trim.replaceAll(" +", " ")).flatMap(_.split("\\W")).countWindowAll(n, 1).reduce(new ReduceFunction[String] {
      override def reduce(value1: String, value2: String): String = value1 + " " + value2
    }).print()
*/
    /*text.map(_.toLowerCase()).map(_.replaceAll("\\W", " ").trim.replaceAll(" +", " ")).flatMap(_.split("\\W")).map{(_,1)}.keyBy(_._1)*/
    /*  val word = "the bee is the bee of the bees"
    val bigramList: List[List[String]] = word.split("\\W").toList.sliding(2).toList;
    print(bigramList)
  */
    //text.flatMap(value => value.split("\\s+")).map(value => (value,1)).keyBy(0).countWindow(2).sum(1).print()

    //print(data)
    env.execute()

  }
}
