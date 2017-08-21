——————————————————————————————————————-
Usage
——————————————————————————————————————-
Rest Service to detect perfect cat image in a given image text file. 

+             +
+++         +++
 +++++++++++++
 ++         ++
++  +     +  ++
++ +++   +++ ++
++           ++
 ++   +++   ++
 ++         ++
  ++ +   + ++
  ++  +++  ++
   ++     ++
     +++++

Request: Post
Parameters: 
{
  file : (required, text)  Max: 128KB 
  threshold: (optional, int) Confidence value 0 (weak) - 100 (strong)
}

If threshold value is not specified, defaults to 75

upon success it will return an array of objects with the coordinates where the image match originates and the confidence value. 
Returns points starting with the strongest match to the threshold specified. 

example request:
curl -X POST http://catmatch-env.us-west-2.elasticbeanstalk.com/imageDetect/v1/cat -F “file=@perfect_cat_image.txt" -F “threshold=35"

examples response:
[  
   {  
      "position":{  
         "x":0,
         "y":0
      },
      "confidenceValue":100
   }
]

——————————————————————————————————————-
How To Run:
——————————————————————————————————————-
Java 8 & Tomcat 8 or 9
Deployment for OSX and Linux :
1. install Tomcat 8 or 9 link to download http://tomcat.apache.org/download-90.cgi and put in tomcat directory
2. copy war file located in target directory into pathToTomcatDirectory/webapp directory
   cp target/Project-1.0-SNAPSHOT.war.original pathToTomcatDirectory/webapp
3. cd pathToTomcatDirectory/bin
4. place perfect_cat_image.txt in pathToTomcatDirectory/bin
   cp src/test/cat_image_files/perfect_cat_image.txt pathToTomcatDirectory/bin
4, Make all sh files executable 
   chmod +x *.sh 
5. Run
   sh startup.sh
   
_______________________________________
Unit & Integration Test 
_______________________________________
All tests and files needed are under the test directory

——————————————————————————————————————-
Design Decisions:
——————————————————————————————————————-
Spring Boot Application with TomCat Server

Empty Lines are ignored, any test after empty lines will not be read. 
Assumes they are not mean to be part of the data frame.

text with a shorter length than the great length line will be appended with white space
to match the greatest length to make a perfect MxN grid 

Only return coordinates that don’t have overlapping image. So if there’s many matches at say, 50% and they lie within the same general area, 
ignore them because the image space is already spoken for. This decision was made to prevent an influx of coordinates, 
and assumes you want the best possible match of the given coordinates

Currently doesn't assess image rotations but can be implemented: 
90 degrees, 180 degree, 270 degree. Methods to translate image already provided in ImageArray class

