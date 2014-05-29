#!/bin/sh
T_FILE=$(dirname $(readlink -f $0))


function scandir(){
	for filename in `ls $1`
             do
                if [ -d $1'/'$filename ] ; then
                   scandir $1'/'$filename
                else
		   cd $1
                      ftype=`file -b "$filename"`
                      case  "$ftype"  in 
			"gzip compressed"*)
                        	echo $filename'   is gzip' ;;
                      	*) 
	           		tar -zcvf $filename.tar.gz $filename
                   		rm -rf $filename
                   		echo  $1'/'$filename'  is remove  ';;
	              esac
                fi
             done 
}
args=`cat "$T_FILE"/conf.json | "$T_FILE"/jq '.sync_dir' | sed 's/^"//g' | sed 's/\"//g'`
scandir $args

cd $T_FILE
./qrsync conf.json
