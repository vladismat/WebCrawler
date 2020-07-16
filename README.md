# WebCrawler

This is a console application that searches predefined terms on different websites starting from the predefined seed URL and following all found links there.<br>
The program stops execution when the number of pages exceeds the limit (1000 by default) or when there is no more pages to visit.<br>
The maximum depth of the search is 8 pages deep from the seed page.<br>
The program takes seed URL and terms from the "input.txt" file in the root directory that has the following format:<br>
"Seed url + line break <br>
term1 + line break <br>
term2 + line break <br>
... "

The terms can contain spaces.

The search is case-sensitive. 

To run the project you preferably need to have installed Maven (or download <a href="http://maven.apache.org/download.cgi">here</a>).
To compile the project execute command: "mvn compile" in the command line of the project directory (where the pom.xml is).
To run the project execute the command: <br>mvn exec:java -Dexec.mainClass="com.matsoft.Main" <br>
Make sure that there is a file "input.txt" in the root directory of the project. Feel free to change it and see the result.

Thank you for reading me and good luck.
