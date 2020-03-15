"""
Author: chaz
Date: 04.07.16
Script: AdMapperTests.py
Description: This script is used for testing the AdMapper.
1. Request an url - pattern of the url tbd on stage02 or stage01
2. Parse the returning content (i.e. re.content)
3. Capture the variables k* (kw, k1, k2, k3) with their values.
4. Rerun steps 1-3 on Oleg's machine.
5. Compare the two lists, stage02 being the standard (if stage02 has values and Oleg's matches we're good,
   If stage02 has values but Oleg's don't then we have a bug).
"""
import requests
import re
from bs4 import BeautifulSoup
import sys
import datetime
import apiUtils

entered_env01 = sys.argv[1].lower()
entered_env02 = sys.argv[2].lower()
# print 'Entered environments are: %s, %s'%(entered_env01, entered_env02)
articles_dict = {'drugs':'/drugs/', 'health':'/health/', 'slidehow':'/slideshow/', 'symptom':'/symptom/', 'video':'/video/'}
articles_list = ['/health/rashes', '/health/raised-skin-bump', '/health/cold-flu/best-remedies-stomach-flu', '/symptom/abdominal-pain', 
                 '/health/skin-disorders', '/symptom/vaginal-itching', '/symptom/vaginal-discharge', '/health/stomach-ulcer',
                 '/health/cold-flu/sinus-infection-symptoms', '/health/cold-flu/sore-throat-natural-remedies', '/health/beauty-skin-care/best-stye-remedies',
                 '/?gaPage=[homepage15]', '/health-slideshow/stuffy-nose-relief', '/health/pregnancy/five-signs-to-take-pregnancy-test', '/symptom/dizziness', 
                 '/health/6-effective-earache-remedies', '/health/digestive-health/why-is-my-poop-green', '/health/vaginal-yeast-infection', 
                 '/health/rash-on-genitals', '/symptom/flank-pain', '/health/ovarian-cysts', '/symptom/pain-on-swallowing', '/health/iron-deficiency-anemia',
                 '/health/pregnancy/early-symptoms-timeline']
protocol = 'http://'
domain = ''
test_url01 = ''
test_url02 = ''
list_of_expected_kvalues = []
list_of_kvalues = []
error_counter = 0

def get_k_values(url):
    # This method does the following:
    # 1. Make a request for the url in the list.
    # 2. Set the returning html to text context.
    # 3. Use regex to find the kvalues.
    # 4. Add the kvalues to a list and returns it.
    list_of_k_values = []
    print 'The contents of list of kvalues for url %s:'%url
    print '-----------------------------------------------------------'
    for article in articles_list:
        print 'Getting k values for %s%s: '%(url, article)
        req = requests.get(url + article)
        # Store the html source.
        page_html = req.content
        # Set BeautifulSoup to parse.
        soup = BeautifulSoup(page_html, 'html.parser')
        # Grab only the text since the variables are in a javascript function.
        text = soup.getText()
        # Use regular expression to get the k values (i.e.: kw, k1, k2 & k3).
        k_values = re.findall(r'var k. = .*', text)
        list_of_k_values.append(k_values)
        print k_values
    print ''
    req.connection.close()
    return list_of_k_values

def compare_kvalues(list01, list02, error_count):
    url_counter = 0
    # Use to compare the list to see if they matched list01 is the standard.
    for j_list, k_list in zip(list01, list02):
        # j_list is the Standard.
        j_list_counter = 0
        item_counter = 0
        print '[INFO]: Comparing both lists [%i] for url: %s...'% (url_counter, articles_list[url_counter])
        # Check to see if either of the lists are empty before comparing the k values.
        if len(j_list) == 0 and len(k_list) == 0:
            print '[INFO]: Both <Standard Server: %s> and <Tested Server: %s> lists are empty...'% (entered_env01, entered_env02)
            j_list_counter += 1
            url_counter += 1
            print ''
            continue
        elif len(j_list) == 0:
            print '[INFO]: The <Standard Server: %s> list is empty!!!'%entered_env01 
            j_list_counter += 1
            url_counter += 1
            print ''
            continue
        elif len(k_list) == 0:
            print '[ERROR]: The <Testing Server: %s> list is empty...'%entered_env02
            error_count +=1
            j_list_counter += 1
            url_counter += 1
            print ''
            continue
        for k in k_list:
            if k in j_list:
                print '[PASSED]: Found %s in the <Standard List: %s>...'%(k, j_list[item_counter])
            else:
                print '[FAILED]: Value %s was not found in list01!!!'%k
                print '[INFO]: <Standard Server: %s> Value: %s <Tested Server: %s> Value: %s'% (entered_env01, j_list[item_counter], entered_env02, k)
                error_count += 1
            item_counter += 1                           
        j_list_counter += 1
        url_counter += 1
        print ''
    print ''
    print 'Total Errors Found: %i'%error_count
    return error_count
                
if __name__ == '__main__': 
    start_time = datetime.datetime.now() 
    print '**************************** Ad Mapper Smoke Tests started at : %s on %s and %s ****************************' %(str(start_time), entered_env01, entered_env02)
    print ''
    # Setup the testing environments.
    test_env01 = apiUtils.get_test_env(entered_env01)
    test_env02 = apiUtils.get_test_env(entered_env02)
    print 'The constructed url for test_url01: %s'%test_env01
    print 'The constructed url for test_url02: %s'%test_env02
    print ''
    # Get the list of expected kvalues.
    list_of_expected_kvalues = get_k_values(test_env01)
    # Get the list of kvalues from the testing url.
    list_of_kvalues = get_k_values(test_env02)
    print ''
    # Compare the two lists of kvaules.
    print 'Now comparing both kvalues lists:'
    print '---------------------------------'
    error_counter = compare_kvalues(list_of_expected_kvalues, list_of_kvalues, error_counter)
    print ''
    end_time = datetime.datetime.now()
    print '**************************** Ad Mapper Smoke Tests ended at : %s on %s and %s ****************************' %(str(end_time), entered_env01, entered_env02)
    print ''
    
    if error_counter > 0:
        sys.exit(1)
    else:
        sys.exit(0)
 
