# -*- coding: utf-8 -*-
# Checks item title for each symptom in API call results 
import requests
import urllib2
import re
import urllib

def get_api_result(symptom):
    url = 'http://apiqa.eng.healthline.com:8080/core/sxs/condition'
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&symptoms='
    r = requests.get(url + params + symptom)
    return r

f = open('symptoms.txt')
out = open('itemtitle_out.tsv', 'w', buffering=1)
out.write("Symptom\Item Title\tRelated Symptoms\n")

for symptom in f:
    api_num = ''
    itemtitle = ''
    result_item = ''
    result_related = ''

    print symptom
    symptom = symptom.strip().lower()
    symptom = re.sub(r'^"|"$', '', symptom)
    enc_symptom = urllib.quote(symptom)

    r = get_api_result(enc_symptom)
    
    api_num = int(r.json()[u'meta'][u'numResults'])

    try:
	    articleInfo = r.json()['data']['articleInfo']
	    for info in articleInfo:
	        itemtitle = info[u'itemtitle'].encode('utf-8').strip()

	        if itemtitle.lower() == symptom.lower():
	            result_item = 'Fail'
	            break
    except Exception, e:
		out.write(symptom + '\tError: {}\n'.format(repr(e)))
		continue

    try:
	    relatedSymptoms = r.json()['data']['relatedSymptoms']
	    for related_symptom in relatedSymptoms:
	        cfn = related_symptom['cfn'].encode('utf-8').strip()

	        if cfn.lower() == symptom.lower():
	            result_related = 'Fail'
    except Exception, e:
		out.write(symptom + '\tError: {}\n'.format(repr(e)))
		continue

    if result_item != 'Fail':
        result_item = 'Pass'

    if result_related != 'Fail':
        result_related = 'Pass'

    print 'Num Results: ' + str(api_num)
    print 'Item Result: ' + result_item
    print 'Related Result: ' + result_related

    out.write('{}\t{}\t{}\n'.format(symptom, result_item, result_related))

f.close()
