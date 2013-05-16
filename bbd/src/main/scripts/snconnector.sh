#! /bin/bash

#Run SNclient.
function snconnector_start()
{
	java -cp ${BBD_INSTALL_DIR}/${BBD_VERSION}/lib/*:${FLUME_INSTALL_DIR}/lib/*: com.everis.bbd.SNApplication
}
