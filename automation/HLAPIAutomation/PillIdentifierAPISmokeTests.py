"""
Author: chaz
Date: 12.07.15
Script: PillIdentifierAPISmokeTests.py
Description: This script is used for smoke testing the Pill Identifier's API
Criteria: 5 Use cases provided by Samya:
1. Search by imprint
2. Search by drug name
3. Search by shape
4. Search by color
5. Search by shape and color and select a multi-color combination
"""

import apiUtils
import datetime
import sys
   
entered_env = sys.argv[1].lower()
test_env = apiUtils.get_env(entered_env)
auto_complete = '/api/service/2.0/gold/auto?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&searchTerms=asp'
pill_imprints = ['512', '120 Inderal XL', 'H']
pill_names = ['Percocet', 'Oxycodone', 'Xanax']
pill_colors  = ['white', 'blue', 'pink']
pill_shapes = ['round', 'oval', 'capsule']
test_count = 0
failed_count = 0
passed_count = 0
result = ''

if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Pill Identifier API Smoke Tests started at : %s on %s ****************************' %(str(start_time), entered_env)
    print ''
    print 'Test 01: Auto Complete:'
    result = apiUtils.parse_response(apiUtils.make_request(test_env + auto_complete, True), 'meta', 'dict')
    (failed_count, test_count) = apiUtils.evaluate_result(result, failed_count, test_count)
    print ''
    print 'Test 02: Imprint:'
    print ''
    for pill_imprint in pill_imprints:
        result = apiUtils.get_pill(test_env, pill_imprint, 'markings', 'meta')
        (failed_count, test_count) = apiUtils.evaluate_result(result, failed_count, test_count)
    print ''
    print 'Test 03: Drug Name:'
    print ''
    for pill_name in pill_names:
        result = apiUtils.get_pill(test_env, pill_name, 'name', 'meta')
        (failed_count, test_count) = apiUtils.evaluate_result(result, failed_count, test_count)
    print ''
    print 'Test 04: Drug Shape:'
    print ''
    for pill_shape in pill_shapes:
        result = apiUtils.get_pill(test_env, pill_shape, 'shape', 'meta')
        (failed_count, test_count) = apiUtils.evaluate_result(result, failed_count, test_count)
    print ''
    print 'Test 05: Drug Color:'
    for pill_color in pill_colors:
        result = apiUtils.get_pill(test_env, pill_color, 'color', 'meta')
        (failed_count, test_count) = apiUtils.evaluate_result(result, failed_count, test_count)
        
    print ''
    print 'Test 06: Drug Shape and Color'
    for color, shape in zip(pill_colors, pill_shapes):
        result = apiUtils.get_pill_color_shape(test_env, color, shape)
        (failed_count, test_count) = apiUtils.evaluate_result(result, failed_count, test_count)
        
    print ''
    passed_count = test_count - failed_count
    print 'Total Tests Ran: %i' %test_count
    print 'Total Tests Passed: %i' %passed_count
    print 'Total Tests Failed: %i' %failed_count
    print ''
    end_time = datetime.datetime.now()
    print '**************************** Pill Identifier API Smoke Tests ended at : %s on %s ****************************' %(str(end_time), entered_env)
    
    if failed_count > 0:
        sys.exit(1)
    else:
        sys.exit(0)
    