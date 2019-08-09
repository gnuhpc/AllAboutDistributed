package org.cmbc.C12

import org.apache.spark.{SparkConf, SparkContext}


object Histogram {
  def main(args: Array[String]) {

    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val line = sc.textFile("/user/tdhclient/family")

    val rdd=line.map(_.split(",")).map(x=>(x(1),x(0))).map(x=>(x._1,1)).sortByKey().reduceByKey(_+_).collect()
    val sum=rdd.foldLeft(0)({(z,f)=>z+f._2})
    rdd.map(x=>println("%s\t%.2f".format(x._1,x._2.toDouble/sum*100)))

    sc.stop()
  }

}
