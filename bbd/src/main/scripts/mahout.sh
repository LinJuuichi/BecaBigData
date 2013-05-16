#! /bin/bash

#Run Mahout.
function mahout()
{
	java -cp "${BBD_INSTALL_DIR}/${BBD_VERSION}/lib/*:${MAHOUT_INSTALL_DIR}/core/target/mahout-core-0.8-SNAPSHOTjar" com.everis.bbd.mahout.FrequentPatternMining
}
