# -*- coding: utf-8 -*-
"""Python - BrowserMob - WebDriver"""
from browsermobproxy import Server
from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
import json
import sys
 
class CreateHar(object):
    """create HTTP archive file"""
 
    def __init__(self, mob_path):
        """initial setup"""
        self.browser_mob = mob_path
        self.server = self.driver = self.proxy = None
 
    @staticmethod
    def __store_into_file(title, result):
        """store result"""
        har_file = open(title + '.har', 'w')
        har_file.write(str(result))
        har_file.close()
 
    def __start_server(self):
        """prepare and start server"""
        self.server = Server(self.browser_mob)
        self.server.start()
        self.proxy = self.server.create_proxy()
 
    def __start_driver(self):
        """prepare and start driver"""
        firefox_capabilities = DesiredCapabilities.FIREFOX
        firefox_capabilities['platform'] = "WIN10"
        firefox_capabilities['marionette'] = True
        firefox_capabilities['binary'] = "C:\Program Files\Mozilla Firefox\firefox.exe"
        self.driver = webdriver.Firefox(capabilities=firefox_capabilities)        
        self.driver.maximize_window()
 
    def start_all(self):
        """start server and driver"""
        self.__start_server()
        self.__start_driver()
 
    def create_har(self, title, url):
        """start request and parse response"""
        self.proxy.new_har(title)
        self.driver.get(url)
        #self.driver.refresh()
        #self.driver.set_script_timeout(15)
        #result = json.dumps(self.proxy.har, ensure_ascii=False)
        result = self.proxy.har
        print "har file contents: %s"%result
        self.__store_into_file(title, result)
 
    def stop_all(self):
        """stop server and driver"""
        self.server.stop()
        self.driver.quit()
 
 
if __name__ == '__main__':
    path = 'C:\\Dev\\browsermob-proxy-2.1.2\\bin\\browsermob-proxy'
    print 'Now creating path...'
    RUN = CreateHar(path)
    print 'Now starting proxy server...'
    RUN.start_all()
    print 'Now loading Standard Article...'
    RUN.create_har('standardArticle', 'http://sfc-stage01.healthline.com/health/gerd/statistics')
    print 'Now loading Tab Article...'
    RUN.create_har('tabArticle', 'http://sfc-stage01.healthline.com/health/allergic-rhinitis')
    print 'Now tearing down...'
    RUN.stop_all()