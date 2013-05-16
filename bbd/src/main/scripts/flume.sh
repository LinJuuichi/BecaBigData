#! /bin/bash

#Run flume.
function flume_start()
{
	${FLUME_INSTALL_DIR}/bin/flume-ng agent -n a1 -c conf -f ${BBD_INSTALL_DIR}/${BBD_VERSION}/config/flume/default_hdfs_agent.properties
}
