from browsermobproxy import Server
server = Server("C:\\Dev\\browsermob-proxy-2.1.2\\bin\\browsermob-proxy")
server.start()
proxy = server.create_proxy()             

from selenium import webdriver
profile  = webdriver.FirefoxProfile()
profile.set_proxy(proxy.selenium_proxy())
driver = webdriver.Firefox(firefox_profile=profile)

def get_har(article, article_url):
    result = ''
    proxy.new_har(article, options={'captureHeaders':True})
    driver.get(article_url)
    result = proxy.har # returns a HAR JSON blob
    print "har file contents: %s"%proxy.har
    return result




if __name__ == '__main__':
    print "Test 01: Standard Article"
    get_har('Standard', 'http://sfc-stage02.healthline.com/health/gerd/statistics')
    print "Test 02: Tab Article"
    get_har('Tab_Article', 'http://sfc-stage01.healthline.com/health/allergic-rhinitis')
    proxy.stop()
    driver.quit()