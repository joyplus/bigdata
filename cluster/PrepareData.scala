package com.joyplus.cluster

/**
 * Created with IntelliJ IDEA.
 * User: weikey
 * Date: 14-3-17
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */

import org.apache.spark.util.Vector
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import java.util.Random
import java.io._

object PrepareData {

  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.println("Usage: SparkLocalKMeans <master> <input_file> <output_file> <user_filter> <item_filter>")
      System.exit(1)
    }
    val sc = new SparkContext(args(0), "SparkLocalKMeans",
      System.getenv("SPARK_HOME"), Seq(System.getenv("SPARK_EXAMPLES_JAR")))

    val lines = sc.textFile(args(1))
    val tmpData = lines.map {
      line =>
        val vs = line.split(",")
        ((vs(0).toLong, (vs(1).toLong, vs(2).toDouble)), (vs(1).toLong, vs(0).toLong))
    }.cache()
    val userVector = tmpData.map(_._1).groupByKey().filter(_._2.size > args(3)).map {
      u =>
        val itemValueMap = u._2.toMap
        (u._1, itemValueMap)
    }.cache()
    val items = sc.broadcast(tmpData.map(_._2).groupByKey().filter(_._2.size > args(4)).map(_._1).collect())

    val data = userVector.map {
      u =>
        val length = items.value.length
        val values = new Array[Double](items.value.length)
        var i = 0
        for (item <- items.value) {
          val value = u._2.get(item)
          if (!value.isEmpty)
            values(i) = value.get
          else
            values(i) = 0
          i += 1
        }
        //      (u._1, Vector.apply(values))
        values.mkString(" ")
    }

    data.saveAsTextFile(args(2))
  }
}

