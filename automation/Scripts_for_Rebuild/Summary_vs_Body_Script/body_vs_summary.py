# -*- coding: utf-8 -*-

import requests
import urllib2
import re
import urllib

def get_api_result(symptom):
    url = 'http://apiqa.eng.healthline.com:8080/core/sxs/condition'
    params = '?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&symptoms='
    r = requests.get(url + params + symptom)
    return r

def clean_string(data):
    reg = re.compile(r'<.*?>')
    data = reg.sub('', data)
    data = data.replace('\r','')
    data = data.replace('\n',' ')

    return data

f = open('symptoms.txt')

out = open('body_vs_summary.tsv', 'w', buffering=1)

out.write('Symptom\tResult\tSummary\tBody\n')

for symptom in f:
    articleSummary = ''
    articleBody = ''

    print symptom
    symptom = symptom.strip().lower()
    symptom = re.sub(r'^"|"$', '', symptom)
    enc_symptom = urllib.quote(symptom)

    r = get_api_result(enc_symptom)

    # from IPython import embed
    # embed()
    try:
        articleSummary = r.json()['data']['healthlineContent']['articleSummary'].encode('utf-8')
        articleBody = r.json()['data']['healthlineContent']['articleBody'].encode('utf-8')
    except TypeError:
        out.write(symptom + '\tSummary/Body not found\n')
        continue
    except Exception, e:
        out.write(symptom + '\tError: {}\n'.format(repr(e)))
        continue

    articleSummary = clean_string(articleSummary)
    articleBody = clean_string(articleBody)
    
    # We use 247 to no include ('...')
    trimmed_summary = articleSummary[:-3]
    len_summary = len(trimmed_summary)
    trimmed_body = articleBody[:len_summary]

    if trimmed_summary == trimmed_body:
        result = 'Pass'
    else:
        result = 'Fail'

    out.write('{}\t{}\t{}\t{}\n'.format(symptom, result, trimmed_summary, trimmed_body))

f.close()
