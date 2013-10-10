#! /bin/bash

path=/tmp/ficheros/
output=/tmp/mapreduce-wc
file=$1
rep=1

total=0
for i in `seq 1 $rep`;
do
	echo "Repeticion MapReduce-WC numero $i"
	hadoop fs -rm -r -skipTrash $output-$i
	init=`date +%s`
	#time hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar -numReduceTasks 3 -input $path$file -output $output-$i -mapper cat -reducer wc > /tmp/log 2>&1
	time hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar -input $path$file -output $output-$i -mapper cat -reducer wc  #> /tmp/log 2>&1
	end=`date +%s`
	let "diff = $end - $init"
	let "total += $diff"
	echo -e "La ejecucion ha tardado \e[0;31m$diff\e[0;0m segundos" 
done

let "total /= $rep"
echo -e "Tiempo de ejecucion media \e[1;34m$total\e[0;0m segundos" 
