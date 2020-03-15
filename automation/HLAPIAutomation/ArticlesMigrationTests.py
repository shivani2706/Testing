"""
Author: chaz
Date: 12.14.17 
Script: ArticlesMigrationTests.py
Description: This script will get the articles urls
from the appropriate sitemap for /health/*, /health-news/*, and /nutrition/*
make api calls to both api-qa and api-stage. A comparison of the responding
json are then compared k/v to k/v for descreprencies, which will be outputted 
in the console or redirected to a text file (preferred method).
"""
import apiUtils
import datetime
import sys
import json

# Global variables
health_article_list = []
hl_key_list = ["ads", "metadata", "recommended", "ga", "byline", "data", "sharebar", "meta"]
feed_key_list = ["home_page_url", "_hl_results", "_adData", "title", "version", "items"]
health_and_news_sitemap = '/hlcms.xml'
nutrition_sitemap = '/hlan.xml'
test_env = 'stage'
test1_env = 'qa'
test2_env = 'stage'

if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Articles Migration Tests started at : %s on %s ****************************' %(str(start_time), test_env.upper())
    print ''
    # Get the articles urls.
    print '[Info]: Now getting the urls for Health Articles from the sitemap'
    health_article_list = apiUtils.get_article_links(test_env, health_and_news_sitemap, 'Health')
    print ''
    print '[Info]: Now getting the urls for the Health News Articles from the sitemap'
    health_news_list = apiUtils.get_article_links(test_env, health_and_news_sitemap, 'Health News')
    print ''
    print '[Info]: Now getting the urls for the Nutrition Articles from the sitemap'
    nutrition_article_list = apiUtils.get_article_links(test_env, nutrition_sitemap, 'Nutrition')
    print ''
    # Make the api calls to both api-qa and api-stage (let's make Stage the standard, it's pointing to NFS).
    
    print '[Info]: Now checking each of the Health Articles, comparing api-qa to api-stage'
    for h_article_url in health_article_list:
        print '[Info]: Now making the api calls to api-qa and api-stage for url: %s'%h_article_url
        h_json_qa = apiUtils.get_article_contents(test1_env, h_article_url)
        h_json_stage = apiUtils.get_article_contents(test2_env, h_article_url)
        print '[Info]: Now comparing responding jsons for url: %s'%h_article_url
        print '*****************************************************************************************************'
        apiUtils.compare_jsons(h_json_qa, h_json_stage, hl_key_list)
    print ''
    print '[Info]: Now checking each of the Health News Articles, comparing api-qa to api-stage'
    for hn_article_url in health_news_list:
        print 'Now making the api calls to api-qa and api-stage for url: %s'%hn_article_url
        hn_json_qa = apiUtils.get_news_content(test1_env, hn_article_url)
        hn_json_stage = apiUtils.get_news_content(test2_env, hn_article_url)
        print '[Info]: Now comparing responding jsons for url: %s'%hn_article_url
        print '****************************************************************'
        apiUtils.compare_jsons(hn_json_qa, hn_json_stage, hl_key_list)
    print ''
    print '[Info]: Now checking each of the Nutrition Articles, comparing api-qa to api-stage'
    for n_article_url in nutrition_article_list:
        print '[Info]: Now making api calls to api-qa and api-stage for url: %s'%n_article_url
        n_json_qa = apiUtils.get_article_contents(test1_env, n_article_url)
        n_json_stage = apiUtils.get_article_contents(test2_env, n_article_url)
        print '[Info]: Now comparing responding jsons for url: %s'%n_article_url
        print '***************************************************************'
        apiUtils.compare_jsons(n_json_qa, n_json_stage, hl_key_list)
        print ''
    
    print '[Info]: Now checking the Health Feed, comparing api-qa to api-stage'
    hf_json_qa = apiUtils.get_health_feed(test1_env)
    hf_json_stage = apiUtils.get_health_feed(test2_env)
    print ''
    print '[Info]: Now comparing the responding jsons for the Health Feed'
    print '***************************************************************'
    apiUtils.compare_jsons(hf_json_qa, hf_json_stage, feed_key_list)
    print ''
    print '[Info]: Now checking the Nutrition Feed, comparing api-qa to api-stage'  
    anf_json_qa = apiUtils.get_nutrition_feed(test1_env)
    anf_json_stage = apiUtils.get_nutrition_feed(test2_env)
    print ''
    print '[Info]: Now comparing the responding jsons for Nutrition Feed'
    print '***************************************************************'
    apiUtils.compare_jsons(anf_json_qa, anf_json_stage, feed_key_list)
    print ''
    print '[Info]: Now checking the News Feed, comparing api-qa to api-stage'
    nf_json_qa = apiUtils.get_data_news_feed(test1_env)
    nf_json_stage = apiUtils.get_data_news_feed(test2_env)
    print ''
    print '[Info]: Now comparing the News Feed, comparing api-qa to api-stage'
    print '***************************************************************'
    apiUtils.compare_jsons(nf_json_qa, nf_json_stage, feed_key_list)
    print ''
    end_time = datetime.datetime.now()
    print '**************************** Articles Migration Tests finished at : %s on %s ****************************' %(str(end_time), test_env.upper())
        
        
    
