#!/bin/bash

if [ $# == 1 ]; then
	
	# start the server
	curl -s "http://lb.walterblaurock.com/balancer-manager/?lf=1&ls=0&wr=$1&rr=&dw=Disable&w=http%3A%2F%2F$1.walterblaurock.com&b=mycluster&nonce=9e162345-74ee-4f99-b077-5644c1708710" > /dev/null

	# verify return value is 0 from curl
	if [ $? != 0 ]; then
		exit 1
	fi

	# verify server has stopped

	# return with exit status
	exit 0
	
else
	echo "error"
	exit 1
fi