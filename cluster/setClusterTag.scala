package org.weikey.database

/**
 * Created with IntelliJ IDEA.
 * User: weikey
 * Date: 14-3-24
 * Time: 下午3:42
 * To change this template use File | Settings | File Templates.
 */

import java.sql.{Connection, DriverManager, ResultSet}
;
import scala.collection.mutable.Map
import scala.collection.mutable.ArrayBuffer

object setClusterTag {

  def main(args: Array[String]) {

    val DBNAME = "hadoop_tmp"
    val DBUSER = "weikey"
    val DBPWD = "mEjWL3nQYUL6KYyj"
    // create database connection
    val dbc = "jdbc:mysql://10.6.2.12:3306/" + DBNAME + "?user=" + DBUSER + "&password=" + DBPWD
    classOf[com.mysql.jdbc.Driver]
    val conn = DriverManager.getConnection(dbc)
    val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
    val prep = conn.prepareStatement("INSERT INTO bg_cluster_class (cluster_id,class_id,tag_id) VALUES (?, ?, ?) ")
    // do database insert
    try {
      var i = 1
      while (i < 33) {
        val sql = "select result2.class_id,result2.tag_id,max(weigth_sum) from " +
          "(select result.class_id,result.tag_id,sum(weigth) as weigth_sum from " +
          "(select prod.prod_id,bvt.tag_id,bt.tag_name,bvt.weigth,bc.class_id,bc.class_name " +
          " from (select `prod_id` from bg_kmeans_result where cluster_id="+i+" order" +
          " by value desc limit 20) as prod join bg_video_tag as bvt on prod.prod_id=bvt.prod_id " +
          "join bg_class_tag as bct on  bvt.tag_id=bct.tag_id join bg_tag as bt on " +
          "bvt.tag_id=bt.tag_id join bg_class as bc on bct.class_id = bc.class_id) as result" +
          " group by result.class_id,result.tag_id order by weigth_sum desc) as result2 group by " +
          "result2.class_id"
        val resultSet = statement.executeQuery(sql)

        while (resultSet.next()) {
          val class_id = resultSet.getInt("class_id")
          val tag_id = resultSet.getInt("tag_id")
          prep.setInt(1, i)
          prep.setInt(2, class_id)
          prep.setInt(3, tag_id)
          prep.executeUpdate
        }
        i += 1
      }
    } catch {
      case e => e.printStackTrace
    }

    finally {
      conn.close
    }
  }
}

