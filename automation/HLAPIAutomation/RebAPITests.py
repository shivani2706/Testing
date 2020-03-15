"""
Author: chaz
Date: 08.20.17 
Script: RebAPITests.py
"""

import apiUtils
import datetime
import sys
import json
from AppsAPITests import test_env

# Passed in test environment i.e.: qa, stage or prod.
#test_env = sys.argv[1].lower()
test_env = 'qa'
article_url = '/health/psoriasis/omega-3-fatty-acids-treatment'
full_article_url = apiUtils.get_test_server(test_env) + article_url
recommended_article_list = ['/health/food-nutrition/diet-avocados#1', '/health/digestive-health/types-of-poop']
article_list = []
extensive_tests = False
debug = False
sitemap_url = '/hlcms.xml'
global_url = 'global_header'
byline_article_url = '/health/cancer/ovarian-cancer-early-signs'
fb_twitter_og_article_url = '/health/crohns-disease/advancing-crohns/crohns-advice-from-patients'
msite_article_url = '/health/high-cholesterol/foods-to-increase-hdl'
if test_env in ('qa', 'stage'):
    sponsored_article_url = '/program/chaz/test01'
else:
    sponsored_article_url = '/program/135317'
if test_env in ('qa', 'stage'):
    embedded_sponsored_article_url = '/health/chaz-test-article'
else:
    embedded_sponsored_article_url = '/health/chaz-test-article'
if test_env in ('qa', 'stage'):
    sponsored_content_url = '/health-news/discovery-of-superhero-bacteria-could-help-in-infection-survival-102915'
else:
    sponsored_content_url = '/health-news/why-suicide-prevention-is-a-big-part-of-this-years-movember-campaign'
recommended_an_items = ['/nutrition/carbohydrate-functions', '/nutrition/walking-for-weight-loss', '/nutrition/protein-deficiency-symptoms', 
                        '/nutrition/losing-weight-too-fast', '/nutrition/metabolism-and-age', '/nutrition/testosterone-and-fat-loss']
full_byline_article_url = apiUtils.get_test_server(test_env) + byline_article_url
ga_article_list = ['/health/womens-health/vaginal-lumps-bumps'] #Tab, Standard Articles for now '/health/gallbladder-problems-symptoms'.
full_ga_article_url = apiUtils.get_test_server(test_env) + '/health/womens-health/vaginal-lumps-bumps'
email_payload = {
                     "email":"cchoy@healthline.com",
                     "k1s":["k11", "k12"],
                     "customVars":{"k1":"v1","k2":"v2"},
                     "template":"Welcome Email"
                } 
bad_email_payload = {
                     "email":"cchoyathealthline.com",
                     "k1s":["k11", "k12"],
                     "customVars":{"k1":"v1","k2":"v2"},
                     "template":"Welcome Email"
                } 
email_pagelink_payload = {
                          "emailAddress":"cchoy@healthline.com",
                          "senderName":"BenderTheSender",
                          "senderEmail":"bender@gmail.com",
                          "pageTitle":"sample page title",
                          "pageLink":"https://www.healthline.com/health/multiple-sclerosis/alternative-treatment"
                          }
bad_email_pagelink_payload = {
                          "emailAddress":"cchoyathealthline.com",
                          "senderName":"BenderTheSender",
                          "senderEmail":"bender@gmail.com",
                          "pageTitle":"sample page title",
                          "pageLink":"https://www.healthline.com/health/multiple-sclerosis/alternative-treatment"
                          }

non_hl_email_pagelink_payload = {
                          "emailAddress":"cchoy@healthline.com",
                          "senderName":"BenderTheSender",
                          "senderEmail":"bender@gmail.com",
                          "pageTitle":"sample page title",
                          "pageLink":"https://www.drugs.com/health/multiple-sclerosis/alternative-treatment"
                          }

create_feedback_payload = {
                           "email": "cchoy@healthline.com",
                           "division": "hrl",
                           "url": "http://www.healthline.com/health/best-low-sugar-fruits",
                           "username": "chaz",
                           "signup": "0",
                           "feedback": "test feedback. I like this article - %s"%test_env,
                           "message": "test message this is a message",
                           "imuid": "1234567"
                           }
bad_email_create_feedback_payload = {
                           "email": "cchoyathealthline.com",
                           "division": "hrl",
                           "url": "http://www.healthline.com/health/best-low-sugar-fruits",
                           "username": "chaz",
                           "signup": "1",
                           "feedback": "test feedback. I like this article - %s"%test_env,
                           "message": "test message this is a message",
                           "imuid": "1234567"
                           }
non_hl_link_create_feedback_payload = {
                           "email": "cchoy@healthline.com",
                           "division": "hrl",
                           "url": "http://www.drugs.com/health/best-low-sugar-fruits",
                           "username": "chaz",
                           "signup": "0",
                           "feedback": "test feedback. I like this article - %s"%test_env,
                           "message": "test message this is a message",
                           "imuid": "1234567"
                           }
update_feeback_payload = {
                          "partnerId":"31a86f67-b1b6-4fd7-8b98-8caeee8d3078",
                          "feedback":{"80331":{"message":"Effective and precise narrative. v - %s"%test_env},
                                      "80364":{"notes":"v"},
                                      "300027":{"feedback":"test v","message":"test test v"}},
                          "type":"editcell"}

category_list = ["General Health", "Children's Health", "Mental Health", "Public Policy", 
                 "Technology", "Women's Health", "Food%20and Nutrition", "Cancer", "Senior Health",
                 "Heart Health", "Men's Health", "Fitness and Exercise", "Strange Science",
                 "Multiple Sclerosis", "Addiction", "Diabetes", "HIV", "Arthritis", "Sexual Health",
                 "Outdoor Health", "Health Insurance Exchanges", "Aging", "Cold and Flu", "Allergies",
                 "Skin Conditions", "COPD", "Crohn's Disease", "Lupus"]

health_news_url = '/health-news/your-options-if-medicaid-goes-broke'
health_article_url = '/health/stomach-ulcer'
starting_date = '2017-08-01'

if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Rebuild API Smoke Tests started at : %s on %s ****************************' %(str(start_time), test_env.upper())
    print ''
    print '' 
    print 'Test 01: Get Data News Feed - Get Method'
    apiUtils.get_data_news_feed(test_env)
    print ''
    print 'Test 01a: Get Data News Feed start = 0 and rows = 10'
    apiUtils.get_data_news_feed_sc(test_env, 0, 10)
    print ''
    print 'Test 01b: Get Data News Feed start = -1 and rows = 10'
    apiUtils.get_data_news_feed_sc(test_env, -1, 10)
    print ''
    print 'Test 01c: Check Health Feed: for /human-body-maps/ pattern'
    apiUtils.check_health_feed(test_env)
    print ''
    print 'Test 01d: Check Diabetes Mine Feed'
    apiUtils.check_diabetes_mine_feed(test_env)
    print ''
    print 'Test 02: Get Article Content - Get Method'
    if extensive_tests:
        # Run after hours, there are alot of health articles.
        print 'Now Running Extensive Tests for Article Content...'
        print '========================================================'
        print ''
        article_list = apiUtils.get_article_links(test_env, sitemap_url)
        if debug:
            print 'Number of Articles to check: %i'%len(article_list)
            print ''
        print 'Now Running Extensive Tests for Ads Data...'
        print '========================================================'
        print ''
        for article in article_list:
            apiUtils.get_article_content(test_env, article)
        print ''
        print 'Test 02b: Get Ads Data - Get Method'
        for article in article_list:
            apiUtils.get_ad_data(test_env, article)
        print ''
    else:
        apiUtils.get_article_content(test_env, article_url)
        print ''
        print 'Test 02a: Get Article Content - Full Url - Get Method'
        apiUtils.get_article_content(test_env, full_article_url)
        print ''
        print 'Test 02b: Get Sponsored Landing Data - Get Method'
        apiUtils.get_sponsored_page(test_env, sponsored_article_url)
        print ''
        print 'Test 02c: Get Facebook, Twitter, og, og:url & canonical'
        apiUtils.check_metadata(test_env, fb_twitter_og_article_url)
        print ''
        print 'Test 02d: Check msiteId & msiteactive value'
        apiUtils.check_msite(test_env, msite_article_url)
        print ''
        print 'Test 02e: Check Recommended Section Article url should not be listed'
        for article in recommended_article_list:
            apiUtils.check_recommended(test_env, article)
        print ''
        print 'Test 02f: Checked for stripped out html tags embedded via cms'
        apiUtils.check_for_stripped_htlm_tags(test_env, '/health/arthritis')
        print ''
        print 'Test 03: Get Ads Data - Get Method'
        apiUtils.get_ad_data(test_env, article_url)
        print ''
        print 'Test 03a: Get Ads Data - Full Url - Get Mehod'
        apiUtils.get_ad_data(test_env, full_article_url)
        print ''
        apiUtils.get_article_content(test_env, article_url)
        print ''
        print 'Test 04: Get Top Stories - Get Method'
        apiUtils.get_top_stories(test_env, global_url)
        print ''
        print 'Test 04a: Get Top Stories - Full Url - get Method'
        apiUtils.get_top_stories(test_env, full_article_url)
        print ''
        print 'Test 05: Get Byline Data - Get Method'
        apiUtils.get_byline(test_env, byline_article_url)
        print ''
        print 'Test 06: Set Newsletter Signup - Post Method'
        apiUtils.set_newsletter_signup(test_env, email_payload)
        print ''
        print 'Test 06a: Set Newsletter Signup - Bad Email - Post Method'
        apiUtils.set_newsletter_signup(test_env, bad_email_payload)
        print ''
        print 'Test 07: Set Emaillink - Post Method'
        apiUtils.set_emaillink(test_env, email_pagelink_payload)
        print ''
        print 'Test 07a: Set Emaillink - Bad Email - Post Method'
        apiUtils.set_emaillink(test_env, bad_email_pagelink_payload)
        print ''
        print 'Test 07b: Set Emaillink - Non HL Url - Post Method'
        apiUtils.set_emaillink(test_env, non_hl_email_pagelink_payload)
        print ''
        print 'Test 08: Get Nutrition Feed Start = 1 and Rows = 5 - Get Method'
        apiUtils.get_nutrition_feed_sr(test_env, 0, 5)
        print ''
        print 'Test 08a: Get Nutrition Feed Start = -1 and Rows = 5 - Get Method'
        apiUtils.get_nutrition_feed_sr(test_env, -1, 5)
        print ''
        print 'Test 09: Get GA Data - Get Method'
        apiUtils.get_ga_data(test_env, '/health/stomach-ulcer')
        print ''
        print 'Test 09a: Get GA Data Full Url - Get Method'
        apiUtils.get_ga_data(test_env, full_ga_article_url)
        print ''
        print 'Test 10: Set User Feedback - Post Method'
        apiUtils.test_feedback(test_env, create_feedback_payload)
        print '' 
        print 'Test 10a: Set User Feedback - Bad Email - Get Method'
        apiUtils.test_feedback(test_env, bad_email_create_feedback_payload)
        print ''
        print 'Test 10b: Set User Feedback - Non HL Link - Get Method'
        apiUtils.test_feedback(test_env, non_hl_link_create_feedback_payload)
        print '' 
        print 'Test 11: Get News Feed by Num and Category - Get Method'
        apiUtils.get_feed_category(test_env, category_list)
        print '' 
        print 'Test 12: Check Sponsored Contents: AN urls - Get Method'
        apiUtils.check_sponsored_urls(test_env, sponsored_content_url, recommended_an_items)
        print '' 
        print 'Test 13: Get Share Bar Attributes - Health News - Get Method'
        apiUtils.get_sharebar_attributes(test_env, health_news_url)
        print ''
        print 'Test 14: Get Share Bar Attributes - Health Article - Get Method'
        apiUtils.get_sharebar_attributes(test_env, health_article_url)
        print '' 
        if test_env == 'qa' or test_env == 'stage' or test_env == 'qa_gateway' or test_env == 'stage_gateway':
            print 'Test 14a: Get Sessions Count - Get Method'
            apiUtils.get_sessions_count(test_env, health_article_url, starting_date)
        print ''
        print 'Test 15: Get Tabs from the Global Header - Get Method'
        apiUtils.get_global_header_data(test_env)
        print ''
        print 'Test 16: Sponsored Page - Get Method'
        apiUtils.get_sponsored_page(test_env, sponsored_article_url)
        # apiUtils.get_sponsored_page(test_env, '/program/135317')
        print ''
        print 'Test 16a: Check Bottom Recommended Widget'
        apiUtils.check_sponsored_widget(test_env, embedded_sponsored_article_url, 'Bottom')
        print ''
        print 'Test 16b: Check Article List Widget'
        apiUtils.check_sponsored_widget(test_env, embedded_sponsored_article_url, 'List')
        print ''
        print 'Test 16c: Check Responsive Video Wiget'
        apiUtils.check_sponsored_widget(test_env, embedded_sponsored_article_url, 'Responsive')
        print ''
        print 'Test 16d: Check Take Action Widget'
        apiUtils.check_sponsored_widget(test_env, embedded_sponsored_article_url, 'Action')
        print ''
    end_time = datetime.datetime.now()
    print '**************************** Rebuild API Smoke Tests finished at : %s on %s ****************************' %(str(end_time), test_env.upper())