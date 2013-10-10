#! /bin/bash

motor=$1
query=$3
rep=$2
node=$4 # only for Impala

total=0
parcial=0

case "$motor" in
	"impala")
		motor="impala-shell -i cdh4-node$node -q "
		;;
	"hive")
		motor="hive -e "
		;;
	*)
		echo "ERRROOOOOOOORRRRRRRR"
		echo "ACIEEEEERRRTOOOOOOOO"
		exit
		;;
esac

for i in `seq 1 $rep`;
do
	echo "Repeticion numero $i"
	init=`date +%s`
	time $motor "$query" > /tmp/log 2>&1
	end=`date +%s`
	let "diff = $end - $init"
	let "total += diff"
	echo -e "La ejecucion ha tardado \e[1;31m$diff\e[0;0m segundos."
	let "parcial = total / $i"
	echo -e "Tiempo de ejecucion media hasta el momento \e[1;34m$parcial\e[0;0m segundos."
done

let "total /= $rep"
echo -e "Tiempo de ejecucion media \e[1;34m$total\e[0;0m segundos."	