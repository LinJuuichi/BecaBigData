#! /bin/bash

#Run Preprocessing.
function preprocessing()
{
	cd "${BBD_INSTALL_DIR}/${BBD_VERSION}/config/dictionaries"
	hadoop jar "${BBD_INSTALL_DIR}/${BBD_VERSION}/lib/bbd-0.0.1-SNAPSHOT.jar"  com.everis.bbd.mapreduce.Preprocessing -files \ 			  	 	 	 	 	 	 	 		 				"char.dictionary#char.dictionary,word.dictionary#word.dictionary,words.dictionary#words.dictionary"
}
