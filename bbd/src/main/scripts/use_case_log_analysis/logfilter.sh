#! /bin/bash

rep=$1

for i in `seq 1 $rep`;
do
	echo "Repeticion numero $i"
	init=`date +%s`
	hadoop fs -rm -r -skipTrash /user/cloudera/logfilter/output
	hadoop jar LogFilterMapReduce.jar com.everis.mapreduce.LogFilter -r 1 /tmp/logs_pol2 /user/cloudera/logfilter/output > /tmp/log 2>&1
	end=`date +%s`
	let "diff = $end - $init"
	let "total += $diff"
	echo -e "La ejecucion ha tardado \e[0;31m$diff\e[0;0m segundos" 
	let "parcial = total / $i"
	echo -e "Tiempo de ejecucion media hasta el momento  \e[1;34m$parcial\e[0;0m segundos" 
done

let "total /= $rep"
echo -e "Tiempo de ejecucion media \e[1;34m$total\e[0;0m segundos" 
