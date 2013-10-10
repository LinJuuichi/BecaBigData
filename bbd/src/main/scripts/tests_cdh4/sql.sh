#! /bin/bash

table=$1

table2=comments1024
impalaMaster=2

echo "Consulta 1 con Impala (COUNT)"
#./query.sh impala 5 "select count(*) from $table where source = 'Twitter for iPhone'" $impalaMaster

echo "Consulta 1 con Hive (COUNT)"
#./query.sh hive 5 "select count(*) from $table where source = 'Twitter for iPhone'" 

echo "Consulta 2 con Impala (GROUP BY)"
./query.sh impala 5 "select query, count(*) from $table group by query" $impalaMaster

echo "Consulta 2 con Hive (GROUP BY)"
#./query.sh hive 5 "select query, count(*) from $table group by query" 

echo "Consulta 3 con Impala (ORDER BY)"
./query.sh impala 5 "select query, count(*) as contador from $table group by query order by contador desc limit 4" $impalaMaster

echo "Consulta 3 con Hive (ORDER BY)"
#./query.sh hive 5 "select query, count(*) as contador from $table group by query order by contador desc" 

echo "Consulta 4 con Impala (JOIN)"
./query.sh impala 5 "select count(*) from $table a join $table2 b on a.id = b.id" $impalaMaster

echo "Consulta 4 con Hive (JOIN)"
#./query.sh hive 5 "select count(*) from $table a join $table2 b on a.id = b.id"
