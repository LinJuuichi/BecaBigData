#! /bin/bash

config_directory=/etc/flume-ng/conf/
config_file=tweets_default_hdfs_agent.properties

flume-ng agent -n a1 -c conf -f $config_directory$config_file

