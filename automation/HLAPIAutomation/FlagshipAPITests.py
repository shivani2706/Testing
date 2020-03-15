"""
Author: chaz
Date: 07.28.16
Script: FlagshipAPITests.py
Description: This script is used for smoke testing the Flagship mobile Application's API.

"""

import apiUtils
import datetime
import sys
import json
import requests
from requests_oauthlib import OAuth1
import random
from random import randrange

# Passed in test environment i.e.: qa, stage or prod.
test_env = sys.argv[1].lower()
# Passed in mobile application i.e.: flagship (for now).
app_key = sys.argv[2].lower()

flagship_api_calls = {'search':'/search', 'search_term':'/search/','article_url':'/article/', 'create':'/create',
                      'user_info':'/id/', 'login':'/login', 'my_profile':'/MyProfile', 'logout':'/logout/', 'news_feed':'/news-list',
                      'update_user_profile':'/UpdateUserProfile', 'about_us':'/AboutUs', 'private_policy':'/PrivatePolicy',
                      'auto_complete':'/suggest/', 'terms_and_conditions':'/TermsAndConditions', 'convert_user':'/conversion/', 
                      'news_catagory_list':'/news-category-list', 'email_signup':'/emailSignup', 'news_list':'/news-list'}


protocol = "http://"
domain = ".healthline.com/"
stage_env_url = "flagship-stage"
qa_env_url = "flagship-qa"
seung_env_url = "10.1.0.103:8380/"
api_url = ''
payload_01 = {"searchTerms":["fever"]}
payload_02 = {"searchTerms":["abdominal bloating","pain","fever","chills"]}
article_list = ["/rashes", "/depression", "/cold-flu", "/asthma-symptoms"]
news_feed_payload = {"params": {
                                "rows": "10",
                                "start": "0"                  
                    }           }
email_signup_payload = {"params": { 
                                   "email": "slee@healthline.com",
                                   "categories": [
                                                  "allergies",
                                                  "cancer"
                                                  ]
                                   }
                        }

news_list_payload = {"params": {
                                "rows": "10",
                                "start": "0",
                                "email": "chaz@healthline.com"
                                }
                     }

auto_entry = 'fev'
test_count = 0
failed_count = 0
passed_count = 0 
result = ''
num_results = 0
art_length = len(article_list) - 1
article_selection = random.randrange(0, art_length)
fs_access_key = '5f5d25135b8ecdfb608b0357af09feee06f96e663435e2e00f5b455f03c7619a'
Flagship_http_headers = {'RANDOM-STR':fs_access_key}
http_headers = {}

def get_app_env_url(test_env):
    if test_env == 'stage':
        api_url = protocol + stage_env_url + domain + app_key
    elif test_env == 'seung':
        api_url = protocol + seung_env_url + app_key
    elif test_env == 'qa':
        api_url = protocol + qa_env_url + domain + app_key
    else:
        api_url = protocol + app_key + domain + app_key
    print "api_url is: %s"%api_url   
    return api_url

if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Application: %s API Smoke Tests started at : %s on %s ****************************' %(app_key, str(start_time), test_env)
    api_url = get_app_env_url(test_env)
    print ''
    print ''
    print 'Test 01: Search Term: fever - Post Method'
    response01 = apiUtils.app_make_post(api_url + flagship_api_calls['search_term'], payload_01, True, app_key, True)
    num_results01 = apiUtils.fs_get_num_results(response01)
    if num_results01 > 0:
        print 'Number of Results: %i Test 01 Passed...'%num_results01
    else:
        print 'Test 01 Failed!'
        failed_count += 1
    test_count += 1
    print ''
    print 'Test 02: Search Term: blister, fever - Post Method'
    response02 = apiUtils.app_make_post(api_url + flagship_api_calls['search_term'], payload_02, True, app_key, True)
    num_results02 = apiUtils.fs_get_num_results(response02)                       
    print ''
    if num_results02 > 0:
        print 'Number of Results: %i Test 02 Passed...'%num_results02
    else:
        print 'Test 02 Failed!'
        failed_count += 1
    test_count += 1
    print ''
    print 'Test03: Article:url: '
    print 'Article selected: %s'%article_list[article_selection]
    response03 = apiUtils.app_make_request(api_url + flagship_api_calls['article_url'] + article_list[article_selection], True, app_key, True)
    num_results03 = apiUtils.fs_get_num_results(response03)
    print ''
    if num_results03 > 0:
        print 'Number of Results: %i Test 03 Passed...'%num_results03
    else:
        print 'Test 03 Failed!'
        failed_count += 1
    test_count += 1
    print ''
    print 'Test04: Auto-complete - Get Method'
    response04 = apiUtils.app_make_request(api_url + flagship_api_calls['auto_complete'] + auto_entry, True, app_key, True)
    num_results04 = apiUtils.fs_get_num_results(response04)
    print ''
    if num_results04 > 0:
        print 'Number of Results: %i Test 04 Passed...'%num_results04
    else:
        print 'Test 04 Failed!'
        failed_count += 1
    test_count += 1
    print ''
    print 'Test05: News Feed - Post Method'
    response05 = apiUtils.app_make_post(api_url + flagship_api_calls['news_feed'], news_feed_payload, True, app_key, True)
    num_results05 = apiUtils.fs_get_num_results(response05)
    if num_results05 > 0:
        print 'Number of Results: %i Test 05 Passed...'%num_results05
    else:
        print 'Test 05 Failed!'
        failed_count += 1
    test_count += 1
    print ''
    print 'Test06: News Category List - Get Method'
    response06 = apiUtils.app_make_request(api_url + flagship_api_calls['news_catagory_list'] + auto_entry, True, app_key, True)
    num_results06 = response06["map"]["serviceData"]
    print ''
    if num_results06 > 0:
        print 'Number of Results: %i Test 06 Passed...'%num_results06
    else:
        print 'Test 06 Failed!'
        failed_count += 1
    test_count += 1
    print ''
    print 'Test07: Email Signup - Post Method'
    response07 = apiUtils.app_make_post(api_url + flagship_api_calls['email_signup'], email_signup_payload, True, app_key, True)
    results07 = response07["map"]["serviceMessage"]
    if results07 == "Email Signup called successfully ":
        print 'Expected Response Message Recieved: %s Test 07 Passed...'%results07
    else:
        print 'Test 07 Failed!'
        failed_count += 1
    test_count += 1
    print ''
    print 'Test08: News List - Post Method'
    response08 = apiUtils.app_make_post(api_url + flagship_api_calls['news_list'], news_list_payload, True, app_key, True)
    num_results08 = apiUtils.fs_get_num_results(response08)
    if num_results08 > 0:
        print 'Number of Results: %i Test 08 Passed...'%num_results08
    else:
        print 'Test 08 Failed!'
        failed_count += 1
    test_count += 1
    print ''
    print '====== Test Results ======'
    passed_count = test_count - failed_count
    print 'Total Tests Ran: %i' %test_count
    print 'Total Tests Passed: %i' %passed_count
    print 'Total Tests Failed: %i' %failed_count
    print ''
    end_time = datetime.datetime.now()
    print '**************************** Application: %s API Smoke Tests ended at : %s on %s ****************************' %(app_key, str(end_time), test_env)
    