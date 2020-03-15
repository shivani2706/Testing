"""
Author: chaz
Date: 10.02.17
Script: MSBuddyCompareJson.py
Description: This script is used for comparing two jsons from Application's API
from 2 different BEs

"""

import apiUtils
import datetime
import sys
import json
import subprocess
import os
import time


# Passed in test environment i.e.: qa, stage or prod.
#test_env = sys.argv[1].lower()
test_env_1 = 'qa'
test_env_2 = 'sb'
# Passed in mobile application i.e.: msbuddy or flagship or wobuddy.
#app_key = sys.argv[2].lower()
app_key = 'msbuddy'
# Passed in the Backend version i.e: V3 or V4 or V5 (for now).
#be_version = sys.argv[3]
be_version = 'V6'
# Passed in application version i.e.: 2.0.3, 2.0.4, 2.4.2, 2.5.2, 2.6.2 or 3.0.0.
#app_version = sys.argv[4]
app_version = '3.0.3'
# Passed in device type i.e.: iOS or android.
#device_type = sys.argv[5]
#device_type = 'Android'
# Social Provider: facebook, google or healthline.
# provider = sys.argv[6]
provider = 'healthline'
ms_access_key = 'RUFBQ3hkRW9zZTBjQkFNRFN2NWJ1UDJ2WWtVdHZ4bkpEeDRBNjRjeXdKcFJqMVJPUkZjcnI4YlFsa3JaQWNINXhVd0VlQTdsYWc3YnBQcUhzdDZvNVFFZTl1eFVkdWNmQUxNWkFpMlQ0Y1d4QjN2VWRPaVM2bjdvREthOU5sQ1dxOGQxWkJrWFJnN1pCUlpDMmtvcHFScGZvM3Z6QlJOcHJkY2lEMVpBaW1mYndaRFpE'
user_id = 'jb@healthline.com'
http_headers = {'OAUTH-ACCESS-TOKEN':ms_access_key, 'OAUTH-PROVIDER':provider, 'OAUTH-USER-ID':user_id, 'APP-VERSION':app_version}

if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Application: %s Compare Jsons started at : %s on %s and %s ****************************' %(app_key, str(start_time), test_env_1, test_env_2)
    json_1 = apiUtils.app__post(apiUtils.get_app_env_url(test_env_1, be_version, app_key) + '/myUserProfile/', http_headers, {"userId1":user_id}, True, app_key, True)
    json_2 = apiUtils.app__post(apiUtils.get_app_env_url(test_env_2, be_version, app_key) + '/myUserProfile/', http_headers, {"userId1":user_id}, True, app_key, True)
    apiUtils.compare_jsons(json_1, json_2)
    print ''
    print '**************************** Application: %s Compare Jsons started at : %s on %s and %s  ****************************' %(app_key, str(start_time), test_env_1, test_env_2)
    print ''