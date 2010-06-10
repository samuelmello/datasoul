#!/usr/bin/python

import sys
import os

def convert(s):
	return s.replace("*", "\\*");

f = open(sys.argv[1], 'r')

x = f.read()

f.close()

for l in  x.split('\n'):
	beg = 0
	#print "---> "+l
	if len(l) == 0 or l[0] == '#':
		continue
	while True:
		idx = l.find("=", beg)
		if idx > 0 and l[idx-1] != '\\':
			ret = os.system('[ $(grep -l "' + convert(l[:idx]) + '" src -r | grep .java$ | wc -l) -eq 0 ]')
			if ret == 0:
				print l[:idx]
			break
		beg += idx

	
