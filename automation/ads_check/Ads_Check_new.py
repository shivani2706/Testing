from selenium import webdriver
import time
# Create a new instance of the Firefox driver
driver = webdriver.Firefox()

passing_flag=0


def check_page(driver, keyword):
#find all iframes on the page
    iframes = driver.find_elements_by_tag_name("iframe")
    frames = []
    failing_counter = 0 

    for i in iframes:
        attrs = driver.execute_script('var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;', i)
        #print attrs

        if attrs.has_key(u'title'):
            title = attrs[u'title']
            if title == '3rd party ad content':
                frames.append(i)
              #  print attrs
#    print "Found", len(frames), "ad frames"
    ad_number = 0
    for a in frames:
        driver.switch_to.frame(a)
        iframes = driver.find_elements_by_tag_name("iframe")

        adframe=None
        src=""
        for iframe in iframes:
            attrs = driver.execute_script('var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;', iframe)
            #print attrs
            if attrs.has_key(u'src') and attrs.has_key(u'width') and attrs.has_key(u'height'):
                src = attrs[u'src']
                width = int(attrs[u'width'])
                height = int(attrs[u'height'])
                if width > 0 and height > 0:
                 #   print attrs
                    adframe=iframe
                    ad_number = ad_number + 1

        if(adframe!=None):
#            print "Checking ad", src
            #print "Checking ad", width, "x", height 
            driver.switch_to.frame(adframe)

            #attrs = driver.execute_script('var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;', iframes[0])
            #print attrs

            page_source = driver.page_source
            if page_source.find(keyword) > 0:
                print "Checking ad", width, "x", height, ": ",  "Found keyword", keyword, "passed"
            else:
                print "Checking ad", width, "x", height, ": ","NOT found keyword", keyword, "failed"
                # passing_flag = passing_flag + 1
                failing_counter = failing_counter +1
        driver.switch_to.default_content()
    print "FOUND", ad_number, "adds on the page"
#files = [ 'bydu.txt', 'dexcom.txt' ]
files = [ 'dexcom.txt' ]
#keywords = [ "BYDUREON", "Dexcom" ]
keywords = [ "Dexcom" ]

for file in files:
    f = open(file)
    pages = []
    for line in f:
        line = line.strip()
        if line != "":
            pages.append(line)

    keyword = keywords[files.index(file)]
    for page in pages:
        failing_counter = 0 
        # go to page
        print "checking page", page
        driver.get(page)
        time.sleep(30)
        check_page(driver, keyword)
        if failing_counter !=0:
            print "Test Failed in ", keyword, " :", failing_counter, " times"
driver.quit()
#if passing_flag == 0:
 #   print "TEST PASSED SUCCESSFULLY"
#else:
 #   print "TEST FAILED ", passing_flag, " Times"