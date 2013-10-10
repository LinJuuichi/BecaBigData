#! /bin/bash

#Run Hive.
function hive_script()
{
	script=$1
	hive -f "$script"
}
