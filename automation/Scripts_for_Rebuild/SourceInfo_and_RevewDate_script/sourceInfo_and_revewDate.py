# -*- coding: utf-8 -*-
# Checks if API returns data with Source info and review date
import requests
import urllib2
import re
import urllib

def get_api_result(symptom):
    url = 'http://apiqa.eng.healthline.com:8080/core/sxs/condition'
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&symptoms='
    #url = 'http://vmc-apistage02.healthline.com/api/service/2.0/sxs/condition'
    #params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&includeSymptom='
    r = requests.get(url + params + symptom)
    return r

def clean_string(data):
    reg = re.compile(r'<.*?>')
    data = reg.sub('', data)
    data = data.replace('\r','')
    data = data.replace('\n',' ')

    return data

f = open('symptoms.txt')

out = open('sourceInfo_and_reviewDate.tsv', 'w', buffering=1)

out.write('Symptom\tResult for SourceInfo\tResult for ReviewDate\n')

for symptom in f:
    sourceInfo = ''
    reviewDate = ''

    print symptom
    symptom = symptom.strip().lower()
    symptom = re.sub(r'^"|"$', '', symptom)
    enc_symptom = urllib.quote(symptom)

    r = get_api_result(enc_symptom)

    # from IPython import embed
    # embed()
    try:
        #sourceInfo = r.json()['data']['healthlineContent']['sourceInfo'].encode('utf-8')
        #reviewDate = r.json()['data']['healthlineContent']['reviewDate'].encode('utf-8')
        sourceInfo = r.json()['data']['healthlineContent']['sourceInfo']
        reviewDate = r.json()['data']['healthlineContent']['reviewDate']
    except TypeError:
        out.write(symptom + '\tSource/ReviewDate not found\n')
        continue
    except Exception, e:
        out.write(symptom + '\tError: {}\n'.format(repr(e)))
        continue

    #sourceInfo = clean_string(sourceInfo)
    #reviewDate = clean_string(reviewDate)
    
    # We use 247 to no include ('...')
    #trimmed_summary = articleSummary[:-3]
    #len_summary = len(trimmed_summary)
    #trimmed_body = articleBody[:len_summary]

    if sourceInfo != ' ':
        result = 'Pass'
    else:
        result = 'Fail'

    if reviewDate != ' ':
        result_1 = 'Pass'
    else:
        result_1 = 'Fail' 		

    out.write('{}\t{}\t{}\n'.format(symptom, result, result_1))

f.close()
