#! /bin/bash

path=/tmp/ficheros/
file=$1
rep=5

total=0
for i in `seq 1 $rep`;
do
	echo "Repeticion HDFS  numero $i"
	hadoop fs -rm -skipTrash $path$file > /dev/null 2>&1
	init=`date +%s`
	time hadoop fs -put /tmp/$file $path$file
	end=`date +%s`
	let "diff = $end - $init"
	let "total += $diff"
	echo -e "La ejecucion ha tardado \e[0;31m$diff\e[0;0m segundos" 
done

let "total /= $rep"
echo -e "Tiempo de ejecucion media \e[1;34m$total\e[0;0m segundos" 
