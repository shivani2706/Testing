'''
Author: chaz
Date: 12.14.17 
Script: apiUtils.py
Description: Some useful methods for 
api automation.
'''
import json
import requests
#from requests_oauthlib import OAuth1
import re
from bs4 import BeautifulSoup
import datetime
import codecs
from random import randint
#import httplib2
import os
from xml.dom import minidom
import urllib2
import time
import collections
import pymysql.cursors

#import unittest
#from apiclient import discovery
#from oauth2client import client
#from oauth2client import tools
#from oauth2client.file import Storage

'''try:
    import argparse
    flags = argparse.ArgumentParser(parents=[tools.argparser]).parse_args()
except ImportError:
    flags = None '''
    
SCOPES = 'https://www.googleapis.com/auth/spreadsheets.readonly'
CLIENT_SECRET_FILE = 'client_secret.json'
api_requests_dict = {'colors':'/api/service/2.0/pill/label/color?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078', 
                     'shapes':'/api/service/2.0/pill/label/shape?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078',
                     'color':'/api/service/2.0/pill/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&colors=',
                     'shape':'/api/service/2.0/pill/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&shape=',
                     'markings':'/api/service/2.0/pill/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&markings=',
                     'name':'/api/service/2.0/pill/images?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078&name='}
pill_with_no_results = []
protocol = "http://"
hl_partner_id = "31a86f67-b1b6-4fd7-8b98-8caeee8d3078"
nurture_partner_id = "8346ec12-2e6f-4744-9e77-64e4b935cc38"
'''def get_credentials(credential_dir, json_name, APPLICATION_NAME):
    """Gets valid user credentials from storage.

    If nothing has been stored, or if the stored credentials are invalid,
    the OAuth2 flow is completed to obtain the new credentials.

    Returns:
        Credentials, the obtained credential.
    """
    cwd = os.getcwd()
    credential_dir = os.path.join(cwd, credential_dir)
    if not os.path.exists(credential_dir):
        os.makedirs(credential_dir)
    credential_path = os.path.join(credential_dir,
                                   json_name)

    store = Storage(credential_path)
    credentials = store.get()
    if not credentials or credentials.invalid:
        flow = client.flow_from_clientsecrets(CLIENT_SECRET_FILE, SCOPES)
        flow.user_agent = APPLICATION_NAME
        if flags:
            credentials = tools.run_flow(flow, store, flags)
        else: # Needed only for compatibility with Python 2.6
            credentials = tools.run_flow(flow, store)
            #credentials = tools.run(flow, store)
        print('Storing credentials to ' + credential_path)
    return credentials '''

def get_env(env):
    # Use to get the testing domain for api.
    test_env = ''
    domain = ''
    if env == 'stage02':
        test_env = "vmc-apistage02"
        domain = ".healthline.com"
    elif env == 'stage01':
        test_env = "vmc-apistage01"
        domain = ".healthline.com"
    elif env == 'stage05':
        test_env = "api"
        domain = ".healthline.com"
    elif env == 'sfc-api1':
        test_env = "sfc-api1"
        domain = ".healthline.com"
    elif env == 'njc-api1':
        test_env = "vmj-api01-prod"
        domain = ".healthline.com"
    elif env == 'dion':
        testing_env = '10.1.0.108'
        domain = ''
    elif env == 'michael':
        test_env = '10.1.0.113'
        domain = ''
    elif test_env == 'oleg':
        test_env = '10.1.0.165'
        domain = ''
    elif test_env == 'prod':
        test_env = "api2"
        domain = ".healthline.com"
    test_url = protocol + test_env + domain
    return test_url

# Methods for the Rebuild Project.
def get_reb_env(env):
    # Use to get the rebuild testing and aws domain for api.
    test_env = ''
    protocol_ssl = 'https://'
    domain = ".healthline.com:"
    port = '8080'
    
    if env == 'qa':
        test_env = 'apiqa.eng'
    elif env == 'stage':
        test_env = 'api-stage'
    elif env == 'api-new':
        test_env = 'api-new'
    elif env == 'api01':
        test_env = 'api01.eng'
    elif env == 'api02':
        test_env = 'api02.eng'
    elif env == 'sfc-api1':
        test_env = 'sfc-api1'
    elif env == 'sfc-api2':
        test_env = 'sfc-api2'
    elif env == 'njc-api01':
        test_env = 'vmj-api01-prod'
    elif env == 'njc-api02':
        test_env = 'njc-api02-prod'
    elif env == 'njc-api03':
        test_env = 'njc-api03-prod'
    elif env == 'qa_gateway':
        test_env = 'lfvg4efl0k.execute-api.us-east-1.amazonaws.com/qa'
    elif env == 'stage_gateway':
        test_env = 'lfvg4efl0k.execute-api.us-east-1.amazonaws.com/stage'  
    elif env == 'prod_gateway':
        test_env = 'lfvg4efl0k.execute-api.us-east-1.amazonaws.com/prod'
    if env in ('qa', 'stage', 'api-new', 'api01', 'api02', 'api03', 'api04', 'api05', 'api06'):
        test_url = protocol + test_env + domain + port
    elif env == 'qa_gateway':
        test_url = protocol_ssl + test_env
    elif env == 'stage_gateway':
        test_url = protocol_ssl + test_env
    elif env == 'prod_gateway':
        test_url = protocol_ssl + test_env
    else:
        test_url = protocol + test_env + domain
    return test_url
    
def get_test_server(test_env):
    test_server = ''
    
    if test_env == 'qa' or test_env == 'qa_gateway':
        test_server = 'https://sfc-stage02.healthline.com'
    elif test_env == 'stage' or test_env == 'stage_gateway':
        test_server = 'https://sfc-stage01.healthline.com'
        # test_server = 'https://frontend-stage.healthline.com'
    elif test_env in ('prod', 'prod_gateway', 'api01', 'api02', 'api-new'):
        test_server = 'https://www.healthline.com'
    return test_server

def get_article_links(test_env, sitemap_url, article_type):
    # Use to get the article urls from HL Sitemap.
    articles_list = []
    hl_articles_list = []
    reg_pattern = 'https:\/\/\w+\.\w+.com'
    article_pattern = ''
    debug = True
    
    # Set the article_pattern based on passed in article type.
    if article_type == 'Health':
        article_pattern = '^\/health\/.*'
    elif article_type == 'Health News':
        article_pattern = '^\/health-news\/.*'
    elif article_type == 'Nutrition':
        article_pattern == '^\/nutrition\/.*'
        
    # Make the request to get the xml of the sitemap passed in.
    url = get_test_server(test_env) + sitemap_url
    if debug:
        print '[Info]: The sitemap url is: %s'%url
    # parse the data from the Sitemap - xml.
    dom = minidom.parse(urllib2.urlopen(url)) 
    link_list = dom.getElementsByTagName('loc')
    for node in link_list:
        articles_list.append(re.sub(reg_pattern, "", node.firstChild.data))
    if debug:
        print '[Info]: articles_list: %s'%articles_list
    for article in articles_list:
        if(re.match(article_pattern, article)):
            hl_articles_list.append(article)
    if debug:
        print '%s Articles List: %s'%(article_type, hl_articles_list)
    return hl_articles_list

def get_data_news_feed(test_env):
    # Use to get the news feed via api
    data_news_feed_api = '/core/news/feed.json?partnerId='
    dnf_api_call = get_reb_env(test_env) + data_news_feed_api + hl_partner_id + '&url='
    
    dnf_json = make_request(dnf_api_call, True, True)
    return dnf_json

def get_data_news_feed_sc(test_env, start, rows):
    # Use to get the news feed via api passed in integers for start and number of rows to fetch.
    data_news_feed_api = '/core/news/feed.json?partnerId='
    dnf_api_call = get_reb_env(test_env) + data_news_feed_api + hl_partner_id + '&start=' + str(start) + '&rows=' + str(rows)
    error_msg = 'Invalid input param(s) had been detected: start= -1 rows= 10'
    
    dnf_json = make_request(dnf_api_call, True, True)
    if "meta" not in dnf_json:
        if dnf_json["_hl_results"]["num_results_returned"] == rows:
            print 'Test Get Data News Feed start = 0 and rows = 10 Passed'
    elif dnf_json["meta"]["status"] == error_msg:
        print 'Test Get Data News Feed start = -1 and rows = 10 Passed'
    else:
        print 'Test Get Data News Feed start = 0 and rows = 10 Failed!!!'
    return dnf_json

def get_health_feed(test_env):
    # Use to get the health feed.
    hf_api = '/core/health/feed.json?partnerId='
    hf_api_call = get_reb_env(test_env) + hf_api + hl_partner_id
    hf_json = make_request(hf_api_call, True, True)
    return hf_json

def get_nutrition_feed(test_env):
    # Use to get the Nutrition feed.
    nf_api = '/core/nutrition/feed.json?partnerId='
    nf_api_call = get_reb_env(test_env) + nf_api + hl_partner_id
    nf_json = make_request(nf_api_call, True, True)
    return nf_json

def check_health_feed(test_env):
    ''' Use to check the /core/health/feed.json
        and make sure that the pattern /human-body-maps/ 
        is not in any of the urls
    '''
    check_url = '/human-body-maps/'
    hf_api = '/core/health/feed.json?partnerId='
    hf_api_call = get_reb_env(test_env) + hf_api + hl_partner_id
    article_list = []
    error_list = []
    
    hf_json = make_request(hf_api_call, True, True)
    # Get the article url and store it in a list.
    if hf_json["items"] != None:
        for item in hf_json["items"]:
            article_list.append(item["url"])
    # Check to see if the check_url pattern is the article_list.
        for article in article_list:
            if check_url in article_list:
                print '[Warning]: Found an article: &s with the pattern: %s'&(article, check_url)
                error_list.append(article)
        if len(error_list) > 0:
            print '[Failed]: Test Check Health Feed for /human-body-maps/ pattern failed!!!'
            print '[Info]: The articles are: %s'%error_list
        else:
            print '[Passed]: Test Check Health Feed for /human-body-maps/ pattern Passed'  
            
def check_diabetes_mine_feed(test_env):
    # Use to get the Diabetes Mined feed
    dm_api = '/core/diabetesmine/feed.json?partnerId='      
    dm_api_call = get_reb_env(test_env) + dm_api + hl_partner_id
    article_list = []
    
    dm_json = make_request(dm_api_call, True, True)
    # Get the article url and store it in a list.
    if dm_json["items"] != None:
        for item in dm_json["items"]:
            article_list.append(item["url"])
        if article_list > 0:
            print '[Passed]: Test Check Diabetes Mine Passed'
            print '[Info]: Articles found for Didbetes Mine: %s'%article_list
        else:
            print '[Failed]: No articles were found for Diabetes Mine!!!'
        
def get_article_content(test_env, article_url):
    # Use to get the aricle content via api.
    # Response json is returned.
    article_content_api = '/core/article/content?partnerId='
    ac_api_call = get_reb_env(test_env) + article_content_api + hl_partner_id + '&url=' + article_url
    error_msg_01 = 'Incorrect service: /article/content has been used for URL: '
    error_msg_02 = '\nunfortunately, we are unable to determine correct service API for above URL'
    status_msg = error_msg_01 + article_url + error_msg_02
    
    try:
        ac_json = make_request(ac_api_call, True, True)
    except Exception as e:
        print 'Exception: %s'%e.args
    # Storing reccomended content into a list.
    if 'nausea' in article_url:
        if ac_json["data"]["sponsored_landing_data"] != None:
            print '[Passed]: Found the Sponsored Landing Data:'
            print '[Info]: %s'%ac_json["data"]["sponsored_landing_data"]   
    elif "recommended" in ac_json:
        recommended_list = ac_json["recommended"]
        if recommended_list != None:
            article_title_list = [d['title'] for d in recommended_list if 'title' in d]
            print 'Get Article Content Test Passed'
            if len(article_title_list) > 0:
                print 'There is content in the Recommended section, there are %i articles'%len(article_title_list)
                print 'Article Tiles: %s'%article_title_list
        else:
            print '[Error]: The Recommended section is empty!!!'
    else:
        if ac_json["meta"]["code"] == 400 and ac_json["meta"]["status"] == status_msg:
            print 'Get Article Content - Full Url Test Passed'
        else:
            print 'Get Article Content Test Failed!!!'
    print ''
    return ac_json

def get_article_contents(test_env, article_url):
    # Use to get the aricle content via api.
    # Response json is returned.
    article_content_api = '/core/article/content?partnerId='
    ac_api_call = get_reb_env(test_env) + article_content_api + hl_partner_id + '&url=' + article_url
    try:
        ac_json = make_request(ac_api_call, True, True)
    except Exception as e:
        print 'Exception: %s'%e.args
    return ac_json

def get_news_content(test_env, article_url):
    # Used to get the health-news contents via api.
    news_content_api = '/core/news/content?partnerId='
    nc_api_call = get_reb_env(test_env) + news_content_api + hl_partner_id + '&url=' + article_url
    
    try:
        nc_json = make_request(nc_api_call, True, True)
    except Exception as e:
        print 'Exception: %s'%e.args
    return nc_json

def check_metadata(test_env, article_url):
    ''' Use to check the metadata section for:
        - Facebook
        - twitter
        - og
        - og:url
        - canonical
    '''
    check_api = '/core/article/content?partnerId='
    check_api_call = get_reb_env(test_env) + check_api + hl_partner_id + '&url=' + article_url
    
    try:
        check_json = make_request(check_api_call, True, True)
        if check_json["metadata"] != None:
            print 'og:image: %s'%check_json["metadata"]["metaTags"]["og:image"]
            print 'og:type: %s'%check_json["metadata"]["metaTags"]["og:type"]
            print 'twitter:title: %s'%check_json["metadata"]["metaTags"]["twitter:title"]
            print 'twitter:card: %s'%check_json["metadata"]["metaTags"]["twitter:card"]
            print 'og:site_name: %s'%check_json["metadata"]["metaTags"]["og:site_name"]
            print 'og:title: %s'%check_json["metadata"]["metaTags"]["og:title"]
            print 'og:description: %s'%check_json["metadata"]["metaTags"]["og:description"]
            print 'twitter:creator: %s'%check_json["metadata"]["metaTags"]["twitter:creator"]
            print 'twitter:image: %s'%check_json["metadata"]["metaTags"]["twitter:image"]
            print 'fb:app_id: %s'%check_json["metadata"]["metaTags"]["fb:app_id"]
            print 'twitter:site: %s'%check_json["metadata"]["metaTags"]["twitter:site"]
            print 'twitter:description: %s'%check_json["metadata"]["metaTags"]["twitter:description"]
            print 'og:url: %s'%check_json["metadata"]["metaTags"]["og:url"]
            print 'canonical: %s'%check_json["metadata"]["canonical"]
            print '[Passed]: Test Check Metadata: Facebook, twitter, og, canonical passed'
        else:
            print '[Failed]: Metadata did not contain any Facebook, twitter, og or canonical data!!!'
    except Exception as e:
        print 'Exception: %s'%e.args

def check_msite(test_env, article_url):
    # Use to check the msiteID and msiteactive values.
    msite_api = '/core/article/content?partnerId='
    msite_api_call = get_reb_env(test_env) + msite_api + hl_partner_id + '&url=' + article_url 
    
    try:
        check_msite_json = make_request(msite_api_call, True, True)
        if check_msite_json["ads"] != None:
            print 'msiteID: %s'%check_msite_json["ads"]["msiteID"]
            print 'msiteactive: %s'%check_msite_json["ads"]["msiteactive"]      
            if check_msite_json["ads"]["msiteactive"]  == True:
                print '[Passed]: Test Check msiteId & msiteactive value passed'
            else:
                print '[Failed]: Test Check msiteId & msiteactive value failed!!!'
            
    except Exception as e:
        print 'Exception: %s'%e.args

def check_recommended(test_env, article_url):
    ''' Use to make sure that the article you are
        is not listed in the recommended field of
        article urls.
    '''
    rec_articles_list = []
    article_list = []
    rec_api = '/core/article/content?partnerId='
    rec_api_call = get_reb_env(test_env) + rec_api + hl_partner_id + '&url=' + article_url
    
    try:
        rec_json = make_request(rec_api_call, True, True)
        if rec_json["recommended"] != None:
            rec_articles_list = rec_json["recommended"]
        for article in rec_articles_list:
            article_list.append(article["txtUrls"][0]["url"])
        # Check to see if the article you're on is in the list.
        if article_url in article_list:
            print "[Failed]: Found the article I'm on: %s in the Recommended Articles List %s"%(article_url, article_list)
        else:
            print "[Info]: The article I'm on: %s was not in the Recommened Articles list %s"%(article_url, article_list)
            print "[Passed]: Test 02e: Check Recommended Section Article passed"
            
    except Exception as e:
        print 'Exception: %s'%e.args
    print ''

def check_for_stripped_htlm_tags(test_env, article_url):
    ''' Use to check an article that was created or modified
        with embredded html tags ie. <br><quote>blah</quote>
    '''    
    c_api = '/core/article/content?partnerId='
    c_api_call = get_reb_env(test_env) + c_api + hl_partner_id + '&url=' + article_url
    html_tags = "<br><quote>blah</quote>"
    
    try:
        c_json = make_request(c_api_call, True, True)
        if c_json["data"]["serp"] != None:
            serp_text = c_json["data"]["serp"]
            print '[Info]: The serp is %s'%serp_text
        if html_tags in serp_text:
            print '[Failed]: The html tags %s were not stripped!!!'%html_tags
        else:
            print '[Passed]: The html tags %s were stripped away'%html_tags
            
    except Exception as e:
        print 'Exception: %s'%e.args
    print ''
    
def check_right_rail_widget(test_env, article_url):
    ''' Use to check the Right Rail Widget
        Should have 5 unique articles - no duplicates
    '''
    seen = set()
    uniq = []
    article_list = []
    rr_api = '/core/article/content?partnerId='
    rr_api_call = get_reb_env(test_env) + rr_api + hl_partner_id + '&url=' + article_url
    
    try:
        rr_json = make_request(rr_api_call, True, True)
        if rr_json["data"]["sponsored_landing_data"]["rightRailWidget"] != None:
            rr_article_list = rr_json["data"]["sponsored_landing_data"]["rightRailWidget"]
            # Get each article in the Right Rail Widget and store it in a list.
            for article in rr_article_list:
                article_list.append(article[0]["url"])
        # Check the article_list for any duplicates.
        for a in article_list:
            if a not in seen:
                uniq.append(a)
                seen.add(a)   
        print '[Info]: The Right Rail Article List: %s'%rr_article_list     
        # Check the seen set
        if len(seen) == 0:
            print '["Passed"]: Test Check Right Rail for 5 Unique Articles passsed'
        else:
            print '[Info]: Found duplicate article: %s'%seen
            print '[Failed]: Test Check Right Rail for 5 Unique Articles failed!!!'
    except Exception as e:
        print 'Exception: %s'%e.args
    print ''
     
def get_ad_data(test_env, article_url):
    # Use to get the ads data from the article url passed in.
    ad_data_api = '/core/advertising/ad?partnerId='
    ad_data_api_call = get_reb_env(test_env) + ad_data_api + hl_partner_id + '&url=' + article_url
    error_msg = 'Unable to get Ad Targeting data: url: '
    status_msg = error_msg + article_url
    
    try:
        ad_json = make_request(ad_data_api_call, True, True)
    
    except Exception as e:
        print 'Exception: %s'%e.args
    if "ads" in ad_json:
        print 'Get Ads Data Test Passed'
    elif ad_json["meta"]["code"] == 400 and ad_json["meta"]["status"] == status_msg:
        print 'Get Ads Data Test - Full Url Test Passed'
    else:
        print 'Get Ads Data Test Failed!!!'
    return ad_json

def get_top_stories(test_env, url):
    #Use to get the Top Stores from Widget located on the Right Rail.
    top_stories_api = '/core/json/content?url=' + url + '&partnerId='
    tc_api_call = get_reb_env(test_env) + top_stories_api + hl_partner_id
    top_stories_list = []
    status_msg = 'No content found for url: '
    
    tc_json = make_request(tc_api_call, True, True)
    if (tc_json["meta"]["code"] == 200 and tc_json["meta"]["status"] == 'Success'):
        top_stories_list = tc_json["data"]["topStoriesWidget"]["linkurls"]
        stories_count = tc_json["data"]["topStoriesWidget"]["topStoryCount"]
        categories = tc_json["data"]["topStoriesWidget"]["categories"]
        print 'Get Top Stories Test Passed'
        print 'The Top Stories are: %s'%top_stories_list
        print 'Number of Top Stories: %i'%stories_count
        print 'Categories are: %s'%categories
    elif tc_json["meta"]["code"] == 400 and tc_json["meta"]["status"] == status_msg + url:
        print 'Get Top Stories Test - Full Url Passed'
    else:
        print 'Get Top Stories Test Failed!!!'
    
def get_feed_category(test_env, category_list):
    # Returns the Feed for the passed in category list.
    # maxResults control how many articles are returned.
    artcle_headings_regex = '<a href=".*\/health-news\/.*">(.*)<\/a>'
    items = []
    article_title_list = []
    fc_api = '/core/news/feed.json?partnerId='
    headings_list = []
    heading_regex = '<a href=".*\/health-news\/.*">(.*)<\/a>' 
    debug = True
    
    for category in category_list:
        # generate a random number between 0 and 20.
        random_num = randint(0, 5)
        
        fc_api_call = get_reb_env(test_env) + fc_api + hl_partner_id + '&rows=' + str(random_num) + '&category=' + category
        # Make the api call.
        headings_json = make_request(fc_api_call, True, True)
        # Get the Web Source of the article.
        article_url = get_test_server(test_env) + '/health-news?condition=' + category + '#grid'
        # Get the text between the href links.
        headings_list = get_link_text(article_url)
        # Set the items array to a list.
        items_list = headings_json['items']
        print 'Headings List for article %s: %s'%(article_url, headings_list)
        # get the article headings from the response json.
        article_title_list = [d['title'] for d in items_list if 'title' in d]  
        # check to see if the proper number of articles were fetched.
        if len(items_list) == random_num:
            print '[Passed]: Rows fetched the proper number of articles: %s'%str(random_num)
        else:
            print '[Failed]: Rows: %s did not fetched the proper number of articles, it fetched: %i articles instead!!!'%(str(random_num), len(items_list)) 
        if debug:
            print 'Article Titles List: %s'%article_title_list
        # check for the titles in the headings_list.
        for title in article_title_list:
            if title in headings_list:
                print 'Found title: %s in the Headings List'%title.encode("iso-8859-15", "replace")
                
            else:
                print 'Did not find title: %s in the Headings List'%title
        
        
def get_byline(test_env, article_url):
    # Returns the Byline values from the passed in article url.
    byline_api = '/core/article/content?partnerId='
    byline_api_call = get_reb_env(test_env) + byline_api + hl_partner_id + '&url=' + article_url
    
    article_json = make_request(byline_api_call, True, True)
    reviewer = article_json["byline"]["reviewerName"]
    reviewer_pic = article_json["byline"]["reviewerImage"]
    if (reviewer != None and reviewer_pic != None):
        print 'Get Byline Test Passed'
        print 'Reviewer: %s'%reviewer
        print "Reviewer's Picture: %s"%reviewer_pic
    else:
        print 'Get Byline Test Failed!!!'
        
def get_nutrition_feed_sr(test_env, start, rows):
    # Returns the Nutrition Feeds via api.
    nutrition_api = '/core/nutrition/feed.json?partnerId='
    nutrition_articles_list = []
    nutrition_api_call = get_reb_env(test_env) + nutrition_api + hl_partner_id + '&start=' + str(start) + '&rows=' + str(rows)
    AN_Title = 'Authority Nutrition Landing Page Feed'
    error_msg = 'Invalid input param(s) had been detected: start= -1 rows= 5'
    
    nutrition_json = make_request(nutrition_api_call, True, True)
    if "meta" not in nutrition_json:
        nutrition_articles_list = nutrition_json["items"]
        if (nutrition_json["title"] == AN_Title and len(nutrition_articles_list) == rows):
            print 'Test Get Nutrition Feed Passed'
    elif nutrition_json["meta"]["code"] == 400 and nutrition_json["meta"]["status"] == error_msg:
            print 'Test Get Nutrition Feed Start = -1 and Rows = 5 Passed'
    else:
        print 'Test Get Nutrition Feed Failed!!!'
    
def set_newsletter_signup(test_env, email_payload):        
    # Submits the passed in payload for the Email Signup 
    email_api = '/core/newslettersignup?partnerId='
    error_msg = 'SailThru Error returned error: 11.0\nSailThru Error returned errormsg: Invalid email: cchoyathealthline.com\n'
    email_api_call = get_reb_env(test_env) + email_api + hl_partner_id 
     
    nl_json = make_post(email_api_call, email_payload, True, True)
    if nl_json["meta"]["code"] == 200 and nl_json["meta"]["status"] == 'Success':
        print 'Test Set Email Signup Passed'
    elif nl_json["meta"]["code"] == 400 and nl_json["meta"]["status"] == error_msg:
        print 'Test Set Email Signup - Bad Email Address Passed'
    else:
        print 'Test Set Email Signup Failed!!!'     
    
def set_emaillink(test_env, emaillink_payload):
    # Submits the passed in payload for the Email Link.
    emaillink_api = '/core/emailpagelink?partnerId='
    email_error_msg = 'SailThru Error returned error: 11.0\nSailThru Error returned errormsg: Invalid email: cchoyathealthline.com\n'
    link_error_msg = 'Only healthline.com links supported'
    emaillink_api = '/core/emailpagelink?partnerId='
    emaillink_api_call = get_reb_env(test_env) + emaillink_api + hl_partner_id
    el_json = make_post(emaillink_api_call, emaillink_payload, True, True)
    if el_json["meta"]["code"] == 200 and el_json["meta"]["status"] == 'Success':
        print 'Test Set Email Link Passed'
    elif el_json["meta"]["code"] == 400 and el_json["meta"]["status"] == email_error_msg:
        print 'Test Set Email Link - Bad Email Passed'
    elif el_json["meta"]["code"] == 400 and el_json["meta"]["status"] == link_error_msg:
        print '[Passed]: Test Set Email Link - Non HL Link Passed'
    else:
        print '[Failed]: Test Set Email Link Test Failed!!!'
        
def get_ga_data(test_env, article_url):
    # Use to get the GA data from an article.
    ga_article_url = get_test_server(test_env) + article_url
    if test_env in ('api-new', 'api01', 'api02', 'prod_gateway'): 
        #if 'vaginal' in article_url:
            ga_regex = '"k1":"([^,]+)","k2":"([^,]+)"'
        #else:
            #ga_regex = 'k\d:\s.*' 
    else:
        ga_regex = 'k\d:\s.*'
    ga_api = '/core/ga/content?url='
    ga_api_call = get_reb_env(test_env) + ga_api + article_url + '&partnerId=' + hl_partner_id
    error_msg = 'Unable to get Ad Targeting data for url: '
    
    # Make the api call..
    ga_json = make_request(ga_api_call, True, True)
    if ga_json["meta"]["code"] == 200 and ga_json["meta"]["status"] == 'Success':
        author = ga_json["ga"]["author"]
        k1 = ga_json["ga"]["k1"]
        k2 = ga_json["ga"]["k2"]
        hrlContentId = ga_json["ga"]["hrlContentId"]
        
    
        # Get the article page source and parse through it for the k values.
        k_list = parse_text(ga_article_url, ga_regex)
    
        print 'k items in the View Source of %s: %s'% (article_url, k_list) 
    
        if (k1 in k_list[0][0] and k2 in k_list[0][1]):
            print 'Author: %s'%author
            print 'k1: %s'%k1
            print 'k2: %s'%k2
            print 'hrlContentId: %s'%hrlContentId
            print 'Test Get GA Data Passed'
    elif ga_json["meta"]["code"] == 400 and ga_json["meta"]["status"] == error_msg + article_url:
        print '[Passed]: Test Get GA Data - Full Url Passed'
    else:
        print '[Failed]: Test Get GA Data Failed'

def get_sessions_count(test_env, article_url, start_date):
    # This api is only for testing purposes, hit the api servers directly, not through the gateway.
    protocol = 'http://'
    domain = '.healthline.com:'
    port = '8080'
    if test_env == 'qa' or test_env == 'qa_gateway':
        server = 'apiqa.eng'
    elif test_env == 'stage' or test_env == 'stage_gateway':
        server = 'api-stage'
    elif test_env == 'api01':
        server = 'api01.eng'
    elif test_env == 'api02':
        server = 'api02.eng'
    else:
        server = 'api-new'
    sc_api = '/core/ga/session?uri='
    
    sc_api_call = protocol + server + domain + port + sc_api + article_url + '&startDate=' + start_date + '&partnerId=' + hl_partner_id
    sc_json = make_request(sc_api_call, True, True)
    if sc_json["meta"]["status"] == 'Success':
        print '[Passed]: Test Get Sessions Count Passed'
    else:
        print '[Failed]: Test Get Sessions Count Failed!!!'
        
def test_feedback(test_env, feedback_payload):
    # Use with the new api for feedback.
    feedback_id = 0
    create_feedback_api = '/core/user/feedback/create?partnerId='
    list_feedback_api = '/core/user/feedback/list?partnerId='
    create_feedback_api_call = get_reb_env(test_env) + create_feedback_api + hl_partner_id
    list_feedback_api_call = get_reb_env(test_env) + list_feedback_api + hl_partner_id
    update_feedback_api = '/core/user/feedback/update?partnerId='
    update_feedback_api_call = get_reb_env(test_env) + update_feedback_api + hl_partner_id
    debug = False
    
    # Create the Feedback.
    make_post(create_feedback_api_call, feedback_payload, False, False)
    # Verifying the Feedback
    list_json = make_request(list_feedback_api_call, True, True)
    feedback_id = list_json["data"][0]["id"]
    if debug:
        print 'Feedback ID: %s'%feedback_id
    
    # Create payload to make an Update
    update_payload = {"partnerId":"31a86f67-b1b6-4fd7-8b98-8caeee8d3078",
                      "feedback":{feedback_id:{"message":"Effective and precise narrative. v"},
                                  "80364":{"notes":"v"},
                                  "300027":{"feedback":"test v " + test_env,"message":"test test v"}},
                                  "type":"editcell"}
    # Update the Feedback.
    update_json = make_post(update_feedback_api_call, update_payload, False, False)
    # Run list again to verify the update.
    updated_json = make_request(list_feedback_api_call, True, True)
    
def get_sharebar_attributes(test_env, article_url):
    # get the share bar states.
    article_regex = '^\/health\/.*'
    
    if re.match(article_regex, article_url):
        sb_api = '/core/article/content?partnerId='
    else:
        sb_api = '/core/news/content?partnerId='
    sb_api_call = get_reb_env(test_env) + sb_api + hl_partner_id + '&url=' + article_url
    sb_json = make_request(sb_api_call, True, True)
    if sb_json["sharebar"] != None:
        print 'Share Bar Attributes: %s'%sb_json["sharebar"]
        print 'Test Get Share Bar Attributes passed'
    else:
        print '[Failed]: Test Get Share Bar Attributes failed!!!'

def get_sponsored_page(test_env, article_url):
    # Use to get the [/sponored/content].
    sp_api = '/core/sponsored/content?partnerId='
    sp_api_call = get_reb_env(test_env) + sp_api + hl_partner_id + '&url=' + article_url
    sp_json = make_request(sp_api_call, True, True)
    if sp_json["ads"] != None or sp_json["metadata"] != None or sp_json["data"]["sponsored_widgets"] != None:
        print '[Info]: ads data: %s'%sp_json["ads"]
        print '[Info]: meta data: %s'%sp_json["metadata"]
        print '[Info]: sponsored widgets: %s'%sp_json["data"]["sponsored_widgets"]
        print '[Passed]: Get Sponsored Page passed'
    else:
        print '[Failed]: Test Get Sponsored Page failed!!!'
        
def check_sponsored_widget(test_env, article_url, type):
    ''' Used to check the Sponsored Widgets
        on a Sponsored Landing Page. Pass in the 
        widget type.
    '''
    widget_list = []
    sw_api = '/core/article/content?partnerId='
    sw_api_call = get_reb_env(test_env) + sw_api + hl_partner_id + '&url=' + article_url
    try:
        sw_json = make_request(sw_api_call, True, True)
        
        if type == 'Bottom': 
            if sw_json["data"]["sponsored_landing_data"]["bottomRecommendedWidget"] != None:
                widget_list = sw_json["data"]["sponsored_landing_data"]["bottomRecommendedWidget"]
                for item in widget_list:
                    print '[Info]: Article Title: %s'%item["txtUrls"][0]["title"]
                    print '[Info]: Article url: %s'%item["txtUrls"][0]["url"]
                    print '[Info]: Summary: %s'%item["summary"]
                print '[Passed]: Test Check Bottom Recommended Widget passed'
            else:
                print '[Failed]: Test Check %s Recommended Widget failed!!!'%type 
        elif type == 'List':
            if sw_json["data"]["articleWidgets"][0]["articleList"] != None:
                widget_list = sw_json["data"]["articleWidgets"][0]["articleList"]
                for item in widget_list:
                    print '[Info]: Link url: %s'%item["link"]
                print '[Passed]: Test Check Article Listing Widget passed'
            else:
                print '[Failed]: Test Check Article %s Widget failed!!!'%type 
        elif type == 'Responsive':
            if sw_json["data"]["videoWidgets"][0] != None:
                print '[Info]: Video Widget Type: %s'%sw_json["data"]["videoWidgets"][0]["type"]
                print '[Info]: Title: %s'%sw_json["data"]["videoWidgets"][0]["htmlTitle"]
                print '[Info]: Video Type: %s'%sw_json["data"]["videoWidgets"][0]["videoType"]
                print '[Passed]: Test Check Video Widget passed'
            else:
                print '[Failed]: Test Check %s Video Widget failed!!!'%type 
        elif type == 'Action':
            if sw_json["data"]["articleWidgets"][1]!= None:
                print '[Info]: Take Action: %s'%sw_json["data"]["articleWidgets"][1]["takeActionWidget"]["tawCallToAction"]
                print '[Info]: Short Description: %s'%sw_json["data"]["articleWidgets"][1]["takeActionWidget"]["tawShortDescription"]
                print '[Info]: Whisper Text: %s'%sw_json["data"]["articleWidgets"][1]["takeActionWidget"]["tawWhisper"]
                print '[Info]: Take Action Link: %s'%sw_json["data"]["articleWidgets"][1]["takeActionWidget"]["tawCtaLink"]
                print '[Passed]: Test Take Action Widget passed'
            else:
                print '[Failed]: Test Check Take %s Widget failed!!!'%type 
    except Exception as e:
        print 'Exception: %s'%e.args
     
    
def check_sponsored_urls(test_env, article_url, url_list):
    # Use to get the Sponsored urls.
    san_api = '/core/news/content?partnerId='
    san_api_call = get_reb_env(test_env) + san_api + hl_partner_id + '&url=' + article_url
    tmp_list = []
    rec_url_list = []
    missing_urls_list = []
    debug = True
    
    # Get the recommended urls and put it in a list.
    san_json = make_request(san_api_call, True, True)
    if san_json["recommended"] != None:
        tmp_list = san_json["recommended"]
        for item in tmp_list:
             rec_url_list.append(item["txtUrls"][0]["url"])
        if debug:
            print 'rec_url_list: %s'%rec_url_list
    # Check the size of the recommended articles list.
    if len(rec_url_list) == 10:
        print '[Passed]: There are %i articles in the Recommended section'%len(rec_url_list)
    else:
        print '[Error]: There are %i articles in the Recommended section!!!' 
    '''# Check against the passed in url_list.
    for url in url_list:
        if url in rec_url_list:
            print 'Found url: %s in the recommended url list...'%url
        else:
            print '[Warning]: Did not find the url: %s in the recommended url list!!!'%url
            missing_urls_list.append(url)
    if len(missing_urls_list) < 6:
        print '[Passed]: Test Check Sponsored Contents: AN urls passed'
    else:
        print '[Failed]: Test Check Sponsored Contents: AN urls failed!!!'
        print '[Info]: The following urls: %s were not found in the recommended section!!!'%missing_urls_list '''
    
            
def check_symptom(test_env, symptom_list):
    ''' Use to check each symptoms in the list passed in
        The SxC api is used to verify if there is an article 
        in the body section of the responding json.
    '''
    sxc_api = '/core/sxs/condition?partnerId='
    sxc_api_call = get_reb_env(test_env) + sxc_api + hl_partner_id + '&symptoms='
    sxc_json = ''
    
    for symptom in symptom_list:
        sxc_json = make_request(sxc_api_call + symptom.lower(), True, True)
        if sxc_json["data"]["articleInfo"] != None:
            print '[Passed]: There are %i articles'%sxc_json["data"]["numResults"]
        else:
            print '[Failed]: There were no articles found!!!'
        print ''
        
def get_articles_sxc(test_env, symptom):
    ''' Use to get each of the articles listed in
        articleInfo field, the returning list contains
        the url for the articles.
    '''
    article_list = []
    sxc_api = '/core/sxs/condition?partnerId='
    sxc_api_call = get_reb_env(test_env) + sxc_api + hl_partner_id + '&symptoms=' + symptom
    file = open("MasterSymptomArticlesList.txt", "a")
    b_run = True
    while b_run:
        try:
            sxc_json = make_request(sxc_api_call, True, True)
            article_info_list = sxc_json["data"]["articleInfo"]
            file.write("------------ Symptom: %s ----------\n"%symptom)
            for article in article_info_list:
                #print'[Info]: hlrcontentid: %s'%article["hrlcontentid"]
                file.write("hrlcontentid: %s\n"%article["hrlcontentid"])
                #print'[Info]: itemtitle: %s'%article["itemtitle"]
                file.write("itemtitle: %s\n"%article["itemtitle"].encode("iso-8859-15", "replace"))
                file.write("Article: %s\n"%article["cmsurl"])
                article_list.append(article["cmsurl"])        
            file.write("Total Articles: %i \n"%len(article_list))
            print'There are %i articles for symptom: %s'%(len(article_list), symptom)
            print '[Passed]: The test passed for symptom: %s'%symptom
            b_run = False 
        except KeyError:
            print '[Info]: No articles were found for symptom: %s'%symptom
            print '[Failed]: The test failed for symptom: %s'%symptom
            break
        finally:
            file.close() 
         
      
def get_global_header_data(test_env):
    # Use to get the data from the Global Header.
    tabs_list = []
    gh_api = '/core/json/content?url=global_header&partnerId='
    gh_api_call = get_reb_env(test_env) + gh_api + hl_partner_id
    gh_json = make_request(gh_api_call, True, True)
    
    if gh_json["data"]["tabs"] != None:
        tabs_list = gh_json["data"]["tabs"]
        print 'Listing only the 3 tabs for now from the global header:'
        print '[Info]: Items in the tabs: %s'%tabs_list
    else:
        print '[Error]: No tabs were found in the global header'   
      
# MS Buddy Methods.        
def get_app_env_url(test_env, be_version, app_key):
    # Returns the constructed application api domain.
    if be_version in ['V6', 'V7']:
        apps_list = {"msbuddy":"/MSBuddy/" + be_version.lower()}
    else:
        apps_list = {"msbuddy":"/MSBuddy-" + be_version, "wobuddy":"/WOBuddy-" + be_version}
    protocol = "https://"
    reg_protocol = "http://"
    domain = ".healthline.com"
    stage_env_url = "msbuddy-stage"
    qa_env_url = "msbuddy-qa"
    seung_env_url = "10.60.2.22:8480"
    new_env_url = "msbuddy-new"
    sb_env_url = "msbuddy-sb"
    api_url = ''
    
    if test_env == 'stage':
        api_url = protocol + stage_env_url + domain  + apps_list[app_key]
    elif test_env == 'seung':
        api_url = reg_protocol + seung_env_url + apps_list[app_key]
    elif test_env == 'qa':
        api_url = protocol + qa_env_url + domain + apps_list[app_key]
    elif test_env == 'new':
        api_url = protocol + new_env_url + domain + apps_list[app_key]
    elif test_env == 'sb':
        api_url = protocol + sb_env_url + domain + apps_list[app_key]
    elif test_env == 'msbuddy01':
        api_url = 'http://' + 'msbuddy01.eng' + domain + apps_list[app_key]
    elif test_env == 'msbuddy02':
        api_url = 'http://' + 'msbuddy02.eng' + domain + apps_list[app_key]
    elif test_env == 'msbuddyc1':
        api_url = 'http://' + 'msbuddyc1.eng' + domain + apps_list[app_key]
    elif test_env == 'msbuddyc2':
        api_url = 'http://' + 'msbuddyc2.eng' + domain + apps_list[app_key]
    else:
        api_url = protocol + app_key + domain + apps_list[app_key]
    print "api_url is: %s"%api_url   
    return api_url

def get_test_env(test_env):
    # Get the test env i.e.: stage02, stage01 or www.
    # Use for regular requests i.e: web pages.
    testing_env = ''
    domain = ''
    test_url = ''
    
    if test_env == 'stage02':
        testing_env = 'sfc-stage02'
        domain = '.healthline.com'
    elif test_env == 'stage07':
        test_env = 'sfc-stage07'
        domain = '.healthline.com'
    elif test_env == 'stage01':
        testing_env = 'sfc-stage01'
        domain = '.healthline.com'
    elif test_env == 'prod':
        testing_env = 'www'
        domain = '.healthline.com'
    elif test_env == 'dion':
        testing_env = '10.1.0.108'
        domain = ''
    elif test_env == 'michael':
        testing_env = '10.1.0.113'
        domain = ''
    elif test_env == 'oleg':
        testing_env = '10.1.0.165'
        domain = ''
    elif test_env == 'olga':
        testing_env = '10.1.0.90'
        domain = ''
    # print 'The testing_env: %s'%testing_env
    test_url = protocol + testing_env + domain
    return test_url

def get_list_of_pills(file_name):
    # Get a list of pills from the .csv files and return them in a list.
    csv_file = file_name
    pills = []
    
    fh = open(csv_file)
    pills = fh.readlines()
    fh.close()
    return pills
    
def make_request(api_url, show_json_payload=False, rtn_json=False):
    # Make the request with the passed in api url
    # Return the payload from the json response.
       
    status_code = 0
    
    print 'Now making request to: %s...'% api_url
    re = requests.get(api_url)
    status_code = re.status_code
    
    if status_code == 200:
        print '[SUCCESS]: Returned status code was: %s'% re.status_code
    elif status_code == 400:
        print '[WARNING]: The api request: %s produce no results...' %api_url
    else:
        print '[ERROR]: Please retry the api url: %s manually' %api_url
    print ''
    if show_json_payload == True:
        print 'The Payload is: %s' %re.text
    if rtn_json == True:
        try:
            return re.json()
        except ValueError:
            print '[ERROR]: ValueError: No JSON object could be decoded'
    else:
        return re.text

def make_post(api_url, payload_data = {}, show_json_payload=False, rtn_json=False):
    # Use to post requests with the passed in api_url and 
    # Dictionary of the payload_data.
    # Access Key for security pass true to use, false to request normal, currently for the Buddy Apps.
    debug = True
    
    if debug:
        print 'api_url: %s and payload:%s'%(api_url, payload_data)
   
    re = requests.post(api_url, json=payload_data)
    status_code = re.status_code
    
    if status_code == 200:
        print '[SUCCESS]: Returned status code was: %s'% re.status_code
    elif status_code == 400:
        print '[WARNING]: The api request: %s produce no results...' %api_url
    else:
        print '[ERROR]: Please retry the api url: %s manually' %api_url
    print ''
    if show_json_payload == True:
        print 'The json payload is: %s' %re.text
    if rtn_json == True:
        try:
            return re.json()
        except ValueError:
            print '[ERROR]: ValueError: No JSON object could be decoded'
    else:
        return re.text
    
def app_make_request(api_url, http_header, show_json_payload, app, rtn_json, fail_counter):
    # Make the request with the passed in api url
    # Return the payload from the json response.
    # Access Key for security pass true to use, false to request normal, currently for the Buddy Apps.
    # For V3 we now need the app version 2.0.3 or greater.
    # Pass in the http headers via a dictionary.
    
    status_code = 0
    
    print 'Now making api request: %s with http header(s): %s' %(api_url, http_header)
    try:
        re = requests.get(api_url, headers=http_header)
        status_code = re.status_code
        if status_code == 200:
            print '[SUCCESS]: Returned status code was: %s'% re.status_code
    except:
        if status_code == 400:
            print '[WARNING]: The api request: %s produce no results...' %api_url
        elif status_code == 1000:
            print '[ERROR]: Fatal Exceptions such as bad solr query or missing solr database'
        elif status_code == 1010:
            print '[ERROR]: Duplicate Email'
        elif status_code == 1020:
            print '[ERROR]: Password not present'
        elif status_code == 1030:
            print '[ERROR]: Invalid Email and Password Combination'
        elif status_code == 1035:
            print '[ERROR]: Invalid Email'
        elif status_code == 1040:
            print '[ERROR]: Non Existent User'
        elif status_code == 1050:
            print '[ERROR]: Invalid Password or Blank Password'
        else:
            print '[ERROR]: Please retry the api url: %s manually' %api_url
    print ''
    if show_json_payload == True:
        try:
            print 'The Payload is: %s' %re.text
        except ValueError:
            print '[ERROR]: ValueError: No JSON object could be decoded'
            fail_counter += 1
            return (re.text, fail_counter)
    if rtn_json == True:
        try:
            return (re.json(), fail_counter)
        except ValueError:
            print '[ERROR]: ValueError: No JSON object could be decoded'
            fail_counter += 1
            return (re.text, fail_counter)
    else:
        return (re.text, fail_counter)
        

def app_make_post(api_url, http_header, payload_data, show_json_payload, app, rtn_json, fail_counter):

    # Use to post requests with the passed in api_url and 
    # Dictionary of the payload_data.
    # Access Key for security pass true to use, false to request normal, currently for the Buddy Apps.
    # Pass in the http headers via a dictionary.
    
    status_code = 0
        
    print 'Now Posting api request: %s with http header: %s and payload: %s' %(api_url, http_header, payload_data) 
    try:
        re = requests.post(api_url, headers=http_header, json=payload_data)
        status_code = re.status_code
        if status_code == 200 or status_code == 0:
            print '[SUCCESS]: Returned status code was: %s'% re.status_code
    except:     
        if status_code == 400:
            print '[WARNING]: The api request: %s produce no results...' %api_url
        elif status_code == 1000:
            print '[ERROR]: Fatal Exceptions such as bad solr query or missing solr database'
        elif status_code == 1010:
            print '[ERROR]: Duplicate Email'
        elif status_code == 1020:
            print '[ERROR]: Password not present'
        elif status_code == 1030:
            print '[ERROR]: Invalid Email and Password Combination'
        elif status_code == 1035:
            print '[ERROR]: Invalid Email'
        elif status_code == 1040:
            print '[ERROR]: Non Existent User'
        elif status_code == 1050:
            print '[ERROR]: Invalid Password or Blank Password'
        else:
            print '[ERROR]: Please retry the api url: %s manually' %api_url # Could be 404 or 500.
        fail_counter += 1
    print ''
    if show_json_payload == True:
        try:
            print 'The json payload is: %s' %re.text
        except ValueError:
            print '[ERROR]: ValueError: No JSON object could be decoded'
            return
    if rtn_json == True:
        try:
            return (re.json(), fail_counter)
        except ValueError:
            print '[ERROR]: ValueError: No JSON object could be decoded'
            fail_counter += 1
            return (re.text, fail_counter)
            
    else:
        return (re.text, fail_counter)    

def app__post(api_url, http_header, payload_data, show_json_payload, app, rtn_json):

    # Use to post requests with the passed in api_url and 
    # Dictionary of the payload_data.
    # Access Key for security pass true to use, false to request normal, currently for the Buddy Apps.
    # Pass in the http headers via a dictionary.
    
    status_code = 0
        
    print 'Now Posting api request: %s with http header: %s and payload: %s' %(api_url, http_header, payload_data) 
    try:
        re = requests.post(api_url, headers=http_header, json=payload_data)
        status_code = re.status_code
        if status_code == 200 or status_code == 0:
            print '[SUCCESS]: Returned status code was: %s'% re.status_code
    except:     
        if status_code == 400:
            print '[WARNING]: The api request: %s produce no results...' %api_url
        elif status_code == 1000:
            print '[ERROR]: Fatal Exceptions such as bad solr query or missing solr database'
        elif status_code == 1010:
            print '[ERROR]: Duplicate Email'
        elif status_code == 1020:
            print '[ERROR]: Password not present'
        elif status_code == 1030:
            print '[ERROR]: Invalid Email and Password Combination'
        elif status_code == 1035:
            print '[ERROR]: Invalid Email'
        elif status_code == 1040:
            print '[ERROR]: Non Existent User'
        elif status_code == 1050:
            print '[ERROR]: Invalid Password or Blank Password'
        else:
            print '[ERROR]: Please retry the api url: %s manually' %api_url # Could be 404 or 500.
       
    print ''
    if show_json_payload == True:
        try:
            print 'The json payload is: %s' %re.text
        except ValueError:
            print '[ERROR]: ValueError: No JSON object could be decoded'
            return
    if rtn_json == True:
        try:
            return re.json()
        except ValueError:
            print '[ERROR]: ValueError: No JSON object could be decoded'
            return re.text
    else:
        return re.text 

def parse_text(url, reg_exp):
    # Use to grab only the text portion of a returned html source.
    # Parse through the text for the regular expression passed in.
    # Return a list of values.
    
    list_of_values = []
    debug = False
    
    req = requests.get(url)
    # Store the html source.
    page_html = req.content
    # Set BeautifulSoup to parse.
    soup = BeautifulSoup(page_html, 'html.parser')
    # Grab only the text since the variables are in a javascript function.
    text = soup.getText()
    # Use regular expression to get the passed in reg-ex pattern.
    values = re.findall(reg_exp, text)
    list_of_values.append(values)
    if debug:
        print 'Text: %s'%text
        print ''
        print 'The list of values: %s'%values
        print ''
    req.connection.close()
    return list_of_values

def get_link_text(url):
    # Use to get the href text.
    list_of_links = []
    debug = True
    
    req = requests.get(url)
    page_html = req.content
    soup = BeautifulSoup(page_html, 'html.parser')

    for anchor in soup.find_all('a'):
        list_of_links.append(anchor.text)
    if debug:
        print 'List of links: %s'%list_of_links
    return list_of_links
    
def parse_response(response_json, data_type, output):
    # Parse through the json payload and fetch the data with
    # passed in key.
    # Default is 'dict' for Dictionary output.
    # Pass in 'text' for Text output.
    
    if output == 'dict':
        parsed_json = json.loads(response_json)
        print 'The parsed data is: %s' %parsed_json[data_type]  
        print '' 
        return parsed_json[data_type]
    else:
        parsed_json = json.dumps(response_json)
        return parsed_json
    

def parse_list_dict(dict_list, dict_key):
    # Parse through a list of dictionaries from the parsed json.
    # Supply list and key.
    values_list = []
    for dl in dict_list:
        for key in dl:
            if key == dict_key:
                values_list.append(dl[key])
    return values_list
    
def get_colors(test_env):
    colors = []
    colors_url = test_env + api_requests_dict['colors']
    
    colors = parse_response(make_request(colors_url), 'data', 'dict')
    return colors

def get_shapes(test_env):
    shapes = []
    shapes_url = test_env + api_requests_dict['shapes']
    
    shapes = parse_response(make_request(shapes_url), 'data', 'dict')
    return shapes
    
def get_pills_color_shape(test_env, colors, shapes):
    # Use for a list of pill's colors and shape combinations.
    color_shape_api = '/api/service/2.0/pill/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078'
    pill_url = ''
    temp_list = []
    pills = []
    
    for color in colors:
        for shape in shapes:
            print 'Making api request for pills with color: %s and shape: %s' %(color, shape)
            pill_url = test_env + color_shape_api + '&colors=%s&shape=%s' %(color, shape)
            temp_list = parse_response(make_request(pill_url), 'data', 'dict')
            if len(temp_list) > 0: 
                print 'Adding populated pills to the pills list for color: %s and shape: %s' %(color, shape)
                pills.append(color + ' & ' + shape + ': ')
                pills.append(temp_list)
                print ''
    return pills

def get_pill_color_shape(test_env, color, shape):
    # Use for a single pill's color & shape combination.
    color_shape_api = '/api/service/2.0/pill/list?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078'
    pill_url = ''
    data = {}

    print 'Making api request for pills with color: %s and shape: %s' %(color, shape)
    pill_url = test_env + color_shape_api + '&colors=%s&shape=%s' %(color, shape)
    data = parse_response(make_request(pill_url, True), 'meta', 'dict')
    number_of_results = data['numResults']
    print 'Number of results: %i' %number_of_results
    if number_of_results > 0:
        print '[PASSED]: Number of pills found: %i' %number_of_results
    else:
        print '[FAILED]: No pills were found for the color search: %s' %color

def get_pills(test_env, item_list, pill_query, data_type):
    pill_api = test_env + api_requests_dict[pill_query]
    pill_url = ''
    temp_list = []
    results = []
    data = {}
    item_list = [item.strip() for item in item_list]
    for item in item_list:
        print 'Making api request for pill with Name/Marking: %s' %item
        pill_url = pill_api + item.strip()
        json_response = make_request(pill_url)
        
        # Get the number of results from the json.
        data = parse_response(json_response, data_type, 'dict')
        number_of_results = data['numResults']
        print 'Number of results: %i' %number_of_results
        # Check to see if the pill produced results.
        if number_of_results > 0:
            print '[PASSED]: Number of Pills found: %i' % number_of_results
        else:
            print '[FAILED]: No Pills were found, please investigate!!!'
            print 'Adding pill name/marking: %s to the pill_with_no_results list...' %item
            pill_with_no_results.append(item)
        temp_list = parse_response(json_response, data_type, 'dict')
        if len(temp_list) > 0:
            print 'Adding populated data to the results list for Drug Name/Marking: %s' %item
            print ''
            results.append('Drug Item: ' + item)
            results.append(temp_list)
            print ''
    return results

def get_num_results(json_response, data_type, output):
    # Get the number of results from the json.
    data = parse_response(json_response, data_type, output)
    number_of_results = data["numResults"]
    print 'Number of results: %i' %number_of_results
    return number_of_results

def fs_get_num_results(response):
    num_results = 0
    num_results = response["map"]["serviceData"]["meta"]["numResults"]
    return num_results

def get_pill(test_env, pill_attribute, pill_query, data_type):
    pill_api = test_env + api_requests_dict[pill_query]
    pill_url = ''
    temp_list = []
    result = ''
    data = {}
    
    print 'Making api request for pill with Name/Marking: %s' %pill_attribute
    pill_url = pill_api + pill_attribute
    json_response = make_request(pill_url)
    print 'json_response is: %s'%json_response    
    number_of_results = get_num_results(json_response, "meta", 'dict')
    # Check to see if the pill produced results.
    if number_of_results > 0:
        print '[PASSED]: Number of Pills found: %i' % number_of_results
        result = 'Passed'
    else:
        print '[FAILED]: No Pills were found, please investigate!!!'
        result = 'Failed'
    print ''
    return result
    

def verify_pills_list(pills_list_api, expected_count):
    print 'The pills list contains %i items'%len(pills_list_api)
    if len(pills_list_api) == expected_count:
        print '[PASSED]: The Pills list: %i matches the expected count: %i...'%(len(pills_list_api), expected_count)
    else:
        print '[WARNING]: The Pills list: %i did not match the expected count: %i, investigate!!!'%(len(pills_list_api), expected_count)
    print ''
    

def output_pills_list(pills_list):
    for pills in pills_list:
        print pills
        print ''
        
def evaluate_result(result, failure_counter, test_counter):
    if result == 'Failed':
        failure_counter += 1
    test_counter += 1
    return (failure_counter, test_counter)
 
def get_time():
    return datetime.datetime.now()

def ordered(obj):
    if isinstance(obj, dict):
        return sorted((k, ordered(v)) for k, v in obj.items())
    if isinstance(obj, list):
        return sorted(ordered(x) for x in obj)
    else:
        return obj
    
def ms_get_key_values_from_json(json_file):
    # Use this for MS Buddy
    # k_v_dict = json.dumps(json_file)
    list_of_keys = []
    debug = False
    k_v_dict = json_file["data"]["user"]
    od = collections.OrderedDict(sorted(k_v_dict.items()))
    if debug:
        print 'k_v_dict: %s'%k_v_dict
    return od 

def get_key_values_from_pub_json(json_file):
    ''' Use to get the key and values from a json return a dict.
        Need to know the keys for the jsons for example, health article:
        {k:["ads", "metadata", "recommend", "ga", "byline", "data", "sharebar", "meta"]}
     '''
    k_v_dict = {}
    #k_v_dict = json.dumps(json_file)
    # list_of_keys = []
    #tmp_dict = {}
   
    debug = True
    
    if debug:
        print '[Info]: Now working on json file: %s'%json_file
    json1_file = open(json_file)
    json1_str = json1_file.read()
    k_v_dict = json.loads(json1_str)
    #od = collections.OrderedDict(sorted(k_v_dict.items()))
    if debug:
        print 'k_v_dict: %s'%k_v_dict
    #return od
    return k_v_dict    
  
def get_key_values_from_json(json_file, key):
    ''' Use to get the key and values from a json return a dict.
        Need to know the keys for the jsons for example, health article:
        {k:["ads", "metadata", "recommend", "ga", "byline", "data", "sharebar", "meta"]}
     '''
    # k_v_dict = json.dumps(json_file)
    list_of_keys = []
    tmp_dict = {}
    k_v_dict = {}
    debug = False
    
    if debug:
        print '[Info]: Now working on json file: %s with key: %s'%(key, json_file)
    # In case the key contains a list.
    if key in ('recommended', 'items'):
        for item in json_file[key]:
            k_v_dict.update(item)
    else:
        k_v_dict = json_file[key]
    #od = collections.OrderedDict(sorted(k_v_dict.items()))
    if debug:
        print 'k_v_dict: %s'%k_v_dict
    #return od
    return k_v_dict    
        
def compare_jsons(json_1, json_2, key_list):
    ''' Use to compare two passed in jsons.
        json_1 is the one from the new BE.
        json-2 is the one from the standard BE.
    '''
    debug = False
    for key in key_list:
        if debug:
            print '[Info]: Now checking key: %s'%key
        key_value_dict_1 = get_key_values_from_json(json_1, key)
        key_value_dict_2 = get_key_values_from_json(json_2, key)
    
        if debug:
            print 'key_value_dict_1: %s'%key_value_dict_1
            print 'key_value_dict_2: %s'%key_value_dict_2
    
            print ''
        try:
            for k, v in key_value_dict_1.items():
                print 'Now evaluating key: %s and value: %s...'%(k, v)
                if k in key_value_dict_2:
                    if v == "" or v == None and key_value_dict_2[k] == None: 
                        print '[PASS]: The key: %s and value: %s was found in json file: json_2'%(k, v)
                    elif v == key_value_dict_2[k]:
                        print '[PASS]: The key: %s and value: %s matches in json file: json_2'%(k, v)
                    elif v != key_value_dict_2[k]:
                        print '[WARNING]: The key: %s exists in json_2 but the values are different from json_1'%k
                        print '[INFO]: The value in json_1: %s and the value in json_2: %s'%(v, key_value_dict_2[k])
                else:
                    print '[ERROR]: The key: %s and value: %s was not found in json file: json_2!!!'%(k, v)
        except Exception as e: print(e)
                    
        print ''
        
def compare_pub_jsons(json_1, json_2):
    ''' Use to compare two passed in jsons from the publishers.
        json_1 is the one from the new BE.
        json-2 is the one from the standard BE.
    '''
    debug = True
    if debug:
        key_value_dict_1 = get_key_values_from_pub_json(json_1)
        key_value_dict_2 = get_key_values_from_pub_json(json_2)
    
        if debug:
            print 'key_value_dict_1: %s'%key_value_dict_1
            print 'key_value_dict_2: %s'%key_value_dict_2
    
            print ''
        try:
            for k, v in key_value_dict_1.items():
                print 'Now evaluating key: %s and value: %s...'%(k, v)
                if k in key_value_dict_2:
                    if v == "" or v == None and key_value_dict_2[k] == None: 
                        print '[PASS]: The key: %s and value: %s was found in json file: json_2'%(k, v)
                    elif v == key_value_dict_2[k]:
                        print '[PASS]: The key: %s and value: %s matches in json file: json_2'%(k, v)
                    elif v != key_value_dict_2[k]:
                        print '[WARNING]: The key: %s exists in json_2 but the values are different from json_1'%k
                        print '[INFO]: The value in json_1: %s and the value in json_2: %s'%(v, key_value_dict_2[k])
                else:
                    print '[ERROR]: The key: %s and value: %s was not found in json file: json_2!!!'%(k, v)
        except Exception as e: print(e)
                    
        print '' 
           
def get_db_results(test_env, query):
    ''' Use to query MS Buddy's MySQL Database
    '''
    host = ''
    user = ''
    pw = ''
    db = ''
    
    if test_env == 'qa':
        host = 'apps-db-qa.eng.healthline.com'
        user = 'msbuddyqa'
        pw = 'msbuddyqa'
        db = 'msbuddyqa'
    elif test_env == 'stage':
        host = 'apps-db-stage.eng.healthline.com'
        user = 'msbuddystage'
        pw = 'msbuddystage'
        db = 'msbuddystage'
    elif test_env == 'prod':
        host = 'apps-db-prod.eng.healthline.com'
        user = 'msbuddyprod'
        pw = 'msbuddyprod'
        db = 'msbuddyprod'
        
    # Connect to the database
    connection = pymysql.connect(host = host,
                             user = user,
                             password = pw,
                             db = db,
                             charset = 'utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)

    try:

        with connection.cursor() as cursor:
            # Read all record
            sql = query
            cursor.execute(sql)
            results = cursor.fetchall()
            for result in results:
                print(result)
    finally:
        connection.close()
    return results