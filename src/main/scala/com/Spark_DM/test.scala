package com.Spark_DM

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object test {
  def main(args: Array[String]): Unit = {

   val conf =  new SparkConf().setAppName("t").setMaster("local")
    val sc = new SparkContext(conf)
    val sQLContext = new HiveContext(sc)

    sQLContext.sql("show databases").show()
  }

}
