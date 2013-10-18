create table if not exists tweets
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
partitioned by(dt string)
row format delimited fields terminated by '\t';
