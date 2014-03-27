package org.weikey.database

import java.sql.{ResultSet, DriverManager}
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
/**
 * Created with IntelliJ IDEA.
 * User: weikey
 * Date: 14-3-26
 * Time: 下午12:55
 * To change this template use File | Settings | File Templates.
 */
object setProdTag {

  def getTag(): Array[String] = {
    val DBNAME = "hadoop_tmp"
    val DBUSER = "weikey"
    val DBPWD = "mEjWL3nQYUL6KYyj"
    val dbc = "jdbc:mysql://10.6.2.12:3306/" + DBNAME + "?user=" + DBUSER + "&password=" + DBPWD
    val conn2 = DriverManager.getConnection(dbc)
    try {
      val statement2 = conn2.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
      val resultSet2 = statement2.executeQuery("SELECT type_name,tag_id,weigth FROM " +
        "`bg_type_tag` join bg_type on bg_type_tag.`type_id`=bg_type.`type_id`")
      val tagWeigth =  ArrayBuffer[String]()
      while (resultSet2.next()) {
        tagWeigth += resultSet2.getString(1)+","+resultSet2.getInt(2) +","+resultSet2.getFloat(3)
      }
      tagWeigth.toArray
    } finally {
      conn2.close()
    }
  }

  def checkVideoExist(video_id :Int): Boolean = {
    val DBNAME = "hadoop_tmp"
    val DBUSER = "weikey"
    val DBPWD = "mEjWL3nQYUL6KYyj"
    val dbc = "jdbc:mysql://10.6.2.12:3306/" + DBNAME + "?user=" + DBUSER + "&password=" + DBPWD
    val conn2 = DriverManager.getConnection(dbc)

    var b = false
    try {
      val statement2 = conn2.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
      val resultSet2 = statement2.executeQuery("select count(*) as count from bg_video_tag where prod_id="+video_id)

      while(resultSet2.next()){
        if(resultSet2.getInt("count")>0)
          b = true
      }
    } finally {
      conn2.close()
    }
    b
  }




  def main(args: Array[String]) {

    val DBNAME = "hadoop_tmp"
    val DBUSER = "weikey"
    val DBPWD = "mEjWL3nQYUL6KYyj"
    val itemsFile = "/tmp/items.txt"
    val centersFile = "10.txt"
    // create database connection
    val dbc = "jdbc:mysql://10.6.2.12:3306/" + DBNAME + "?user=" + DBUSER + "&password=" + DBPWD
    classOf[com.mysql.jdbc.Driver]
    val conn = DriverManager.getConnection(dbc)
    val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
    val prep = conn.prepareStatement("INSERT INTO bg_video_tag (prod_id,tag_id,weigth) VALUES (?, ?, ?) ")
    // do database insert
    try {
      val tagWeigth=getTag()
      var i = 1
      while (i < 33) {
        val sql = "SELECT d_id,d_name, d_area, d_type_name FROM mac_vod JOIN " +
          "(SELECT prod_id FROM  `bg_kmeans_result`  WHERE  `cluster_id` = "+i+" ORDER BY  " +
          "`value` DESC  LIMIT 20 ) AS prod ON mac_vod.d_id = prod.prod_id"
        val resultSet = statement.executeQuery(sql)
        val video_tag_arr =new Array[Float](10)
        while (resultSet.next()) {
          val video_id=resultSet.getInt("d_id")
          if(!checkVideoExist(video_id)){
          val d_area = resultSet.getString("d_area")
          val d_type_name = resultSet.getString("d_type_name")
          val video_desc=d_area+","+d_type_name
          for(tw<-tagWeigth){
              val t=tw.split(",")
              if(video_desc.contains(t(0))){
                video_tag_arr(t(1).toInt-1)+=t(2).toFloat
              }
          }
          var j=0
          while(j < 10)  {
            prep.setInt(1, video_id)
            prep.setInt(2, j+1)
            prep.setFloat(3,video_tag_arr(j))
            prep.executeUpdate
            video_tag_arr(j)=0
            j+=1
          }
        }
        }
        i += 1
      }
    }
    finally {
      conn.close
    }
  }
}

