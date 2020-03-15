# -*- coding: utf-8 -*-

# Checks Ks values in API call results for each symptom and creates report with results

import requests
import urllib2
import re
import urllib

def get_api_result(symptom):
    url = 'http://apiqa.eng.healthline.com:8080/core/sxs/condition'
    #url = 'http://api-stage.healthline.com:8080/core/sxs/condition' 
    #url = 'http://api-new.healthline.com/core/sxs/condition' 
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&symptoms='
	
    r = requests.get(url + params + symptom)
    return r

f = open('symptoms.txt')

out = open('api_ads.tsv', 'w', buffering=1)

out.write('Symptom\tk1\tk2\tk3\n')

for symptom in f:
    print symptom
    symptom = symptom.strip().lower()
    symptom = re.sub(r'^"|"$', '', symptom)
    enc_symptom = urllib.quote(symptom)

    k1 = ''
    k2 = ''
    k3 = ''

    r = get_api_result(enc_symptom)
    try:
        k1 = r.json()['ads']['k1'].encode('utf-8')
        k2 = r.json()['ads']['k2'].encode('utf-8')
        k3 = r.json()['ads']['k3'].encode('utf-8')
    except Exception, e:
        out.write(symptom + '\tError: {}\n'.format(repr(e)))
        continue

    out.write('{}\t{}\t{}\t{}\n'.format(symptom, k1, k2, k3))

f.close()
