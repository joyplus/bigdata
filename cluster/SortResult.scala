package com.joyplus.cluster

/**
 * Created with IntelliJ IDEA.
 * User: weikey
 * Date: 14-3-17
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */

import org.apache.spark.util.Vector
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import java.util.Random
import java.io._

object SortResult {

  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("Usage: SparkLocalKMeans <master> <input_file1> <input_file2> <output_file>")
      System.exit(1)
    }
    val sc = new SparkContext(args(0), "SparkLocalKMeans",
      System.getenv("SPARK_HOME"), Seq(System.getenv("SPARK_EXAMPLES_JAR")))

    val lines = sc.textFile(args(1))
    val kPoints = sc.textFile(args(2))

    val items = lines.map {
      line =>
        val vs = line.split(",")
        (vs(1).toLong, vs(0).toLong)
    }.groupByKey().filter(_._2.size > 10).map(_._1).toArray

    kPoints.first.split(' ').size
    items.size

    kPoints.map(_.split(' ').map(_.toDouble).zip(items).sortBy(-_._1).toList).saveAsTextFile(args(3))
  }
}


