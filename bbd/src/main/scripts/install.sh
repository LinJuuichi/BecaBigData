#! /bin/bash

#Move to app directory.
BASEDIR=$(dirname $0)
cd ${BASEDIR}/

#Credentials to execute scripts.
sudo chmod +x *.sh

#Include imports.
. variables.sh		# Variables.

if [ ! -d "$BBD_INSTALL_DIR" ]; then
	sudo mkdir "$BBD_INSTALL_DIR"
fi

if [ -d "${BBD_INSTALL_DIR}/${BBD_VERSION}" ]; then
	sudo rm -r -f "${BBD_INSTALL_DIR}/${BBD_VERSION}"
fi

cd ../../

sudo mv -f "$BBD_VERSION" "${BBD_INSTALL_DIR}"

if [ -L "$BBD_LINK" ]; then
	sudo unlink "$BBD_LINK"
fi

sudo ln -s "${BBD_INSTALL_DIR}/${BBD_VERSION}/bin/run.sh" "$BBD_LINK"
