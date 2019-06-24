package org.cmbc.C12

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
  * 统计字符出现次数
  */
object WordCount {
  def main(args: Array[String]) {

    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val file = sc.textFile("/user/tdhclient/file")

    val rdd = file.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_)
    rdd.collect().foreach(println)
    rdd.saveAsTextFile("/user/tdhclient/out")
    sc.stop()
  }
}
