CREATE EXTERNAL TABLE IF NOT EXISTS 
tweets 
( 
  id bigint, 
  text string,
  posted string, 
  username string, 
  userID bigint, 
  longitude double, 
  latitude double, 
  source string, 
  query string
) 
row format serde 'org.apache.hadoop.hive.contrib.serde2.JsonSerde' 
location '${TABLEPATH}';

LOAD DATA INPATH '${INPUT}' OVERWRITE INTO TABLE tweets;

INSERT OVERWRITE DIRECTORY '${OUTPUT}' SELECT text FROM tweets;


