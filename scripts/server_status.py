#!/usr/bin/python

import urllib
import sys
import re

def main(*args):

	if (len(args) != 2):
		return 1

	sock = urllib.urlopen("http://lb.walterblaurock.com/balancer-manager/")
	htmlSource = sock.read()
	sock.close()

	# get the second table
	table = re.findall("<table.*?>((.|\n)*?)</table>", htmlSource)[1][0]
	
	# get all rows
	count = 0
	for row in re.findall("<tr>((.|\n)*?)</tr>", table):
		if count == 0:
			count = 1
			pass;
		
		# parse the row for the status field
		tds = re.findall("<td.*?>((.|\n)*?)</td>", row[0])
		
		if len(tds) != 0:
			if tds[1][0] == args[1]:

				up = "yes"
				active = "yes"
				
				if tds[4][0].rstrip() != "0":
					active = "no"
				
				if tds[5][0].rstrip() == "Dis":
					up = "no"
					active = "no"

				sys.stdout.write(up + "," + active)
				sys.stdout.flush()
				return 0
		
		count = count + 1
		

	return 1

if __name__ == '__main__':
	sys.exit(main(*sys.argv))