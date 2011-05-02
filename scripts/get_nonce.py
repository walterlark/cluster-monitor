#!/usr/bin/python

import urllib
import sys
import re

def main(*args):

	if (len(args) != 1):
		return 1

	sock = urllib.urlopen("http://lb.walterblaurock.com/balancer-manager/")
	htmlSource = sock.read()
	sock.close()

	# get the nonce
	nonce = re.findall("&nonce=.*\"", htmlSource)[0]
	
	nonce = nonce.replace("&nonce=", "")
	nonce = nonce.replace("\"", "")
	
	sys.stdout.write(nonce)
	sys.stdout.flush()
	
	return 0
	
if __name__ == '__main__':
	
	status = 1
	try:
		status = main(*sys.argv)
	except:
		status = 1
	sys.exit(status)