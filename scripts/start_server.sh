#!/bin/bash

if [ $# == 1 ]; then
	
	# start the server
	curl -s "http://lb.walterblaurock.com/balancer-manager/?lf=1&ls=0&wr=$1&rr=&dw=Enable&w=http%3A%2F%2F$1.walterblaurock.com&b=mycluster&nonce=9e162345-74ee-4f99-b077-5644c1708710" > /dev/null

	# verify return value is 0 from curl
	if [ $? != 0 ]; then
		exit 1
	fi
	
	# move to directory where this script is
	path=$0
	folder=${path/"start_server.sh"/""} 
	cd $folder
	
	# run status script
	status=`./server_status.py $1`
	
	# if no output, return 1
	if [ $? != 0 ]; then
		exit 1
	fi
	
	# return exit status
	if [ $status = "yes,yes" ] || [ $status = "yes,no" ]; then
		exit 0
	else
		exit 1
	fi
	
else
	exit 1
fi