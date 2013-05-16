create external table if not exists tweets
(
id double,
text string,
userId double,
username string,
latitude double,
longitude double,
posted string,
source string,
query string
)
row format delimited fields terminated by '\t'
location '/user/training/hive';

LOAD DATA INPATH '/user/training/flume/2013-05-14' OVERWRITE INTO TABLE tweets;

INSERT OVERWRITE DIRECTORY '/user/training/mapreduce' SELECT text FROM tweets;
