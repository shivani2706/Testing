"""
Author: chaz
Date: 04.27.16
Script: ArticlesAPITests.py
Description: This script is used for testing the Michael's API.
Usage: Pass in the standard server, test server and True to show diff, False to suppress it.
Based on Michael Zack's API Doc.
1. Request article via api from a server without Taxonomy
2. Request the same article via api from a server with Taxonomy
3. Compare the jsons if they are equal we're good otherwise we have a problem.
"""
import apiUtils
import sys
import datetime
from difflib import context_diff

recent_articles_published = '/api/service/2.0/news/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&publishedSince='
article_info = '/api/service/2.0/article/content?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&url='
api_call_list = ['/api/service/2.0/article/content?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&url=rashes',
                 '/api/service/2.0/bodymap/players?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078',
                 '/api/service/2.0/bodymap/overview?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&player=Breast',
                 '/api/service/2.0/slideshow/content?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&url=10-diet-changes-to-extend-your-life',
                 '/api/service/2.0/video/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&query=diabates',
                 '/api/service/2.0/pill/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&name=lipitor',
                 '/api/service/2.0/gold/content/acyclovir/oral-capsule?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078',
                 '/api/service/2.0/recipe/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078',
                 '/api/service/2.0/spellchecking/city?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&query=san%20hose',
                 '/api/service/2.0/spellchecking/health?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&query=diabates']

entered_env01 = sys.argv[1].lower()
entered_env02 = sys.argv[2].lower()
show_diff = sys.argv[3]
article_url_list = []
api_counter = 0
error_counter = 0

api_url01 = apiUtils.get_env(entered_env01)     # Taxonomy Off
api_url02 = apiUtils.get_env(entered_env02)     # Taxonomy On

def output_differences(json_text01, json_text02):
    for line in context_diff(json_text01, json_text02, fromfile='before.py', tofile='after.py'):
        sys.stdout.write(line)

if __name__ == '__main__': 
    start_time = datetime.datetime.now() 
    print '**************************** Articles API Tests started at : %s on %s and %s ****************************' %(str(start_time), entered_env01, entered_env02)
    print ''
    for api_call in api_call_list:
        results01 = apiUtils.parse_response(apiUtils.make_request(api_url01 + api_call, False), "meta")
        results02 = apiUtils.parse_response(apiUtils.make_request(api_url02 + api_call, False), "meta")
        num_results01 = results01['numResults']
        num_results02 = results02['numResults']
        if num_results01 > 0 and num_results02 > 0:     # For now just checking for response and number of results.
            print '[PASSED]: The number of results are greater than 0 <%s Taxonomy Off - %i> and <%s Taxonomy On - %i>'%(entered_env01, num_results01, entered_env02, num_results02)
        elif num_results01 <= 0 and num_results02 > 0:
            print '[FAILED]: There were no results for <%s Taxonomy Off - %i> and <%s Taxonomy On - %i>'%(entered_env01, num_results01)
            error_counter += 1
        elif num_results01 == 0 and num_results02 == 0:
            print '[INFO]: There were no results for <%s Taxonomy Off - %i> and <%s Taxonomy On - %i>'%(entered_env01, num_results01, entered_env02, num_results02)
        print ''
        if show_diff == True:
            # This switch is for a deeper comparision of the jsons.
            if results01 == results02:
                print '[PASSED]: The json matches for <%s Taxonomy Off> and <%s Taxonomy On>'%(entered_env01, entered_env02)
            else:
                print '[FAILED]: The json for <%s Taxonomy Off> and <%s Taxonomy On> did not match for url: %s!!!'%(entered_env01, entered_env02, api_call_list[api_counter])
                print ''
                print '[INFO]: <%s Taxonomy Off json:>'%entered_env01
                print ''
                print results01
                print ''
                print '[INO]: <%s Taxonomy On json:>'%entered_env02
                print results02
                print ''          
                print '[INFO]: The Differences:'
                print ''
                output_differences(results01, results02)
                print ''
                error_counter += 1
        api_counter += 1
    print ''
    print 'Total APIs Made: %i'%len(api_call_list)
    print 'Total Tests Passed: %i'%(len(api_call_list) - error_counter)
    print 'Total Errors Found: %i'%error_counter
    
       
    end_time = datetime.datetime.now()
    print '**************************** Articles API Tests ended at : %s on %s and %s ****************************' %(str(end_time), entered_env01, entered_env02)
    print ''
    
    if error_counter > 0:
        sys.exit(1)
    else:
        sys.exit(0)