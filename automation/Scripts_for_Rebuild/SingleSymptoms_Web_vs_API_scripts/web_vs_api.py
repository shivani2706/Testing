# -*- coding: utf-8 -*-
# Compares number of results on WEB and API (single symptom)
import requests
import urllib2
import re
import urllib
#   Returns search results on API using package "requests" (reads JSON file)
def get_api_result(symptom):
    # xxx
    #url = 'http://vmc-apistage02.healthline.com/api/service/2.0/sxs/condition'
    #params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&includeSymptom='
    
    # Luda

    url = 'http://api-new.healthline.com/core/sxs/condition'
	#url = 'http://apiqa.eng.healthline.com:8080/core/sxs/condition' -- it is QA API Server
    #url = 'http://vmc-apistage02.healthline.com/api/service/2.0/sxs/condition' --It is Old API server
    #url = 'http://10.60.2.41/api/service/2.0/sxs/condition'
    #params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&includeSymptom=' 
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&symptoms='
    r = requests.get(url + params + symptom)
    return r
#  Returns search results on WEB using package "requests" (reads JSON file)
def get_web_results(symptom, page_num):
    #weburl = 'http://sfc-stage02.healthline.com/symptom/'
	weburl = 'http://www.healthline.com/symptom/'
    params = '?lazyLoad=true&pagenum=' + str(page_num)
    webpage = weburl + symptom + params
    rweb = requests.get(webpage)
    redirect = ''
    if rweb.history:
        redirect = rweb.url.split('/')[-1]
        redirect = redirect.split('?')[0]
        rweb = requests.get(rweb.url + params)
    return rweb, redirect

f = open('symptoms.txt')

out = open('web_vs_api_out.tsv', 'w', buffering=1)
api_err = open('api_err.txt', 'w', buffering=1)
web_err = open('web_err.txt', 'w', buffering=1)
url_err = open('url_err.txt', 'w', buffering=1)
no_api = open('no_api.txt', 'w', buffering=1)
no_web = open('no_web.txt', 'w', buffering=1)
health_redirect = open('health_redirect.txt', 'w', buffering=1)
regex1 = re.compile('\d+')
regex2 = re.compile('.*/([^/]+)')

out.write("Symptom\tRedirected Symptom\tHow many conditions found on " + \
           "Web\tHow many conditions found on API\tResults\t " + \
           "Found on API but missed on Web\tFound on Web but missed on API " + \
           "\tNumber of missed on Web\tNumber of missed on API\tComments\n")

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
        api_num = 'Error'
        api_err.write(symptom + '\n')
    else:
        api_num = int(r.json()[u'meta'][u'numResults'])

#Find num of WEB ===================================================
    rweb, redirect = get_web_results(enc_symptom, 1)


    if rweb.status_code != 200:
        web_num = 'Error'
        web_num_message = "!= 200"
        web_err.write(symptom + '\n') 
    elif '/health/' in rweb.url:
        web_num = "Error"
        web_num_message = "Redirected to /health/"
        health_redirect.write(symptom + '\n') 
    else:
        try:
            web_num = int(rweb.json()[u'totalRows'])
        except ValueError:
            web_num = 'Error'
            web_num_message = "No json"
            web_err.write(symptom + ':' + ' no JSON\n')

# Check if results equal========================================
    if api_num == 0:
        api_num_message = "Not found in API"
        no_api.write(symptom + '\n')
    if web_num == 0:
        web_num_message = "Not found in web"
        no_web.write(symptom + '\n')

    if api_num == 'Error' or web_num == 'Error':
        if api_num_message:
            api_num = api_num_message
        if web_num_message:
            web_num = web_num_message

        print "Error"
        out.write(symptom + '\t' + redirect + '\t' + str(web_num) + '\t' \
                  + str(api_num) + '\n')
        continue

# Select URLs from API Json file into api_result===================================

    api_results = []

    for i in range(api_num):
        # api_results.append(r.json()["data"]["articleInfo"][i][u'itemtitle'].encode('utf-8'))
        a = r.json()["data"]["articleInfo"][i][u'url'].encode('utf-8')
        a = a.split('_')[-1]
        a = a.split('/')[-1]
        api_results.append(a)
	
# Select URLs from WEB Json file into web_results===================================

    if web_num % 20 == 0:
        page_nums = web_num / 20
    else:
        page_nums = (web_num / 20) + 1

    web_results = []
    for page_num in range(1, page_nums + 1):
        rweb, redirect = get_web_results(enc_symptom, page_num)

        try:
            for objects in rweb.json()['results']:
                url = objects[u'url'].encode('utf-8')
                url = url.split('/')[-1]
                web_results.append(url)
        except ValueError:
            web_err.write(symptom + ':' + ' no results on page_num ' + \
                          str(page_num) + '\n')


    miss_api = list(set(api_results) - set(web_results))
    miss_web = list(set(web_results) - set(api_results))

    if api_num == web_num:
        result = "passed"
        print result
        out.write(symptom + '\t' + redirect + '\t' + str(web_num) + '\t' + \
              str(api_num) + '\t' + result + '\t' + \
              str(miss_api) + '\t' + \
              str(miss_web) + '\t' + str(len(miss_api)) + \
              '\t' + str(len(miss_web)) + '\t' + comments + '\n')
        continue
    else:
        result = "failed"

    if web_num == 0 and api_num != 0:
        result = "passed"
        comments = 'No results in web, but results in api found'

    if web_num - api_num == 1:
        result = "passed"
        comments = "Web results exactly one more than api results"

    out.write(symptom + '\t' + redirect + '\t' + str(web_num) + '\t' + \
              str(api_num) + '\t' + result + '\t' + \
              str(miss_api) + '\t' + \
              str(miss_web) + '\t' + str(len(miss_api)) + \
              '\t' + str(len(miss_web)) + '\t' + comments + '\n')

    print "api_num: " + str(api_num)
    print "web_num: " + str(web_num)
    print "Missing in API: " + str(miss_web)
    print "Missing in Web: " + str(miss_api)
    print "Result: " + result + "\n"

f.close()
