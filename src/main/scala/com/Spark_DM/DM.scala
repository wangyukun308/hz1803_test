package com.Spark_DM

import com.Constants.Constan
import com.SparkUtils.JDBCUtils
import com.config.ConfigManager
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

/**
  * dwd导入dws
  */
object DM {
  def main(args: Array[String]): Unit = {
    //System.setProperty("hadoop.home.dir", "D:\\Huohu\\下载\\hadoop-common-2.2.0-bin-master")
    //val conf = new SparkConf().setAppName(Constan.SPARK_APP_NAME_USER).setMaster(Constan.SPARK_LOACL)
    //val sc = new SparkContext(conf)
    val sc = SparkSession.builder().enableHiveSupport().appName(Constan.SPARK_APP_NAME_USER).master(Constan.SPARK_LOACL).config("spark.debug.maxToStringFields","100").getOrCreate()
    //val hiveContext = new HiveContext(sc)
    // 加载相应的语句
    val sql = ConfigManager.getProper(args(0))
    if(sql == null){
      LoggerFactory.getLogger("SparkLogger")
        .debug("提交的表名参数有问题！请重新设置。。。")
    }else{
        // 处理SQL内部的占位符
      //val finalSql = sql.replace("?",args(1))
        // 运行SQL
      val df = sc.sql(sql)
      // 处理配置参数
      val mysqlTableName = args(0).split("\\.")(1)
      val hiveTableName = args(0)
      val jdbcProp = JDBCUtils.getJdbcProp()._1
      val jdbcUrl = JDBCUtils.getJdbcProp()._2
      // 存入MySQL

      df.write.mode("append").jdbc(jdbcUrl,mysqlTableName,jdbcProp)
      // 存入Hive
      df.write.mode(SaveMode.Append).insertInto(hiveTableName)
    }
  }
}
