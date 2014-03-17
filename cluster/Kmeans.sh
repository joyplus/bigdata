./run-example com.joyplus.cluster.PrepareData "spark://slave3:7077" "hdfs://masterslave1:8020/user/hadoop/oryx/recommend/00000/inbound/20140217.csv" "hdfs://masterslave1:8020/tmp/data2" 10 10

./run-example org.apache.spark.mllib.clustering.KMeans spark://slave3:7077 hdfs://masterslave1:8020/tmp/data2 10 10 > out.log 

tail out.log -n11 |sed -i '$d' > 10centers.txt

./run-example com.joyplus.cluster.SortResult "spark://slave3:7077" "hdfs://masterslave1:8020/user/hadoop/oryx/recommend/00000/inbound/20140217.csv" "/home/hadoop/spark-0.8.0/10centers.txt" "hdfs://masterslave1:8020/tmp/10"

