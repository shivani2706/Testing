from selenium import webdriver

# Create a new instance of the Firefox driver
driver = webdriver.Firefox()

def check_page(driver, keyword):
    iframes = driver.find_elements_by_tag_name("iframe")
    frames = []

    for i in iframes:
        attrs = driver.execute_script('var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;', i)
        #print attrs

        if attrs.has_key(u'title'):
            title = attrs[u'title']
            if title == u'3rd party ad content':
                frames.append(i)
                print attrs
    print "found", len(frames), "ad frames"

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
                    print attrs
                    adframe=iframe

        if(adframe!=None):
            print "checking ad", src
            driver.switch_to.frame(adframe)

            #attrs = driver.execute_script('var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;', iframes[0])
            #print attrs

            page_source = driver.page_source
            if page_source.find(keyword) > 0:
                print "Found keyword", keyword, "passed"
            else:
                print "NOT found keyword", keyword, "failed"
        driver.switch_to.default_content()

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
        # go to page
        print "checking page", page
        driver.get(page)
        check_page(driver, keyword)
