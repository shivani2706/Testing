''' Author: chaz
    Date: 09.08.17
    This script utilizes the spreadsheet: https://docs.google.com/spreadsheets/d/1ZQzemc-x3rFqHUJlgiDgGxPmlxlHCWUIC3EsO3W8s4k/edit#gid=0
    to verify that an article is associated with a sympton listed in it. Columns F and J are used.
    An api call for SXC with the symptom and the body is check for content, so it should not be None (in Java Null). 
'''
import gspread
import sys
import datetime
from oauth2client.service_account import ServiceAccountCredentials
import apiUtils

#test_env = sys.argv[1].lower()
test_env = 'qa'
cell_list = []
symptoms_list = []
check_symptom_list =[]
debug = False

if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Symptom Checker Tests started at : %s on %s ****************************' %(str(start_time), test_env.upper())
    print ''
    # use creds to create a client to interact with the Google Drive API
    scope = ['https://spreadsheets.google.com/feeds']
    creds = ServiceAccountCredentials.from_json_keyfile_name('client_secret.json', scope)
    client = gspread.authorize(creds)
    # Find a workbook by name and open the first sheet
    sheet = client.open("SxC: Multiple Application IMUID fixes").sheet1
    # all_cells = sheet.range('F1:J70')
    cell_list = sheet.get_all_values()
    for c_list in cell_list:
        if 'DONE' in c_list:
            symptoms_list.append(c_list) 
    if debug:
        print 'Symptoms List: %s'%symptoms_list
        print 'Number of Symptoms to check: %i'%len(symptoms_list)
    for symptoms in symptoms_list:
        check_symptom_list.append(symptoms[5])
    if debug:
        print 'Check Symptom List: %s'%check_symptom_list
        print 'Number of Symptoms to check: %i'%len(check_symptom_list)
    # Use SxC API to check each symptom for an article.
    apiUtils.check_symptom(test_env, check_symptom_list)
    print '' 
    end_time = datetime.datetime.now()
    print '**************************** Symptom Checker Tests finished at : %s on %s ****************************' %(str(end_time), test_env.upper())
            
            