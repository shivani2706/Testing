require 'rubygems'
#require 'watir-webdriver'
require 'watir'
require 'pry'
#require 'Excel'
require 'win32ole'

 
  file = "C:\\Users\\rgupta\\Desktop\\CMS_automation\\cmsarticles.xlsx"
  
  excel = WIN32OLE.new('Excel.Application')
  excel.visible = true
  #workbook = excel.Workbooks.Add()
  
	wb=excel.Workbooks.Open(file)
	ws=wb.Worksheets(1)
	data=ws.UsedRange.Rows.Value
	
	vartext0="Judith Marcin, MD"
	vartext1="Brenda B. Spriggs, MD, FACP"
	vartext2="Deborah Weatherspoon, PhD, RN, CRNA, COI"
	vartext3="Deborah Weatherspoon, PhD, RN, CRNA, COI"
	vartext4="George Krucik, MD, MBA"
	vartext5="Judith Marcin, MD"
	vartext6="Mark R Laflamme, MD"
	vartext7="Mark R Laflamme, MD"
	vartext8="Timothy J. Legg, PhD, CRNP"
	vartext9="Timothy J. Legg, PhD, CRNP"
	
	vartextarr=[vartext0,vartext1,vartext2,vartext3,vartext4,vartext5,vartext6,vartext7,vartext8,vartext9]
	
	@file_name = "RepublishReport-#{Time.now.strftime('%Y%m%d-%H%M%S')}.txt"
	aFile = File.open(@file_name,"w") 
	aFile.write "Current time = #{Time.now()}"
	aFile.write "\n-----------------------------------\n"
    aFile.write "-----------------------------------\n"

 
 browser=Watir::Browser.new :chrome
 
 #for stage
 browser.goto("https://vmj-hlcms01-stage.healthline.com") 

 #for prod
 #browser.goto("http://vmj-hlcms01-prod")
 
 
#logging to browser
 browser.text_field(:id, "edit-name").set("QA")
 browser.text_field(:id, "edit-pass").set("letmein")
 browser.button(:id, "edit-submit").click
 # sleep(2)

  #binding.pry
    #hash = Hash[data]

	j=0
	for i in 1..data.count
	

	@article=ws.Range("A#{i}").text

	
	#browser.goto("http://vmj-hlcms01-prod/node/#{@nodeID}/edit")
	browser.goto("https://vmj-hlcms01-stage.healthline.com/#{@article}")
	sleep(2)
	if browser.text.include?("This document is locked")
		browser.link(:text => "here").click
		sleep(3)
	end

	
	#Clicking the EDIT link in cms
	browser.link(:text ,"Edit").click	
	sleep(2)
	
	browser.text_field(:id, "edit-field-medical-byline-0-value").clear   
	
	#for j in 0..9
	
		browser.text_field(:id, "edit-field-medical-byline-0-value").set(vartextarr[j]) 
	#end
		
			
		#Setting the Force Republish Checkbox to true
	browser.checkbox(:id, "edit-field-forcerepublish-value").set(true)
	
	#Click Save button
	browser.button(:id, "edit-submit").click
	j=j+1
	 sleep(2)
	 
	 aFile.write  "\n Article republished row: " +i.to_s
	 	
	end
	aFile.write"\n\n------Runs:#{i} completed-----------------"
	
	wb.close
	excel.quit
	aFile.close
	browser.link(:text, "Log out").click
	browser.close
  