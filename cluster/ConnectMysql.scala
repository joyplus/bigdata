package org.weikey.database

/**
 * Created with IntelliJ IDEA.
 * User: weikey
 * Date: 14-3-17
 * Time: 下午6:10
 * To change this template use File | Settings | File Templates.
 */

import java.sql.{Connection, DriverManager, ResultSet};

object connectMysql {
  def main(args: Array[String]) {

    val DBNAME = "hadoop_tmp"
    val DBUSER = "weikey"
    val DBPWD = "mEjWL3nQYUL6KYyj"
    val itemsFile="/tmp/items.txt"
    val centersFile="10.txt"
    // create database connection
    val dbc = "jdbc:mysql://10.6.2.12:3306/" + DBNAME + "?user=" + DBUSER + "&password=" + DBPWD
    classOf[com.mysql.jdbc.Driver]
    val conn = DriverManager.getConnection(dbc)
    //val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
    // do database insert
    try {
      val prep = conn.prepareStatement("INSERT INTO kmeans_result (prod_id, value,cluster_id) VALUES (?, ?, ?) ")
      var i=0
      val items=scala.io.Source.fromFile(itemsFile).mkString.split(" ")
      for (line <-scala.io.Source.fromFile(centersFile).getLines()){
         i+=1
         for(value<-line.split(" ")){
           prep.setInt(1, items(i).toInt)
           prep.setDouble(2, value.toDouble)
           prep.setInt(3, i+1)
           prep.executeUpdate
         }
       }

    }
    finally {
      conn.close
    }
  }
}

