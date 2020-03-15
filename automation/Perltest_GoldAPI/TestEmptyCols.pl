#!/usr/bin/perl -w

use strict;
use warnings;
use Spreadsheet::ParseExcel;
use Spreadsheet::WriteExcel;
use LWP;
use JSON;

# grab the current time
my @now = localtime();

# rearrange the following to suit your stamping needs.
# it currently generates YYYYMMDDhhmmss
my $timeStamp = sprintf("%04d%02d%02d_%02d%02d", 
                        $now[5]+1900, $now[4]+1, $now[3],
                        $now[2],      $now[1],   $now[0]);

# insert stamp into constant portion of file name.
# the constant portion of the name could be included 
# in the sprintf() above.
#my $baseUrl = "vmc-apistage02.healthline.com";
#my $baseUrl = "vmc-apistage01.healthline.com";

#my $baseUrl = "sfc-api1.healthline.com";
my $baseUrl = "vmj-api01-prod.healthline.com";


#my $baseUrl = "10.1.0.113";
#=for comment
# Create a new Excel file
my $FileName1 = "Report$timeStamp.xls";
my $workbook1 = Spreadsheet::WriteExcel->new($FileName1);
# Add a worksheet
my $worksheet1 = $workbook1->add_worksheet('Gold API Test');


my $FileName2 = "DBInput.xls";

my $parser   = Spreadsheet::ParseExcel->new();
my $workbook2 = $parser->parse($FileName2);

die $parser->error(), ".\n" if ( !defined $workbook2 );

# Following block is used to Iterate through all worksheets
# in the workbook and print the worksheet content 

for my $worksheet ( $workbook2->worksheets() ) {

  # Find out the worksheet ranges
  my ( $row_min, $row_max ) = $worksheet->row_range();
  my ( $col_min, $col_max ) = $worksheet->col_range();

  #for my $row ( $row_min .. $row_max ) {
  for my $row ( 1 .. $row_max ) {
    for my $col ( $col_min .. $col_max ) {

    # Return the cell object at $row and $col
    my $cell = $worksheet->get_cell( $row, $col );
    next unless $cell;

    print "Row, Col    = ($row, $col)\n";
    print "Value       = ", $cell->value(),       "\n";
    
    my $browser = LWP::UserAgent->new;
my $url = 'http://'.$baseUrl.'/api/service/2.0/gold/content'.$cell->value().'?partnerId=31a86f67-b1b6-4fd7-8b98-8caeee8d3078';
        
    print "$url\n";
    
    #my $newRow = ($row-1)*9;
   #   $worksheet1->write($newRow,0, $url);
      
       my $newRow = ($row-1);
      $worksheet1->write($newRow+1,0, $url);

    # Issue request, with an HTTP header
    my $response = $browser->get($url,
      'User-Agent' => 'Mozilla/4.0 (compatible; MSIE 7.0)',
    );
    die 'Error getting $url' unless $response->is_success;
    #print 'Content type is ', $response->content_type;
    #print 'Content is:';
    #print $response->content;
    #print "\n";

    my $json_text = decode_json($response->content);
    
    # Change width for only first column
    $worksheet1->set_column(0,0,20);

    # Write a formatted and unformatted string, row and column
    # notation.
    #$worksheet1->write(0,0, "PERL FLAVOURS");
    #$worksheet1->write(1,0,"Active State PERL");
    #$worksheet1->write(2,0,"Strawberry PERL");
    #$worksheet1->write(3,0,"Vennila PERL");
    #$worksheet1->write(4,0,"Test");

    if(@{$json_text->{data}->{drugs}}){    
        
      	$worksheet1->write(0,1, "imuid");  
	$worksheet1->write(0,2, "route_form"); 
	$worksheet1->write(0,3, "generic_name"); 
	$worksheet1->write(0,4, "last_updated"); 
	$worksheet1->write(0,5, "cmsurl"); 
	#$worksheet1->write(0,6, "brands"); 
	#$worksheet1->write(0,7, "routes");   
    
        foreach my $episode(@{$json_text->{data}->{drugs}}){
            my $value = "";
            if($episode->{imuid}){
                $value = $episode->{imuid};    
               # $worksheet1->write($newRow+1,0, "imuid");    
                $worksheet1->write($newRow+1,1, $value);        
            }
           # print "$value\n";    
        
            if($episode->{route_form}){
                $value = $episode->{route_form};
                #$worksheet1->write($newRow+2,0, "route_form");    
                $worksheet1->write($newRow+1,2, $value);                
            }
           # print "$value\n";

            if($episode->{generic_name}){
                $value = $episode->{generic_name};
               # $worksheet1->write($newRow+3,0, "generic_name");    
                $worksheet1->write($newRow+1,3, $value);            
            }
          #  print "$value\n";                        
        
            if($episode->{last_updated}){
                $value = $episode->{last_updated};    
               # $worksheet1->write($newRow+4,0, "last_updated");    
                $worksheet1->write($newRow+1,4, $value);                
            }
             if($episode->{cmsurl}){
                                $value = $episode->{cmsurl};
                              #  $worksheet1->write($newRow+5,0, "cmsurl");
                                $worksheet1->write($newRow+1,5, $value);                   
                        }
             if($episode->{brands}){
                 $value = $episode->{brands};
                               # $worksheet1->write($newRow+6,0, "brands");
                               # $worksheet1->write($newRow+1,6, $value);                   
                        }
            if($episode->{routes}){
                                $value = $episode->{routes};
                              #  $worksheet1->write($newRow+7,0, "routes");
                               # $worksheet1->write($newRow+1,7, $value);                   
                        }
              #  print "$value\n";
        } 
    }
    }
  }
}
 
 
