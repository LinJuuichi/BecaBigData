#! /bin/bash

#Run Preprocessing.
function preprocessing()
{
	cd "${BBD_VERSION_LINK}/config/dictionaries"
	hadoop jar "${BBD_INSTALL_DIR}/${BBD_VERSION}/lib/bbd-0.0.1-SNAPSHOT.jar"  com.everis.bbd.mapreduce.Preprocessing -files "word.dictionary#word.dictionary,words.dictionary#words.dictionary"
}
