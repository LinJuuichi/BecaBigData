#! /bin/bash

. environment.sh

# Checks if contains.
function exists()
{
	DICTIONARY=$1
	WORD=$2
	DICTIONARY_PATH=$DICTIONARIES_PATH/${DICTIONARY}$DICTIONARY_SUFFIX_NAME
	
	# -w: search the exact word.
	NUMBER=`cat $DICTIONARY_PATH | grep -w $WORD | wc -l`

	if [ $NUMBER -gt 0 ]
	then
		echo "Word $WORD exists in dictionary $DICTIONARY."
		return 0
	else
		echo "Word $WORD does NOT exist in dictionary $DICTIONARY."
		return 1
	fi
}

# Check if the word exists in list from dictionary.
function exists_in_list()
{
	DICTIONARY=$1
	LIST=$2
	WORD=$3
	DICTIONARY_PATH=$DICTIONARIES_PATH/${DICTIONARY}$DICTIONARY_SUFFIX_NAME

	# -w: search the exact word.
	LWORD=`cat $DICTIONARY_PATH | grep ^$LIST= | grep -w $WORD``
`
	if [  "$LWORD" == "" ]
	then
		echo "Word $WORD does NOT exist in list $LIST."
		return 1
	else
		echo "Word $WORD exists in list $LIST."
		return 0
	fi
}

# Check if the list exists in dictionary.
function exists_list()
{
	DICTIONARY=$1
	LIST=$2
	DICTIONARY_PATH=$DICTIONARIES_PATH/${DICTIONARY}$DICTIONARY_SUFFIX_NAME

	# -w: search the exact word.
	LINE=`cat $DICTIONARY_PATH  | awk -F'=' '{ print $1 }' | grep -n -w $LIST | awk -F':' '{ print $1 }' ``
`
	if [  "$LINE" == "" ]
	then
		echo "List $LIST does NOT exist."
		return 1
	else
		echo "List $LIST exists."
		return 0
	fi
}
