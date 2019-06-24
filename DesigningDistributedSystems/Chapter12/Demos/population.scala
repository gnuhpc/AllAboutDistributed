package org.cmbc.C12

import java.util.concurrent.{Callable, Executors}

import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.JavaConversions._



object population {
  def main(args: Array[String]): Unit = {
    val sparkConf=new SparkConf()
    val sc=new SparkContext(sparkConf)

    //保存任务返回值
    val list=new java.util.ArrayList[String]()
    var sum = 0

    val task_paths=new java.util.ArrayList[String]()
    task_paths.add("/user/tdhclient/usa/town1")
    task_paths.add("/user/tdhclient/usa/town2")
    task_paths.add("/user/tdhclient/usa/town3")

    //线程数等于path的数量
    val nums_threads=task_paths.size()
    //构建线程池
    val executors=Executors.newFixedThreadPool(nums_threads)
    try{
      for (i <- 0 until nums_threads) {
        val task = executors.submit(new Callable[String] {
          override def call(): String = {
            sc.textFile(task_paths.get(i)).collect().foreach(println)
            val count = sc.textFile(task_paths.get(i)).count()
            return count.toString()
          }
        })
        list.add(task.get())
      }
    }finally {
      executors.shutdown()
    }

    //遍历获取结果
    list.foreach(result=>{
      sum=sum+result.toInt
    })

    println("population is : "+sum)

    //停止spark
    sc.stop()

  }
}
