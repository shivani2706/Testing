# -*- coding: utf-8 -*-

# Finds symptoms which don't return status 200

import requests
import urllib2
import re
import urllib

def get_api_result(symptom):
    # xxx
    #url = 'http://vmc-apistage02.healthline.com/api/service/2.0/sxs/condition'
    #params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&includeSymptom='
    
    
    #-- it is new API Servers
    url = 'http://apiqa.eng.healthline.com:8080/core/sxs/condition' 
    #url = 'http://api-stage.healthline.com:8080/core/sxs/condition' 
    #url = 'http://api-new.healthline.com/core/sxs/condition'
    #url = 'http://vmc-apistage02.healthline.com/api/service/2.0/sxs/condition' --It is Old API server
    #url = 'http://10.60.2.41/api/service/2.0/sxs/condition'  -- MZ server
    #params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&includeSymptom='  --It is Old parameter
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&symptoms='
    r = requests.get(url + params + symptom)
    return r

f = open('symptoms.txt')
#f = open('symptoms_binge.txt')

api_err = open('api_err.txt', 'w', buffering=1)

for symptom in f:
    api_num = 0
    api_num_message = ''
    web_num = 0
    web_num_message = ''
    comments = ''

    print "***************************************"
    print symptom
    symptom = symptom.strip().lower()
    symptom = re.sub(r'^"|"$', '', symptom)
    enc_symptom = urllib.quote(symptom)

#Find num of API =================================================
    r = get_api_result(enc_symptom)
    if r.status_code != 200:
        print "Status_code != 200" 
        api_err.write(symptom + '\n')
  
f.close()
