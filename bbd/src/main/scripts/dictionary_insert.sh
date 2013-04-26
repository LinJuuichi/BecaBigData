#! /bin/bash

. environment.sh

INSERT_WORD=0
INSERT_IN_LIST=1

# Inserts word into dictionary.
function insert_word()
{
	# Inserting word.
	echo $WORD >> $DICTIONARY_PATH
}

# Inserts word into dictionary in list.
# If list doesn't exist, creates it.
function insert_in_list()
{
	# In which line is the list?
	# -w: search the exact word.
	LINE=`cat $DICTIONARY_PATH  | awk -F'=' '{print $1}' | grep -n -w  "$LIST" | awk -F':' '{print $1}'`

	# In case the list does not exist,
	if [  "$LINE" == "" ]
	then
		# Creates the list at the end of the file.
		echo "$LIST=$WORD" >> $DICTIONARY_PATH
	else # The list exists.
		# -i: modifies the file
		# ^${LIST}: the lines that beggin with ${LINE}
		# / s/$/ ${WORD}/: substitute the end of the line ($) by $WORD (aka append $WORD to the line)
		sed -i  "/^"${LIST}"/ s/$/ "$WORD"/" $DICTIONARY_PATH
	fi
}

function insert()
{
	INSERT_MODE=$1
	DICTIONARY=$2
	WORD=$3
	LIST=$4
	DICTIONARY_PATH=$DICTIONARIES_PATH/${DICTIONARY}$DICTIONARY_SUFFIX_NAME
	BACKUP_DICTIONARY_PATH=$DICTIONARIES_PATH/${DICTIONARY}${DICTIONARY_BACKUP_SUFFIX}$DICTIONARY_SUFFIX_NAME

	# Backing up dictionary.
	cp $DICTIONARY_PATH $BACKUP_DICTIONARY_PATH

	# Inserting mode.
	if [ $INSERT_MODE -eq $INSERT_WORD
	then
		insert_word
	elif  [ $INSERT_MODE -eq $INSERT_IN_LIST]
	then
		insert_in_list
	fi
	result=$?

	# Restoring if operation has failed.
	if [ $result -ne 0 ]
	then
		mv $BACKUP_DICTIONARY_PATH $DICTIONARY_PATH	
	fi 
}
