"""
Author: chaz
Date: 11.24.15
Automation to test the api calls for the Pill Identifier
Usage: python pillIdentifierAPI [test_environment: stage, qa or prod]
I recommend redirecting the output to a text file
i.e.: python pillIdentifierAPI stage > pillIdentifierAPITests[date/time].txt
    
"""

import datetime
import json
import requests
import sys
import apiUtils
   
api_requests_dict = {'colors':'/api/service/2.0/pill/label/color?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078', 
                     'shapes':'/api/service/2.0/pill/label/shape?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078',
                     'markings':'/api/service/2.0/pill/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&markings=',
                     'name':'/api/service/2.0/pill/images?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&name='}
entered_env = sys.argv[1].lower()
colors_list = []
shapes_list = []
pills_list = []
name_list = []
imprint_list = []
pill_with_no_results = []
current_payload = ' '
test_env = ''
name_file = 'pillDrugNames.csv'
imprint_file = 'pillMarkings.csv'
expected_auto_count = 19
expected_imprint_count = 1662
number_of_results = 0


            

if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Pill Identifier API Test started at : %s on %s ****************************' %(str(start_time), entered_env)
    print ''
    test_env = apiUtils.get_env(entered_env)
    colors_list = apiUtils.get_colors(test_env)
    shapes_list = apiUtils.get_shapes(test_env)
    name_list = apiUtils.get_list_of_pills(name_file)
    imprint_list = apiUtils.get_list_of_pills(imprint_file)
    print '** Results lists:'
    print 'First testing the Legacy api calls:'
    print ''
    #autocomplete_url_web = test_env + '/v2/druginputautocomplete?query=tyl&for=pill'
    #imprint_url_web = test_env + '/v2/goldDrugImprint?query=e'
    imprint_url_api = test_env + '/api/service/2.0/pill/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&markings=e'
    #apiUtils.verify_pills_list(apiUtils.parse_response(apiUtils.make_request(autocomplete_url_web),'Drugs', 'dict'), expected_auto_count)
    apiUtils.parse_response(apiUtils.make_request(imprint_url_api),'data', 'dict')
    print 'Now running tests for the new api calls:'
    apiUtils.output_pills_list(apiUtils.get_pills_color_shape(test_env, colors_list, shapes_list))
    print 'Running tests for names...'
    apiUtils.get_pills(test_env, name_list, 'name', 'meta')
    print ''
    print 'Number of pill names which produced no results: %i' %len(pill_with_no_results)
    print 'List of pill names which produced no results: %s' %pill_with_no_results
    print ''
    print 'Running tests for markings...'
    apiUtils.get_pills(test_env, imprint_list, 'markings', 'meta')
    print ''
    print 'Number of pill markings which produced no results: %i' %len(pill_with_no_results)
    print 'List of pill makings which produced no results: %s' %pill_with_no_results
    end_time = datetime.datetime.now()
    print '**************************** Pill Identifier API Test ended at : %s on %s ****************************' %(str(end_time), entered_env)
    
    
    
    


        




