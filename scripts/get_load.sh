#!/bin/bash

if [ $# == 1 ]; then

	# get todays date
	date=`eval date +%Y-%m-%d`
	
	# read the last line of the log file
	load=`ssh -q -o StrictHostKeyChecking=yes $1.walterblaurock.com "tail -n 1 /var/lib/collectd/csv/localhost/load/load-$date"`

	# verify return value is 0 from curl
	if [ $? != 0 ]; then
		exit 1
	fi
	
	echo $load
	exit 0
	
else

	# fail if incorrect arguments are given
	exit 1
	
fi