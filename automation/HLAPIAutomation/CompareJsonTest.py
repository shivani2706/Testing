"""
Author: chaz
Date: 01.08.18
Script: CompareJsonTests.py
Description: This script will get the articles jsons from nfs and s3 directories
you have to download them from each source
A comparison of the jsons are then compared k/v to k/v for descreprencies, which will be outputted 
in the console or redirected to a text file (preferred method).

"""
import apiUtils
import datetime
import sys
import json
import os

# Global variables
health_article_list = []
hl_key_list = ["ads", "metadata", "recommended", "ga", "byline", "data", "sharebar", "meta"]
feed_key_list = ["home_page_url", "_hl_results", "_adData", "title", "version", "items"]
health_and_news_sitemap = '/hlcms.xml'
nutrition_sitemap = '/hlan.xml'
test_env = 'stage'
test1_env = 'qa'
test2_env = 'stage'
nfs_path = 'nfs_%s'%test_env + '/'
s3_path = 's3_%s'%test_env + '/'
article_json_dict = {'Standard Article':'healthfeature-chaz-test-article.json', 'DM':'partner_article-diabetesmine__chaz-test-dm-article.json', 'Nutrition':'authoritynutrition-chaztest05.json', 
                     'Health News':'newsarticles-health-news__chaz__test02.json', 'Tabbed Article':'healthfeature-138603.json', 'Global Header':'global_header-138604.json', #'Global Footer':'global_footer-138605.json',
                     'Article Listing':'article_widget-138591.json', 'Take Action Widget':'article_widget-138596.json', 'Pull Quotes Widget':'article_widget-138606.json',
                     'Responsive Image Widget':'article_widget-138607.json', 'Fact Box Widget':'article_widget-138608.json', 'Ad Widget':'article_widget-138609.json',
                     'Highlight Widget':'article_widget-138610.json', 'Q&A Widget':'article_widget-138611.json', 'Testimonial':'article_widget-138612.json', 'Quote Card Widget':'article_widget-138613.json',
                     'Pros and Cons Widget':'article_widget-138614.json', 'Sponsored Small Image Listing Widget':'sponsored_program_widget-138597.json', 'Sponsored Video Widget':'sponsored_program_widget-138566.json',
                     'Sponsored Article Widget':'sponsored_program_widget-138615.json', 'Sponsored Hero Listing Widget':'sponsored_program_widget-138616.json', 
                     'Sponsored Medium Image Listing Widget':'sponsored_program_widget-138617.json', 'Sponsored Text Only Listing Widget':'sponsored_program_widget-138618.json'
                     }
debug = True

if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Compare Json from s3 to nfs Tests finished at : %s on %s ****************************' %(str(start_time), test_env.upper())
    print ''
    if debug:
        print '[Info]: NFS path: %s and S3 path: %s'%(nfs_path, s3_path)
        print ''
    for key in article_json_dict:
        print '[Info]: Now Comparing json: %s from s3 and nfs for Article: %s'%(article_json_dict[key], key)
        print '**************************************************************************************************************'
        apiUtils.compare_pub_jsons(s3_path + article_json_dict[key], nfs_path + article_json_dict[key])
        
    end_time = datetime.datetime.now()
    print '**************************** Compare Json from s3 to nfs Tests finished at : %s on %s ****************************' %(str(end_time), test_env.upper())
    