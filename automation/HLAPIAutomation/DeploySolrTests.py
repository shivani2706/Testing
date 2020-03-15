"""
Author: chaz
Date: 05.03.16
Script: DeploySolrTests.py
Description: This script is used for running the tests for solr as listed on the QA Wiki page.
1. Spell Check.
2. Max Score Check.
3. Index List.
4. New Field Type Check.
"""
import apiUtils
import sys
import datetime

test_count = 4
failed_count = 0
passed_count = 0
result = ''

solr_query_list = ['/solr/city-state-spellcheck?q=san%20hose&max=2&rows=0',
                   '/solr/select?indent=on&version=2.2&q=sourcename%3Ahlcms&fq=fever&start=0&rows=10&fl=*%2Cscore&qt=&wt=&explainOther=&hl.fl=',
                   '/solr/select/?q=*:*&version=2.2&start=0&rows=0&indent=on&facet=true&facet.field=sourcename',
                   '/solr/admin/file/?contentType=text/xml;charset=utf-8&file=schema.xml']

entered_env = sys.argv[1].lower()

def get_test_env(test_env):
    domain = '.healthline.com'
    testing_env = ''
    # Get the test env i.e.: stage02, stage01 or www.
    if test_env == 'stage01':
        testing_env = 'http://vmc-solr01-stage'
    elif test_env == 'sfc':
        testing_env = 'http://vmc-solr01-prod'
    elif test_env == 'sfc-web01':
        testing_env = 'http://sfc-web01'
    elif test_env == 'njc':
        testing_env = 'http://njc-solrslave01-prod'
    elif test_env == 'njc-web01':
        testing_env = 'http://njc-web01'
    elif test_env == 'prod_clone':
        testing_env = 'http://64.84.32.42'
        domain = ''
    # print 'The testing_env: %s'%testing_env
    return testing_env + domain

if __name__ == '__main__': 
    start_time = datetime.datetime.now() 
    print '**************************** Deploy Solr Tests started at : %s on %s ****************************' %(str(start_time), entered_env)
    print ''
    print 'Test 01: Solar Misspell Check:'
    result = apiUtils.make_request(get_test_env(entered_env) + solr_query_list[0], True)
    print ''
    print 'Test 02: Max Score Check:'
    result = apiUtils.make_request(get_test_env(entered_env) + solr_query_list[1], True)
    print ''
    print 'Test 03: Index List in Solr:'
    result = apiUtils.make_request(get_test_env(entered_env) + solr_query_list[2], True)
    print ''
    print 'Test 04: New Field Type Check:'
    result = apiUtils.make_request(get_test_env(entered_env) + solr_query_list[3], True)
    print ''
    passed_count = test_count - failed_count
    print 'Total Tests Ran: %i' %len(solr_query_list)
    print 'Total Tests Passed: %i' %passed_count
    print 'Total Tests Failed: %i' %failed_count
    print ''
    end_time = datetime.datetime.now()
    print '**************************** Deploy Solr Tests ended at : %s on %s ****************************' %(str(end_time), entered_env)
    
    if failed_count > 0:
        sys.exit(1)
    else:
        sys.exit(0)
    