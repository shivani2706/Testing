'''
Author: chaz
Date: 11.07.17
Script: SXCTests
Descriptions: Test suite for Symptom Checker API
Symptoms are stored in a text file: symptomList.txt.csv
'''
import apiUtils
import datetime
import sys
import json

# Passed in test environment i.e.: qa, stage or prod.
#test_env = sys.argv[1].lower()
test_env = 'api-new'
if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Rebuild API SXC Tests started at : %s on %s ****************************' %(str(start_time), test_env.upper())
    print ''
    print 'Test01: Load each symptom and make the api call'
    file = open("symptomList.txt", "r")
    for line in file:
        print '[Info]: Now checking symptom: %s'%line
        apiUtils.get_articles_sxc(test_env, line)
    print ''
    end_time = datetime.datetime.now()
    print '**************************** Rebuild API SXC Tests finished at : %s on %s ****************************' %(str(end_time), test_env.upper())