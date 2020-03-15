"""
Author: chaz
Date: 11.02.17
Script: MSBuddyAPITests.py
Description: This script is used for smoke testing the mobile Application's API
Supports only the new BE with RDBMS - MySQL
"""

import apiUtils
import datetime
import sys
import json
import requests
from requests_oauthlib import OAuth1

# Passed in test environment i.e.: qa, stage or prod.
#test_env = sys.argv[1].lower()
test_env = 'stage'
# Passed in mobile application i.e.: msbuddy or flagship or wobuddy.
#app_key = sys.argv[2].lower()
app_key = 'msbuddy'
# Passed in the Backend version i.e: V3 or V4 or V5 (for now).
#be_version = sys.argv[3]
be_version = 'V7'
# Passed in application version i.e.: 2.0.3, 2.0.4, 2.4.2, 2.5.2, 2.6.2 or 3.0.0.
#app_version = sys.argv[4]
app_version = '3.0.5'
# Passed in device type i.e.: iOS or android.
#device_type = sys.argv[5]
#device_type = 'Android'
# Social Provider: facebook, google or healthline.
# provider = sys.argv[6]
provider = 'healthline'
# Flag to use the version for FE 2.0.2 or greater.
# use_app_version = sys.argv[6]
 

#print 'The values for use_access_token: %s and use_app_version: %s' %(use_access_token, use_app_version)

list_of_users_info = [["tester01@healthline.com", "01", "Male", "http://cdn.bgr.com/2016/06/darth-vader-star-wars-rogue-one.jpg", "Female"],
                     ["tester02@healthline.com", "02","Female", "https://www.thewrap.com/wp-content/uploads/2016/06/dragon-game-of-thrones.jpeg", "Male"]]
# For BE V6 and on.
new_apps_api_calls = {"check_server":"/checkServer","create_user_profile":"/createUserProfile/", "get_user_profile":"/getUserProfile/", "login":"/logIn", 
                      "mutual_friends":"/mutualFriends/", "my_user_profile":"/myUserProfile/", "delete_user":"/deleteUser/", "todays_match":"/todaysMatch/", 
                      "unread_message_count":"/unreadMessageCount/", "unread_message_count_only":"/unreadMessageCountOnly/",
                      "fetch_conversation_list":"/fetchConversationList/", "user_setting":"/userSetting/",
                      "get_user_settings":"/getUserSettings/", "logout":"/logOut/", "update_user_profile":"/updateUserProfile/",
                      "set_user_status":"/setUserStatus/", "about_us":"/aboutUs/", "privacy_policy":"/privacyPolicy/", "terms_and_conditions":"/termsAndConditions/", 
                      "validate_email_reg_info":"/validateEmailRegInfo/", "email_login":"/emailLogin/", "reset_password":"/resetPassword/", "conversion_user":"/conversionUser/",
                      "conversion_release":"/conversionRelease/", "run_inactivate":"runInactivate", "update_user_profile":"/updateUserProfile/", "matchAB":"/dailyMatchAB/",
                      "send_push_notification":"/sendNotification/"}


apps_list = {"msbuddy":"/MSBuddy-" + be_version, "wobuddy":"/WOBuddy-" + be_version}
protocol = "https://"
domain = ".healthline.com"
stage_env_url = "msbuddy-stage"
qa_env_url = "msbuddy-qa"
seung_env_url = "10.1.0.103:8380"
api_url = ''
ms_access_key = 'RUFBQ3hkRW9zZTBjQkFNRFN2NWJ1UDJ2WWtVdHZ4bkpEeDRBNjRjeXdKcFJqMVJPUkZjcnI4YlFsa3JaQWNINXhVd0VlQTdsYWc3YnBQcUhzdDZvNVFFZTl1eFVkdWNmQUxNWkFpMlQ0Y1d4QjN2VWRPaVM2bjdvREthOU5sQ1dxOGQxWkJrWFJnN1pCUlpDMmtvcHFScGZvM3Z6QlJOcHJkY2lEMVpBaW1mYndaRFpE'
http_headers = {'OAUTH-ACCESS-TOKEN':ms_access_key, 'OAUTH-PROVIDER':provider, 'OAUTH-USER-ID':'tester01@healthline.com', 'APP-VERSION':app_version}
 
# Current, may change if we switch cloud services i.e.: Apple or Google.
chaz_iOS_token = 'eaa37658ed013e4e578f1176ad1e84cbee26714a34562830e754ab21c59fb286'
chaz_android_token = 'eloWMdFmBBg:APA91bHs6o_GDVZfj7j0gwaDxWd47yoM7Yv06OmbPGcQgY6FkfBBXJoEZ9pwxgOpdh2HnpKM1sF46HmdEcHGm49972WB5OJeJYCBA9AAlyk85PAycBNlG96oI3NSwZksT8IeiET9LtBX'
push_message = 'Test Push Notification via api'
# solr queries.
delete_ms_app_profile = "https://vmc-apm01-stage.healthline.com/solr/msb_core/update?stream.body=<delete><query>id:1131685073510199</query></delete>&commit=true"
delete_wo_app_profile = "http://vmc-apm01-stage.healthline.com/solr/msb_core/update?stream.body=<delete><query>id:1133560829989277</query></delete>&commit=true"
undelete_ms_app_profile_stage = "https://vmc-apm01-stage.healthline.com/solr/msb_core/update?stream.body=<add><doc><field name=\"id\">1131685073510199</field><field name=\"deleted\" update=\"set\">false</field></doc></add>&commit=true"
undelete_wo_app_profile_stage = "http://vmc-apm01-stage.healthline.com/solr/wob_core/update?stream.body=<add><doc><field name=\"id\">1131685073510199</field><field name=\"deleted\" update=\"set\">false</field></doc></add>&commit=true"
undelete_ms_app_profile_prod = "https://msbuddy.healthline.com/solr/msb_core/update?stream.body=<add><doc><field name=\"id\">1131685073510199</field><field name=\"deleted\" update=\"set\">false</field></doc></add>&commit=true"
undelete_wo_app_profile_prod = "http://wobuddy.healthline.com/solr/msb_core/update?stream.body=<add><doc><field name=\"id\">1131685073510199</field><field name=\"deleted\" update=\"set\">false</field></doc></add>&commit=true"
device_tokens_stage = "http://vmc-apm01-stage.healthline.com/solr/wob_core/select?q=*:*&fq=contenttype:wob_device&rows=999"
test_count = 0
error_counter = 0
failed_count = 0
passed_count = 0 
result = ''
response = ''

def get_device_tolken(user_id):
    # Use to get the device token for a given user.
    app_obj = ''
    # Account for the application name.
    if app_key == 'msbuddy':
        app_obj = 'msb'
    else:
        app_obj = 'wob'
    lookup_device_token_qa = "https://msbuddy-qa.healthline.com/solr/" + app_obj +"_core/select?q=*:*&fq=contenttype:" + app_obj + "_device AND userId:" + user_id + "&rows=999"
    lookup_device_token_stage = "https://msbuddy-stage.healthline.com/solr/" + app_obj +"_core/select?q=*:*&fq=contenttype:" + app_obj + "_device AND userId:" + user_id + "&rows=999"
    lookup_device_token_prod = "https://msbuddy.healthline.com/solr/"
    # To Do: examine the json.
    # To Do: Parse it for the devicetoken.
    
def create_payload(apps_api_call, be_version, user_id):
    # Pass in the apps_api_call for a post that needs a payload.
    payload = {}
    
    if apps_api_call == "send_message":
        payload = {"userId1":"56789", "userId2":"12345", "message":"Hi, message sent via auto api..."}    
    elif apps_api_call == "enable_push_notification":
        payload = {"userId1":user_id, "propertyName":"enablePushNotification", "value":False}
    elif apps_api_call == "daily_match":
        payload = {"userId1":user_id, "propertyName":"dailyMatch", "value":False}
    elif apps_api_call == "update_user_profile":
        payload = {"oauthId":user_id,
                    "birthYear":"1980",
                    "birthDate":"1980-10-08",
                    "gender":"Male",
                    "firstName":"HLMSTesterAPI",
                    "lastName":"C.",
                    "zipCode":"94107",
                    "profilePic":"http://cdn.bgr.com/2016/06/darth-vader-star-wars-rogue-one.jpg",
                    "childStatus":"true",
                    "location":"94107",
                    "msStartYear":"2017",
                    "msTypeCode":"Secondary-progressive",
                    "medicationCodeList":["Ampyra",
                                          "Deltasone"],
                    "questionText":"bad-day-boost",
                    "questionText":"I'm having a bad day, what advice do you have to boost my spirit?",
                    "status":"Active",
                    "emailAddress":"tester01@healthline.com",
                    "password1":"11111111",
                    "enablePushNotification":"true",
                    "dailyMatch":"true",
                    "deviceType":"iOS",
                    "deviceToken":"abcde-111",
                    "displayName":"tester01",
                    "whoKnowsCode":"friends-family",
                    "ageOptionCode":"school",
                    "workStatusCode":"full-time",
                    "enableSubscriptionLetter":"true",
                    "symptomCodeList": [
                                        "fatigue",
                                        "cognitive-function"
                                        ],
                    "relationshipStatusCode":"single"
                    }
    elif apps_api_call == "email_login":
        payload = {"userId1":user_id,
                    "password1":"11111111",
                    "deviceToken":"abcde-111"
                    }
    elif apps_api_call == "delete_user":
        payload = {"userId1":user_id} 
    print 'payload: %s'%payload  
    return payload

def set_http_headers():
    # Set the http headers accordingly.
    return http_headers
        

def get_reset_url(test_env, app_key):
    if test_env == 'stage':
        if app_key == 'msbuddy':
            return undelete_ms_app_profile_stage
        elif app_key == 'wobuddy':
            return undelete_wo_app_profile_stage
    else:
        if app_key == 'msbuddy':
            return undelete_ms_app_profile_prod
        elif app_key == 'wobuddy':
            return undelete_wo_app_profile_prod
 
def create_api_user(info_list, http_headers, backend_version):
    payload = {}
    response = ''
    failed_count = 0
    
    payload =  {"oauthId":info_list[0],
                    "birthYear":"1980",
                    "birthDate":"1980-10-08",
                    "gender":info_list[2],
                    "firstName":"HLMSTesterAPI" + info_list[1],
                    "lastName":"C.",
                    "zipcode":"94107",
                    "location":"94107",
                    "ProfilePic":info_list[3],
                    "msStartYear":"2017",
                    "msCondition":"Secondary-progressive",
                    "msTypeCode":"Secondary-progressive",
                    "medication":["Ampyra"],
                    "medicationCodeList":["Ampyra"],
                    "questionCode":"which-medications",
                    "questionText":"Which medications have been most effective for you?",
                    "emailAddress":info_list[0],
                    "password1":"11111111",
                    "enablePushNotification":"true",
                    "dailyMatch":"true",
                    "deviceType":"android",
                    "deviceToken":"adbce-222",
                    "displayName":"tester01",
                    "whoKnowsCode":"just-me",
                    "ageOptionCode":"school",
                    "workStatusCode":"full-time",
                    "enableSubscriptionLetter":"true",
                    "symptomCodeList": [
                                        "fatigue",
                                        "bladder-bowel",
                                        "cognitive-function"
                                        ],
                    "relationshipStatusCode":"single"
                }
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["create_user_profile"], http_headers, payload, True, app_key, True, failed_count)
    return (response, failed_count) 
    
def create_api_users(last_user, ms_access_key, provider, app_version, device): 
    # Use to create several users with different id number but 
    # has the same profile data.
    i = 1
    http_headers = {'OAUTH-ACCESS-TOKEN':ms_access_key, 'OAUTH-PROVIDER':provider, 'OAUTH-USER-ID':i, 'APP-VERSION':app_version, 'DEVICE-TYPE':device}
    error_counter = 0
    results = ()
    
    for i in range(1, last_user):
        payload = {"oauth_id":i,
                   "maritalstatus":"false",
                   "childstatus":"true",
                   "age":"29",
                   "gender":"Female",
                   "firstname":"HLMSTesterAPI" + i,
                   "lastname":"C.",
                   "zipcode":"94107",
                   "profilepic":"",
                   "msexperience":"1 Years",
                   "mscondition":"Secondary-progressive",
                   "msTypeCode":"Secondary-progressive",
                   "medication":["Ampyra"],
                   "question":"How do you cope with MS?",
                   "emailaddress":"tester" + i + "@healthline.com",
                   "enablePushNotification":"true",
                   "dailyMatch":"true",
                   "enableSubscriptionLetter":"true",
                }
        
        post_url = apiUtils.get_app_env_url(test_env) + new_apps_api_calls["create_user_profile"]
        print 'The post url is: %s'%post_url
        print 'Now making the Post call to create user %s...'%payload["firstname"]
        results = apiUtils.app_make_post(post_url, http_headers, payload, True, app_key, True) 
        print 'User %s created...'%payload["firstname"]
        i += 1
    return results[1]

def send_push_nofication(device_token, device_type, message, show_payload, app, rtn_json, failed_count):
    results = ()
    # Use to send a test push notification to the phone directly.
    push_url = apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["send_push_notification"] + device_token + "/" + device_type + "/" + message
    results = apiUtils.app_make_request(push_url, http_headers, show_payload, app, rtn_json, failed_count)
    return results[1]

def test_push_nofication(user_id, type, message, show_payload, app, rtn_json, failed_count):
    results = ()
    # Use to send a test push notification to the phone directly.
    http_header = {} # Passed for the method, but for test api calls no header is needed.
    push_url = apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["send_push_notification"] + user_id + "/" + type + "/" + message
    results = apiUtils.app_make_request(push_url, http_header, show_payload, app, rtn_json, failed_count)
    return results[1]
    
def match_a_to_b(user_id, number_of_times):
    i = 0
    http_headers = {}
    for i in range(0, number_of_times):
        print'Now matching user_id: %s to %i...'%(str(user_id), i)
        match_a_to_b_api = apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["matchAB"] + str(user_id) + '/' + str(i) + '/false'
        apiUtils.app_make_request(match_a_to_b_api, http_headers, True, app_key, True)
    i += 1
    
if __name__ == '__main__':  
    start_time = datetime.datetime.now() 
    print '**************************** Application: %s API Smoke Tests started at : %s on %s ****************************' %(app_key, str(start_time), test_env)
    #print 'Setup: Now resetting the user via solr...'
    #apiUtils.make_request(get_reset_url(test_env, app_key), True)
    #print 'Now unblocking the user...'
    #apiUtils.make_post(apiUtils.get_app_env_url(test_env) + apps_api_calls["unblock_user"] + "12345/56789", create_payload("unblock_user"), True)
    print ''
    print ''
    print 'Test 00: Check Server: - Get Method' 
    print ''
    test_count += 1
    (response, failed_count) = apiUtils.app_make_request(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["check_server"], set_http_headers(), True, app_key, True, failed_count)
    if response["status"] == 0 and response["message"] == "Server test done successfully":
        print 'Test 00 Passed...'
    else:
        print 'Test 00 Failed!!!'
        failed_count += 1
    print ''
    
    print 'Setup: Delete Users - Just in case they exists'
    print ''
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["delete_user"], set_http_headers(), create_payload("delete_user", be_version, list_of_users_info[0][0]), True, app_key, True, failed_count)
    print ''
    print 'Test 01: Create User: - Post Method'
    print ''
    # Creating 2 users so they can converse with one another.
    test_count += 1
    
    (response, failed_count) = create_api_user(list_of_users_info[0], set_http_headers(), be_version)
    if response["status"] == 0 and response["message"] == "user profile created successfully":
        print 'Running MySQL Query...'
        results = apiUtils.get_db_results(test_env, "SELECT * from `user_profile` where `user_id` = 'tester01@healthline.com';")
        if results[0]['sendbird_access_token'] != None and results[0]['access_token'] != None:
            print 'Found sendbird access token: %s and access token: %s'%(results[0]['sendbird_access_token'], results[0]['access_token'])
        print 'Test 01 Passed...'
    else:
        print 'Test 01 Failed!!!'
        failed_count += 1
        print ''
    # Test 02 does the Validation.
    print ''
    print 'Test 02: My Profile: - Post Method'
    print ''
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["my_user_profile"], set_http_headers(), {"userId1":list_of_users_info[0][0]}, True, app_key, True, failed_count)
    if response["status"] == 0 and response["data"]["user"]["oauthId"] == list_of_users_info[0][0]:
        print 'Test 02 Passed...'
    else:
        print 'Test 02 Failed!!!'
        failed_count += 1
    ## To do: Validation.
    print ''
    print 'Test 03: Update Profile: - Post Method'
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["update_user_profile"], set_http_headers(), create_payload("update_user_profile", be_version, "tester01@healthline.com"), True, app_key, True, failed_count)
    if response["status"] == 0 and response["message"] == "User profile updated successfully":
        print 'Running MySQL Query...'
        print "Checking for User's Medication..."
        results = apiUtils.get_db_results(test_env, "select medication_code from user_medication where user_id = 'tester01@healthline.com';")
        if len(results[0]['medication_code']) == 2:
            print "The User's Medication: %s were updated correctly"%results[0]['medication_code']
        print 'Running MySQL Query...'
        print "Checking for User's Symptoms..."
        results = apiUtils.get_db_results(test_env, "select symptom_code from user_symptom where user_id = 'tester01@healthline.com';")
        if len(results[0]['symptom_code']) == 2:
            print "The User's Symptoms: %s were updated correctly"%results[0]['symptom_code']
        print 'Test 03 Passed...'
    else:
        print 'Test 03 Failed!!!'
        failed_count += 1
    print ''
    print 'Test 03b: My Profile: - Post Method'
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["my_user_profile"], set_http_headers(), {"userId1":"tester01@healthline.com"}, True, app_key, True, failed_count)
    if response["status"] == 0 and response["message"] == "User details fetched successfully":
        print 'Test 03b Passed...'
    else:
        print 'Test -3b Failed!!!'
        failed_count += 1
    print ''
    print 'Test 04: Test Push Notification: - Get Method'
    test_count += 1
    failed_count = send_push_nofication('cchoy@healthline.com', 'test', push_message, True, app_key, True, failed_count)
    print '' 
    print 'Test 05: Email Login - Post Method'
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["email_login"], set_http_headers(), create_payload("email_login", be_version, "tester01@healthline.com"), True, app_key, True, failed_count) 
    print ''
    print 'Test 06: Get User Setting - Post Method'
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["get_user_settings"], set_http_headers(), {"userId1":"tester01@healthline.com"}, True, app_key, True, failed_count)
    if response["status"] == 0 and response["data"] != None:
        print 'Test 06 Passed...'
    else:
        print 'Test 06 Failed!!!'
        failed_count += 1
    print ''
    print 'Test 07: About Us - Get Method'
    test_count += 1
    if be_version in ['V6', 'V7']:
        (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["about_us"], set_http_headers(), {}, True, app_key, True, failed_count)
        if response["status"] == 0 and response["data"]["aboutUs"] != None:
            print 'Test 08 Passed...'
        else:
            print 'Test 08 Failed!!!'
            failed_count += 1
    print ''
    print 'Test 08: Privacy Policy - Get Method'
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["privacy_policy"], set_http_headers(), {}, False, app_key, False, failed_count)    
    print ''
    print 'Test 09: Terms And Conditions - Get Method'
    test_count += 1
    apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["terms_and_conditions"], set_http_headers(), {}, False, app_key, False, failed_count)
    print ''
    print 'Test 10: Enable Push Notification - Post Method'
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["user_setting"], set_http_headers(), create_payload("enable_push_notification", be_version, "tester01@healthline.com"), True, app_key, True, failed_count)
    if response["status"] == 0 and response["message"] == "success":
        print 'Test 10 Passed for Setting Option: Push Notifications to On...'
    else:
        print 'Test 10 Failed for Setting Option: Push Notification to On!!!'
        failed_count += 1
        (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["get_user_settings"], set_http_headers(), {"userId1":"tester01@healthline.com"}, True, app_key, True, failed_count)
        if response["status"] == 0 and response["data"]["enablePushNotification"] == True:
            print 'Test 10 Passed for Get User Settings Option: Push Notifications...'
        else:
            print 'Test 10 Failed for Get User Settings Option: Push Notifications!!!'
            failed_count += 1
    print ''
    print 'Test 11: Disable Daily Match - Post Method'
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["user_setting"], set_http_headers(), create_payload("daily_match", be_version, "tester01@healthline.com"), True, app_key, True, failed_count)
    if response["status"] == 0 and response["message"] == "success":
        print 'Test 11 Passed for Setting Option: Daily Match...'
    else:
        print 'Test 11 Failed for Setting Option: Daily Match!!!'
        failed_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["get_user_settings"], set_http_headers(), {"userId1":"tester01@healthline.com"}, True, app_key, True, failed_count)
    if response["status"] == 0 and response["data"]["dailyMatch"] == False:
        print 'Test 11 Passed for Getting Option: Daily Match...'
    else:
        print 'Test 11 Failed for Getting Option: Daily Match!!!'
        failed_count += 1
    print ''
    print 'Test 12: Today\'s Match - Post Method'
    test_count += 1
    (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["todays_match"], set_http_headers(), {"userId1":"tester01@healthline.com"}, True, app_key, True, failed_count)
    if response["status"] == 0 and response["message"] == "TodaysMatch service completed successfully":
        print 'Test 12 Passed...'
    else:
        print 'Test 12 Failed!!!'
        failed_count += 1
    print ''
    '''print 'Test 13: Delete User - Post Method'
    test_count += 1
    for user_id in list_of_users_info:
        (response, failed_count) = apiUtils.app_make_post(apiUtils.get_app_env_url(test_env, be_version, app_key) + new_apps_api_calls["delete_user"], set_http_headers(), create_payload("delete_user", be_version, user_id[0]), True, app_key, True, failed_count)
        if response["status"] == 0 and response["message"] == "Success: User deleted: %s"%user_id[0]:
            print 'Test 13 Passed...'
        else:
            print 'Test 13 Failed!!!'
            failed_count += 1
    # create_api_users(1, ms_access_key, provider, app_version, device_type)
    # match_a_to_b(285526881798694, 500)
    '''
    end_time = datetime.datetime.now() 
    print ''
    passed_count = test_count - failed_count
    print 'Total Tests Ran: %i' %test_count
    print 'Total Tests Passed: %i' %passed_count
    print 'Total Tests Failed: %i' %failed_count
    print ''
    print '**************************** Application: %s API Smoke Tests finished at : %s on %s Version: %s ****************************' %(app_key, str(end_time), test_env, be_version)
    print ''
    if failed_count > 0:
        print 'Some Test(s) Failed!!!'
        sys.exit(1)
    else:
        print 'All Tests Passed...'
        sys.exit(0)
        
