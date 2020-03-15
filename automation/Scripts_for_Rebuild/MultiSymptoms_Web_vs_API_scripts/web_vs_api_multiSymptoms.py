# -*- coding: utf-8 -*-
# This script compares Web results and API results for combination of 'fever' with each symptom
import requests
import urllib2
import re
import urllib

# Using package "requests" (reads JSON file)
def get_api_result(symptom):
    # xxxx
    #url = 'http://vmc-apistage02.healthline.com/api/service/2.0/sxs/condition'
    #params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&includeSymptom='
    
    # Luda
    #url = 'http://api-new.healthline.com/core/sxs/condition'
    #url = 'http://apiqa.eng.healthline.com:8080/core/sxs/condition'
    url = 'http://10.60.2.16:8080/core/sxs/condition'
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&symptoms=fever&symptoms='
    r = requests.get(url + params + symptom)
    return r
	
# Using package "requests" (reads JSON file)
def get_web_results(symptom, page_num):
    #weburl = 'http://sfc-stage02.healthline.com/symptom/'
    weburl = 'http://www.healthline.com/symptom/fever/'
    params = '?lazyLoad=true&pagenum=' + str(page_num)
    webpage = weburl + symptom + params
    rweb = requests.get(webpage)
    #print rweb
    redirect = ''
    if rweb.history:
        redirect = rweb.url.split('/')[-1]
        redirect = redirect.split('?')[0]
        rweb = requests.get(rweb.url + params)
    return rweb, redirect

#f = open('symptoms_No_fever.txt')
f = open('symptoms_not_order.txt')

out_pass = open('PASS.tsv', 'w', buffering=1)
out_number_not_match = open('WRONG_NUMBERS.tsv', 'w', buffering=1)
out_contentID_not_match = open('WRONG_CONTENT.tsv', 'w', buffering=1)
out_different_order = open('WRONG_ORDER.tsv', 'w', buffering=1)

api_err = open('api_err.txt', 'w', buffering=1)
num_api_err = 0
web_err = open('web_err.txt', 'w', buffering=1)
num_web_err = 0
no_api = open('no_api.txt', 'w', buffering=1)
num_no_api = 0
no_web = open('no_web.txt', 'w', buffering=1)
num_no_web = 0
regex1 = re.compile('\d+')
regex2 = re.compile('.*/([^/]+)')

num_symptoms = 0
num_pass = 0
num_diff_nums = 0
num_wrong_order = 0
num_wrong_rank_order = 0
num_diff_content = 0
		   
out_pass.write("Fever with Symptom\tNumber on " + \
           "Web\tNumber on API\t" + \
           "Content IDs found on API\tContent IDs found on Web\tAPI ranks\tResults\n")
		   
out_number_not_match.write("Fever with Symptom\tNumber on " + \
           "Web\tNumber on API\t" + \
           "Content IDs found on API\tContent IDs found on Web\tAPI ranks\tResults\n")

out_different_order.write("Fever with Symptom\tNumber on " + \
           "Web\tNumber on API\t" + \
           "Content IDs with ranks found on API\tContent IDs with ranks found on Web\tAPI ranks\tContent ID's with not matching ranks\tResults\n")

out_contentID_not_match.write("Fever with Symptom\tNumber on " + \
           "Web\tNumber on API\t" + \
           "Content IDs found on API\tContent IDs found on Web\tMissing in API\tMissing in Web\tAPI ranks\tResults\n")

for symptom in f:
    num_symptoms += 1
    symptom = symptom.strip()
    api_num = 0
    api_num_message = ''
    api_results = []
    api_ranks = []
    wrong_rank_web = []
    web_num = 0
    web_num_message = ''
    web_results = []
    comments = ''
    results = ''

    print "\n***************************************"
    print symptom
    print '---------------------------------------'
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
        print api_num_message
        num_no_api += 1
        no_api.write(symptom + '\n')

    if web_num == 0:
        web_num_message = "Not found in web"
        print web_num_message
        num_no_web += 1
        no_web.write(symptom + '\n')

    if api_num == 0 or web_num == 0:
        continue

    if api_num == 'Error':
        if api_num_message:
            api_num = api_num_message
        print "API Error"
        num_api_err += 1
        out.write(symptom + '\t' + str(web_num) + '\t' \
                  + str(api_num) + '\n')
        continue

    if web_num == 'Error':
        if web_num_message:
            web_num = web_num_message
        print "Web Error"
        num_api_err += 1
        out.write(symptom + '\t' + str(web_num) + '\t' \
                  + str(api_num) + '\n')
        continue

    # Select URLs from API Json file into api_result===================================

    #api_results = []

    for i in range(api_num):
        articleInfo = r.json()["data"]["articleInfo"][i]
        hrlcontentid = articleInfo[u'hrlcontentid'].encode('utf-8')
        rank = str(articleInfo[u'rank']).encode('utf-8')
        hrlcontentid = hrlcontentid.split('_')[-1]
        hrlcontentid = hrlcontentid.split('/')[-1]
        api_results.append(hrlcontentid)
        api_ranks.append(rank)
			
    # Select URLs from WEB Json file into web_results===================================

    if web_num % 20 == 0:
        page_nums = web_num / 20
    else:
        page_nums = (web_num / 20) + 1

    #web_results = []
    for page_num in range(1, page_nums + 1):
        rweb, redirect = get_web_results(enc_symptom, page_num)

        try:
            for objects in rweb.json()['results']:
                #url = objects[u'url'].encode('utf-8')
                url = objects[u'contentId'].encode('utf-8')
                #url = objects[u'diseaseIMUID']
                url = url.split('/')[-1]
                web_results.append(url)
            #print web_results
        except ValueError:
            web_err.write(symptom + ':' + ' no results on page_num ' + \
                          str(page_num) + '\n')

	# 4st case when all is good***************
    if api_results == web_results:
        num_pass += 1
        print "PASS: same order"
        results = "Pass"
        out_pass.write(symptom + '\t' + str(web_num) + '\t' + \
        str(api_num) + '\t' + \
        str(api_results) + '\t' + \
        str(web_results) + '\t' + str(api_ranks) + '\t' + results + '\t' + '\n')
	
    # 1st case********************************
    # Different api and web numbers (results will definitely be different)
    elif api_num != web_num:
        num_diff_nums += 1
        results = "Failed"
        print "Number different"
        out_number_not_match.write(symptom + '\t' + str(web_num) + '\t' + \
        str(api_num) + '\t' + \
        str(api_results) + '\t' + \
        str(web_results) + '\t' + str(api_ranks) + '\t' + results + '\t' + '\n')
	#********************************************
	
	# 2nd case**************************
    # Same results, different order
    elif api_results != web_results and sorted(api_results) == sorted(web_results):
        num_wrong_order += 1
        print "Wrong order...Checking rank order"

        # Make dictionary to save rank to each contentID
        rank_dict = dict(zip(api_results, api_ranks))

        results = "Passed"
        for api_result, web_result in zip(api_results, web_results):
            if api_result != web_result and rank_dict[api_result] != rank_dict[web_result]:
                num_wrong_rank_order += 1
                results = "Failed"
                message = "API {} - {} DOES NOT MATCH to web {} - {}".format(api_result, rank_dict[api_result], web_result, rank_dict[web_result])
                print message
                wrong_rank_web.append(message)

        for i in range(0, len(api_results)):
            api_results[i] = api_results[i] + ' - ' + rank_dict[api_results[i]]
            web_results[i] = web_results[i] + ' - ' + rank_dict[web_results[i]]

        if results != "Failed":
            wrong_rank_web = "ContentIDs not in order but have the same ranks"
            print wrong_rank_web

        out_different_order.write(symptom + '\t' + str(web_num) + '\t' + \
        str(api_num) + '\t' + \
        str(api_results) + '\t' + \
        str(web_results) + '\t' + str(api_ranks) + '\t' + str(wrong_rank_web) + '\t' + results + '\t' + '\n')

    # 3rd case**************************
    # Same number, different results
    elif sorted(api_results) != sorted(web_results):
        missing_in_api = list(set(web_results) - set(api_results))
        missing_in_web = list(set(api_results) - set(web_results))
        num_diff_content += 1
        results = "Failed"
        print "Arrays different"
        out_contentID_not_match.write(symptom + '\t' + str(web_num) + '\t' + \
        str(api_num) + '\t' + \
        str(api_results) + '\t' + \
        str(web_results) + '\t' + str(missing_in_api) + '\t' + str(missing_in_web) + \
        '\t' + '\t' + str(api_ranks) + '\t' + results + '\n')


print "\n\n\n"
print "************************************"
print "              RESULTS               "
print "************************************"

print "Total Symptoms: " + str(num_symptoms)
print "Passed: " + str(num_pass)
print "Different Number: " + str(num_diff_nums)
print "Same, but wrong order: " + str(num_wrong_order)
print "Different content: " + str(num_diff_content)

print "No API: " + str(num_no_api)
print "No Web: " + str(num_no_web)
print "API errors: " + str(num_api_err)
print "Web errors: " + str(num_web_err)

f.close()
