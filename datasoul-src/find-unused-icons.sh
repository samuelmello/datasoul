#!/usr/bin/python

import os
import dircache

list = dircache.listdir('src/datasoul/icons/v2')

for icon in list:
	ret =  os.system('[ $(grep -l "'+icon+'" src -r | grep .java$ | wc -l) -eq 0 ]')
	if ret == 0:
		print icon


