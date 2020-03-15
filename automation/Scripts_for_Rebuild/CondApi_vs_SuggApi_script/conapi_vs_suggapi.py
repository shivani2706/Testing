# -*- coding: utf-8 -*-
# Finds how many conditional  and suggested conditions found for each symptom
import requests
import urllib2
import re
import urllib


# For new API:
def get_api_suggest_result(symptom):
    url = 'http://apiqa.eng.healthline.com:8080/core/sxs/suggest'
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&searchTerms='
    sr = requests.get(url + params + symptom)
    return sr

# For new API:
def get_api_condition_result(symptom):
    url = 'http://apiqa.eng.healthline.com:8080/core/sxs/condition'
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&symptoms='
    cr = requests.get(url + params + symptom)
    return cr

#f = open('symptoms_part.txt')
#f = open('symptoms_sick.txt')
f = open('symptoms_no_web.txt')

out = open('condapi_vs_suggapi_out.tsv', 'w', buffering=1)
api_err = open('api_err.txt', 'w', buffering=1)
web_err = open('web_err.txt', 'w', buffering=1)
url_err = open('url_err.txt', 'w', buffering=1)
no_api = open('no_api.txt', 'w', buffering=1)
no_web = open('no_web.txt', 'w', buffering=1)
health_redirect = open('health_redirect.txt', 'w', buffering=1)
regex1 = re.compile('\d+')
regex2 = re.compile('.*/([^/]+)')
api_num = 0
api_num_message = ''
web_num = 0
web_num_message = ''

out.write("Symptom\tHow many conditions found on " + \
           "Condition API\tHow many conditions found on Suggest API\n")

for symptom in f:
    api_num = 0
    api_num_message = ''

    print "**************************************************"
    print symptom
    symptom = symptom.strip().lower()
    symptom = re.sub(r'^"|"$', '', symptom)
    enc_symptom = urllib.quote(symptom)

#Find num of Condition API =================================================
    cr = get_api_condition_result(enc_symptom)
    if cr.status_code != 200:
        c_api_num = 'Error'
        c_api_err.write(symptom + '\n')
    else:
        #print "Status 200"
        
        c_api_num = int(cr.json()[u'meta'][u'numResults'])
        #print api_num

#Find num of Suggested API =================================================
    sr = get_api_suggest_result(enc_symptom)
    if sr.status_code != 200:
        s_api_num = 'Error'
        s_api_err.write(symptom + '\n')
    else:
        #print "Status 200"
        
        s_api_num = int(sr.json()[u'meta'][u'numResults'])
        #print api_num


    out.write(symptom + '\t' + str(c_api_num) + '\t' + \
              str(s_api_num) + '\n')

f.close()
