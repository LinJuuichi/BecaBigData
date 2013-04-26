#! /bin/bash

# usage function
function usage()
{
	echo "To Do"	
}

# Include|imports
. environment.sh		# Variables
. dictionary_exists.sh	# Exist functions
. dictionary_insert.sh	# Insert functions
. dictionary_remove.sh	# Remove functions

# Modes
EXISTS=1
INSERT=2
REMOVE=3
REMOVE_LIST=4
PRINT=0

# Execute getopt
SHORT_OPT="x:puhe:i:d:l:r:"
LONG_OPT="remove-list:,print,usage,help,exists:,insert:,dictionary:,list:,remove:"

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
		-h|--help|-u|--usage)
	      		usage
	      		shift;;
		-p|--print)
			MODE=$PRINT
			shift;;
		-x|--remove-list)
			MODE=$REMOVE_LIST
			LIST=$2
			shift 2;;
		-e|--exists)
			MODE=$EXISTS
			WORD=$2
			shift 2;;
		-i|--insert)
			MODE=$INSERT
			WORD=$2
			shift 2;;
		-d|--dictionary)
			# Checking if the dictionary selected is correct.
			if [ $2 != ${CHAR_DICTIONARY_NAME} -a $2 !=  ${WORD_DICTIONARY_NAME} -a $2 != ${WORD_SUBS_DICTIONARY_NAME} ]
			then
				echo "Dictionary $2 does not exist."
				usage
				exit 1
			fi
			DICTIONARY=$2
			shift 2;;
		-l|--list)
			LIST=$2
			shift 2;;
		-r|--remove)
			MODE=$REMOVE
			WORD=$2
			shift 2;;
		--)
			shift
			break;
	esac
done

# Arguments checking
# If someone is missing, forces bad execution and usage printing.
if [ -z "$MODE" ]
then
	echo "Mode not specified."
	usage
	exit 1
fi

if [ -z "$DICTIONARY" ]
then
        echo "Dictionary not specified."
        usage
        exit 1
fi

# Printing $DICTIONARY
if [ $MODE -eq $PRINT ]
then
	if [ $DICTIONARY == $WORD_SUBS_DICTIONARY_NAME ]
	then
		if [ ! -z "$LIST" ]
		then
			cat $DICTIONARIES_PATH/${DICTIONARY}$DICTIONARY_SUFFIX_NAME | grep -w ^$LIST
			exit
		fi
	fi
	cat $DICTIONARIES_PATH/${DICTIONARY}$DICTIONARY_SUFFIX_NAME
	exit
fi

# Removing a list from the dictionary $DICTIONARY
if [ $MODE -eq $REMOVE_LIST ]
then
	remove $REMOVE_A_LIST $DICTIONARY $LIST
	exit
fi

# The options resting require a word to be set.
if [ -z "$WORD" ]
then
        echo "Word not specified."
        usage
        exit 1
fi

# Operations
# Inserting $WORD into $DICTIONARY
if [ $MODE -eq $INSERT ]
then
	if [ $DICTIONARY == $WORD_SUBS_DICTIONARY_NAME ]
	then
		if [ -z "$LIST" ]
		then
			echo "Missing list option."
			usage
			exit 1
		fi
		insert $INSERT_IN_LIST $DICTIONARY  $WORD $LIST
	else
		insert $INSERT_WORD $DICTIONARY $WORD
	fi
fi

# Check if $WORD exists in $DICTIONARY
if [ $MODE -eq $EXISTS ]
then
	if [ $DICTIONARY == $WORD_SUBS_DICTIONARY_NAME ]
	then
		if [ -z "$LIST" ]
		then
			echo "Missing list option."
			usage
			exit 1
		fi
		exists_list $DICTIONARY $LIST
		if [ $? -eq 0  ]
		then
			exists_in_list $DICTIONARY $LIST $WORD
		fi
	else
		exists $DICTIONARY $WORD
	fi
fi

# Removing $WORD from $DICTIONARY
if [ $MODE -eq $REMOVE ]
then
	if [ "$DICTIONARY" == "$WORD_SUBS_DICTIONARY_NAME" ]
	then
		if [ -z $LIST ]
		then
			echo "List not specified."
			usage
			exit 1
		else
			remove $REMOVE_FROM_LIST $DICTIONARY $WORD $LIST
		fi	
	else
		remove $REMOVE_WORD $DICTIONARY $WORD
	fi
fi
