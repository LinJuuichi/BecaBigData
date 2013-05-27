#! /bin/bash

# Include
. /opt/bbd/bbd/bin/variables.sh		# Variables.

# usage function
function usage()
{
	echo "To Do"
}

#Move to app directory.
BASEDIR="${BBD_VERSION_LINK}/bin"
cd "$BASEDIR"


. uninstall.sh 	# Uninstall.
. flume.sh	# Flume functions.
. snconnector.sh 	# SNClient functions.
. hive.sh
. preprocessing.sh
. mahout.sh

# Execute getopt
SHORT_OPT="s:c:"
LONG_OPT="step:,uninstall,upgrade,configuration:"

ARGS=`getopt -o $SHORT_OPT -l $LONG_OPT -n "getopt.sh" -- "$@"`
# Bad arguments
if [ $? -ne 0 ];
then
	usage
	exit 1
fi

# Eval arguments
eval set -- "$ARGS";
while true;
do
	case "$1" in
		-i|--input)
	      		INPUT=$2
	      		shift 2;;
		-o|--output)
			OUTPUT=$2
			shift 2;;
		-s|--step)
			STEP=$2
			shift 2;;
		-c|--configuration)
			CONFIGURATION="$2"
			shift 2;;
		--uninstall)
			uninstall
			exit 1;;
		--upgrade)
			usage
			exit 1;;
		--)
			shift
			break;
	esac
done

# Arguments checking
# If someone is missing, forces bad execution and usage PRINT_MODEing.
if [ -z "$STEP" ]
then
	echo "STEP not specified."
	usage
	exit 1
fi

# STEP execution
case  "$STEP" in
"flume")
	flume_start "$CONFIGURATION"
	;;
"snconnector")
	snconnector_start
	;;
"hive")
	script="../config/hive/hive.q"
	hive_script "$script"
	;;
"preprocessing")
	preprocessing
	;;
"mahout")
	mahout
	;;
*)
	echo "Invalid step."
	;;
esac
