# Use this script within selenium test: HeaderBidding.java

import sys
import json
import re
from haralyzer import HarParser, HarPage
from httplib import responses
import argparse
from urlparse import urlparse
import re
import collections

entries_list = []
queryString_list = []
temp_list = []
inv_map = {}
queryStringValues = []
prev_scp_values = []
har_path_01 = '..\..\CaptureNetworkTraffic\Archive 16-12-04 15-58-52.har'
har_path_02 = '..\..\CaptureNetworkTraffic\sfc-stage02.healthline.com.StandartArticle.har'
har_path_03 = '..\..\CaptureNetworkTraffic\www.healthline.com.har'
json_file = ''


def get_har_file(harfile_path):
    #Returns the json from the harfile path.
    harfile = open(harfile_path)
    harfile_json = json.loads(harfile.read())
    return harfile_json

def har_to_csv(harfile_path):
    ''''Reads a har file from the filesystem, converts to CSV, then dumps to
    stdout.
    '''
    harfile = open(harfile_path)
    harfile_json = json.loads(harfile.read())
    i = 0
    for entry in harfile_json['log']['entries']:
        i = i + 1
        url = entry['request']['url']
        urlparts = urlparse(entry['request']['url'])
        size_bytes = entry['response']['bodySize']
        size_kilobytes = float(entry['response']['bodySize'])/1024
        mimetype = 'unknown'
        if 'mimeType' in entry['response']['content']:
            mimetype = entry['response']['content']['mimeType']

        print '%s,"%s",%s,%s,%s,%s' % (i, url, urlparts.hostname, size_bytes,
                                       size_kilobytes, mimetype)
        
def nested_dict_iter(nested_dict):
    for key, value in nested_dict.iteritems():
        if isinstance(value, collections.Mapping):
            for inner_key, inner_value in nested_dict_iter(value):
                yield inner_value
        else:
            yield value
        
'''def nested_dict_iter(nested_dict):
    list_of_vaules = []
    for k, v in nested_dict.iteritems():
        if isinstance(v, dict):
            nested_dict_iter(v)
    else:
        list_of_vaules.append(v)
    return list_of_vaules '''

def get_values(list_of_entries):
    '''
    Takes a list of something and grab the values 
    puts them in a list and returns the list
    '''
    list_of_values = []
    pattern = 'pos=.|.'
    
    for entry in list_of_entries:
        values = entry.values()
        for value in values:
            if 'prev_scp' in str(value):
                list_of_values.append(value)
            else:
                pass
    return list_of_values

def get_from_key(list_of_dict):
    # get the value      from the key in generic terms.
    found_entry_list = []
    pattern = 'pos=.|.'
    for d in list_of_dict:
        for key in d:
            if d[key] == 'prev_scp':
                found_entry_list.append(d[key])
            elif re.match(pattern, str(d[key])) == True:
                found_entry_list.append(d[key])
                break
            else:
                pass 
    return found_entry_list
             
def get_recursively(search_dict, field):
    """
    Takes a dict with nested lists and dicts,
    and searches all dicts for a key of the field
    provided.
    """
    fields_found = []
    for search in search_dict:
        for key, value in search.iteritems():

            if key == field:
                fields_found.append(value)

            elif isinstance(value, dict):
                results = get_recursively(value, field)
                for result in results:
                    fields_found.append(result)

            elif isinstance(value, list):
                for item in value:
                    if isinstance(item, dict):
                        more_results = get_recursively(item, field)
                        for another_result in more_results:
                            fields_found.append(another_result)

    return fields_found

def get_correlator(harfile_path):
    correlator = ''
    
    harfile = open(harfile_path)
    harfile_json = json.loads(harfile.read())
    for entry in harfile_json['log']['entries']:
        correlator = entry['request']['queryString']['correlator']
        print 'correlator: %i'%correlator
    return correlator

'''def get_prev_scp(harfile_path):
    prev_scp = ''
    harfile = open(harfile_path)
    harfile_json = json.loads(harfile.read())
    prev_scp = harfile_json['log']['entries']['request']['queryString']['prev_scp']
    print 'prev_scp: %i'%prev_scp '''

def get_queryString(harfile_path):
    value_list = []
    
    
    with open(harfile_path, 'r') as f:
        har_page = HarPage('page_2', har_data=json.loads(f.read()))
        value_list = har_page.filter_entries("GET", "TEXT/CSS", 200, None, False)
        print 'har_page_queryString: %s'%value_list
    return value_list
    
        
def get_prev_scp(harfile_path):
    values_list = []
    found_entry_list = []
    pattern = 'pos=.'
    
    with open(harfile_path, 'r') as f:
        har_page = HarPage('page_2', har_data=json.loads(f.read()))

    print har_page.hostname
    print har_page.url
    # Grab the response entries from the .har file.
    entries_list = har_page.entries
    # Traverse through the list of unconventional dictionary and get the values (some values are list of dictionaries.
    print 'entries_list: %s'%entries_list
    values_list = get_values(entries_list)
    # values_list = get_recursively(entries_list, 'value')
    print 'values_list: %s'%values_list
    # Now we go through the list of dictionaries.
    found_entry_list = [d['name'] == 'prev_scp' for d in values_list if 'name' in d]
    # found_entry_list = get_from_key(values_list)
    print 'found_entry_list: %s'%found_entry_list
    '''
    # found_entry_list is a json.
    # now we have a list of dictionaries.
    queryStringValues = found_entry_list["queryString"]
    for value in queryStringValues:
        if value['name'] == 'prev_scp':
            prev_scp_values.append(value)
            break
    print 'prev_scp_values: %s'%prev_scp_values
    print prev_scp_values[0]['value'] '''
    
def get_prev_scp_org(harfile_path):
    found_entry_list = []
    
    with open(harfile_path, 'r') as f:
        har_page = HarPage('page_2', har_data=json.loads(f.read()))

    print har_page.hostname
    print har_page.url
    # Grab the reponse entries from the .har file.
    entries_list = har_page.entries
    # Traverse through the list of unconventional dictionary.
    print entries_list
    for entry in entries_list:
        temp_list = entry.values()
        # Get the dictionary that we are most interested in which contains prev_scp.
    for temp in temp_list:
        if 'prev_scp' in str(temp):
            found_entry_list = temp
            break
    print found_entry_list
    # found_entry_list is a json.
    # now we have a list of dictionaries.
    queryStringValues = found_entry_list["queryString"]
    for value in queryStringValues:
        if value['name'] == 'prev_scp':
            prev_scp_values.append(value)
            break
    print 'prev_scp_values: %s'%prev_scp_values
    print prev_scp_values[0]['value']

def get_entries_list(harfile_path):
    # Returns a sliced list which has the pertinent information.
    sliced_list = []
    temp_list = []
    prev_scp_values = []
    with open(harfile_path, 'r') as f:
        har_page = HarPage('page_2', har_data=json.loads(f.read()))

    print har_page.hostname
    print har_page.url
    # Grab the reponse entries from the .har file.
    entries_list = har_page.entries
    # Traverse through the list of unconventional dictionary.
    print 'entries_list: %s'%entries_list
    # print 'entries_list length is: %i'%len(entries_list)
    # Now let's cut the entries_list in half and take the last half and see what's in it.
    # print 'The two numbers for the slice are: %i, %i'%(len(entries_list)/8, len(entries_list)/2)
    # print 'Last half of the entries_list is: %s'%entries_list[(len(entries_list)/8):len(entries_list)/2]
    sliced_list = entries_list[(len(entries_list)/7):len(entries_list)/2]
    # print 'sliced_list length: %i'%len(sliced_list)
    # print 'The two numbers for the slice_list are: %i, %i'%((len(sliced_list)/32) + 4, len(sliced_list) - 1)
    print 'first half of the sliced_list is: %s'%sliced_list
    print 'length of sliced_list: %i'%len(sliced_list)
    print 'sliced_list[10] is: %s'%sliced_list[10]
    # print 'first half of the sliced_list is: %s'%sliced_list[(len(sliced_list)/40) + 4 :(len(sliced_list)/2) - 1]
    # temp_list = sliced_list[1].values()
    # print 'temp_list is: %s'% temp_list
    '''for item in temp_list:
        if 'prev_scp' in item:
            prev_scp_values = item
            break
    print prev_scp_values '''
    return sliced_list 

 
if __name__ == '__main__':
    # har_to_csv(har_path) 
    # get_correlator(har_path_01)
    # get_queryString(har_path_01)
    get_entries_list(har_path_02)
    #print 'queryString: %s'%json_file['log']['entries']['request']['queryString']