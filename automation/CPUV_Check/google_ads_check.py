import httplib2
import os
import pandas as pd
import time
import unittest

from apiclient import discovery
from oauth2client import client
from oauth2client import tools
from oauth2client.file import Storage
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait

try:
    import argparse
    flags = argparse.ArgumentParser(parents=[tools.argparser]).parse_args()
except ImportError:
    flags = None

# If modifying these scopes, delete your previously saved credentials
# at ~/.credentials/sheets.googleapis.com-python-quickstart.json
SCOPES = 'https://www.googleapis.com/auth/spreadsheets.readonly'
CLIENT_SECRET_FILE = 'client_secret.json'
APPLICATION_NAME = 'Google Spreadsheet Ads Check'

def find(driver):
    elements = driver.find_elements_by_xpath("//div[@data-line-item-id]")
    if elements:
        return elements
    else:
        return False

def get_credentials():
    """Gets valid user credentials from storage.

    If nothing has been stored, or if the stored credentials are invalid,
    the OAuth2 flow is completed to obtain the new credentials.

    Returns:
        Credentials, the obtained credential.
    """
    cwd = os.getcwd()
    credential_dir = os.path.join(cwd, '.credentials')
    if not os.path.exists(credential_dir):
        os.makedirs(credential_dir)
    credential_path = os.path.join(credential_dir,
                                   'sheets.ads_check.json')

    store = Storage(credential_path)
    credentials = store.get()
    if not credentials or credentials.invalid:
        flow = client.flow_from_clientsecrets(CLIENT_SECRET_FILE, SCOPES)
        flow.user_agent = APPLICATION_NAME
        if flags:
            credentials = tools.run_flow(flow, store, flags)
        else: # Needed only for compatibility with Python 2.6
            credentials = tools.run(flow, store)
        print('Storing credentials to ' + credential_path)
    return credentials


def run_main_script(data_line_item_id, urls):
    # Create a new instance of the Firefox driver
    driver = webdriver.Firefox()

    for url in urls:
        url = url.strip()

        # go to url
        try:
            driver.get(url)

            # Find each class that contains data-line-item-id
            print "URL " + url
            print "=================="

            try:
                elements = WebDriverWait(driver, 20).until(find)
            except Exception:
                print "Can't load. Skipping...\n"
                continue

            if elements:
                for element in elements:
                    element_class = element.get_attribute('class')
                    element_id = element.get_attribute('data-line-item-id')

                    print "Class: " + element_class
                    print "Id: " + element_id

                    if int(element_id) == int(data_line_item_id):
                        result="PASS"
                    else:
                        result="FAIL: id {} != {}".format(element_id, data_line_item_id)
                    print result

            else:
                print "Error: data-line-item-id not found"
        except Exception as e:
            print "ERROR. Message: " + e.message

        print "\n"


    driver.close()

def main():
    credentials = get_credentials()
    http = credentials.authorize(httplib2.Http())
    discoveryUrl = ('https://sheets.googleapis.com/$discovery/rest?'
                    'version=v4')
    sheets_api = discovery.build('sheets', 'v4', http=http,
                              discoveryServiceUrl=discoveryUrl)

    spreadsheetId = '1AjlBANF9QZnOXJ1mfygAQ9q-EeYS6QlPkPpcJ7Lv75M'

    spreadsheet = sheets_api.spreadsheets().get(spreadsheetId=spreadsheetId).execute()

    print "\n======================================================="
    print "           Searching for Campaigns                     "
    print "======================================================="
    campaigns = []
    for sheet in spreadsheet['sheets']:
        try:
            if sheet['properties']['tabColor'] == {u'blue': 0.19215687, u'green': 0.8352941, u'red': 0.73333335}:
                title = sheet['properties']['title']
                print "Found campagn " + title
                campaigns.append(title)
        except KeyError:
            continue

    for campaign in campaigns:
        print "\n======================================================="
        print "Getting data-line-item-id for campaign {}              ".format(campaign)
        print "======================================================="
        ranges = '{}!A1:Z1000'.format(campaign)
        results = sheets_api.spreadsheets().values().batchGet(spreadsheetId=spreadsheetId, ranges=ranges).execute()
        healthline_data_id = ''
        healthline_urls = []
        drugs_data_id = ''
        drugs_urls = []
        livestrong_data_id = ''
        livestrong_urls = []
        cur_campaign = ''
        for row in results['valueRanges'][0]['values']:
            if row and 'Healthline' in row[0]:
                try:
                    healthline_data_id = row[4] # show always be row "E" = index 4
                    healthline_data_id = healthline_data_id.split(';')[0] # If has semi-colon it in
                    try:
                        int(healthline_data_id)
                    except ValueError:
                        print "ERROR: data-line-item-id is not a valid number for Healthline"
                        continue
                    cur_campaign = 'healthline'
                except IndexError:
                    print "ERROR: No data-line-item id found for Healthline"
                    continue

                print "FOUND: Healthline. id = {}".format(healthline_data_id)
                continue
            elif row and 'Drugs' in row[0]:
                try:
                    drugs_data_id = row[4] # show always be row "E" = index 4
                    drugs_data_id = drugs_data_id.split(';')[0] # If has semi-colon it in
                    try:
                        int(drugs_data_id)
                    except ValueError:
                        print "ERROR: data-line-item-id is not a valid number for Drugs"
                        continue
                    cur_campaign = 'drugs'
                except IndexError:
                    print "ERROR: No data-line-item id found for Drugs"
                    continue
                
                print "FOUND: Drugs. id = {}".format(drugs_data_id)
                continue
            elif row and 'Livestrong' in row[0]:
                try:
                    livestrong_data_id = row[4] # show always be row "E" = index 4
                    livestrong_data_id = livestrong_data_id.split(';')[0] # If has semi-colon it in
                    try:
                        int(livestrong_data_id)
                    except ValueError:
                        print "ERROR: data-line-item-id is not a valid number for Livestrong"
                        continue
                    cur_campaign = 'livestrong'
                except IndexError:
                    print "ERROR: No data-line-item id found for Livestrong"
                    continue
                
                print "FOUND: Livestrong. id = {}".format(livestrong_data_id)
                continue

            try:
                if cur_campaign == 'healthline':
                    healthline_urls.append(row[4])
                elif cur_campaign == 'drugs':
                    drugs_urls.append(row[4])
                elif cur_campaign == 'livestrong':
                    livestrong_urls.append(row[4])
            except IndexError:
                print campaign + " is missing some URLs"

        if healthline_urls:
            print "\n======================================================="
            print "Healthline URLs for CAMPAIGN {}: {}            ".format(campaign, healthline_data_id)
            print "======================================================="
            for url in healthline_urls:
                print url

            print "********************************************************"
            print "CHECKING URLs for CAMPAIGN {}: {}            ".format(campaign, healthline_data_id)
            print "********************************************************"
            run_main_script(healthline_data_id, healthline_urls)

        if drugs_urls:
            print "\n======================================================="
            print "Drugs URLs for CAMPAIGN {}: {}         ".format(campaign, drugs_data_id)
            print "======================================================="
            for url in drugs_urls:
                print url

            print "********************************************************"
            print "CHECKING URLs for CAMPAIGN {}: {}         ".format(campaign, drugs_data_id)
            print "********************************************************"
            run_main_script(drugs_data_id, drugs_urls)

        if livestrong_urls:
            print "\n======================================================="
            print "Livestrong URLs for CAMPAIGN {}: {}    ".format(campaign, livestrong_data_id)
            print "======================================================="
            for url in livestrong_urls:
                print url

            print "********************************************************"
            print "CHECKING URLs for CAMPAIGN {} {}    ".format(campaign, livestrong_data_id)
            print "********************************************************"
            run_main_script(livestrong_data_id, livestrong_urls)
        

if __name__ == '__main__':
    main()