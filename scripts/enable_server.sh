#!/bin/bash

if [ $# == 1 ]; then

	# move to directory where this script is
	path=$0
	folder=${path/"enable_server.sh"/""} 
	cd $folder
	
	# get nonce
	nonce=`./get_nonce.py`
	
	# if no output, return 1
	if [ $? != 0 ]; then
		exit 1
	fi
	
	# enable the server
	curl -s "http://lb.walterblaurock.com/balancer-manager/?lf=1&ls=0&wr=$1&rr=&dw=&w=http%3A%2F%2F$1.walterblaurock.com&b=mycluster&nonce=$nonce" > /dev/null

	# verify return value is 0 from curl
	if [ $? != 0 ]; then
		exit 1
	fi
	
	# run status script
	status=`./server_status.py $1`
	
	# if no output, return 1
	if [ $? != 0 ]; then
		exit 1
	fi
	
	# return exit status
	if [ $status = "yes,yes" ] || [ $status = "no,yes" ]; then
		exit 0
	else
		exit 1
	fi
	
else
	exit 1
fi