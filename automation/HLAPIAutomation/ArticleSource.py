"""
Author: chaz
Date: 06.07.16
Script: ArticlesSources.py
Description: This script is used for testing the various article pages mainly in the viewed source way.
             Checks: 
             uaProperty ID
             Platform page   
Usage: Pass in the test server.

"""
entered_env01 = sys.argv[1].lower()

dict_of_sites = {'Healthline':'UA-26124207-1', 'Drugs.com':'UA-26124207-2', 'Livestrong':'UA-26124207-6'}

HL_dict_of_Articles = {'Regular':'/health/multiple-sclerosis/top-iphone-android-apps',
                       'Topic Center Tab':'/health/multiple-sclerosis/managing',
                       'Slide Show':'/healthline/health-slideshow/top-ms-events-around-the-country',
                       'Tab':'/healthline/health/multiple-sclerosis/exacerbation-ms-attack',
                       'Video':'/health/multiple-sclerosis/newly-diagnosed-what-to-expect'
                       }

Drugs_dict_of_Articles = {'Regular':'/health/ms/mobile-apps-1453/',
                          'Topic Center Tab':'/health/ms/',
                          'Slide Show':'/health/ms/country-events-2677/',
                          'Tab':'/health/ms-treatment/exacerbation-2687/',
                          'Video':'/health/ms-treatment/expectations-2564/'
                          }

Livestrong_dict_of_Articles = {'Regular':'/healthline/health/multiple-sclerosis/top-iphone-android-apps',
                               'Topic Center Tab':'/health/multiple-sclerosis/managing',
                               'Slide Show':'/healthline/health-slideshow/top-ms-events-around-the-country',
                               'Tab':'/healthline/health/multiple-sclerosis/exacerbation-ms-attack',
                               'Video':'/healthline/health/multiple-sclerosis/newly-diagnosed-what-to-expect'
                               }

for site in list_of_sites:
    print "Now checking on %s Articles..."% site
    if site == 'Healthline':
        for key in HL_dict_of_Articles:
            
         
                               
                               
                               
                               
                               
                               
    