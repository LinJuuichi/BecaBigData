#! /bin/bash

path=/user/hive/warehouse/comments/
file=$1
rep=$2

total=0
parcial=0

for i in `seq 1 $rep`;
do
	echo "Repeticion Mahout numero $i"
	init=`date +%s`
	time mahout fpg -i $path$file -o /tmp/mahout -k 25 -method mapreduce -regex '[\ ]' -s 40  > /tmp/log 2>&1
	end=`date +%s`
	let "diff = $end - $init"
	let "total += $diff"
	echo -e "La ejecucion ha tardado \e[0;31m$diff\e[0;0m segundos" 
	let "parcial = total / $i"
	echo -e "Tiempo de ejecucion media hasta el momento  \e[1;34m$parcial\e[0;0m segundos" 
done

let "total /= $rep"
echo -e "Tiempo de ejecucion media \e[1;34m$total\e[0;0m segundos" 
