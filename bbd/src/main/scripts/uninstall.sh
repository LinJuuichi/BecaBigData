#! /bin/bash

# Include|imports
. variables.sh		# Variables.

#Run Uninstall.
function uninstall()
{
	if [ -L "$BBD_LINK" ]; then
		sudo unlink "$BBD_LINK"
	fi

	if [ -d "$BBD_INSTALL_DIR" ]; then
		sudo rm -r -f "$BBD_INSTALL_DIR"
	fi
}
