Install instructions:
1. Get the war downloading it or building it from the source. 
2. Copy the workast.war to the webapps Tomcat directory
2. Copy "tools/tomcat/endorsed/*.*" dir to "%TOMCAT_HOME%/endorsed"
3. Start the Tomcat server
4. Go to http://localhost[:port]/[context]
5. Create a user and go!

Switch to MySQL
   Modify /resources/properties/db.properties data or create a config file (ie.: "/users/username/workast.properties") and 
   add the config file to the Tomcat initialization -DconfigFile="file:///users/username/workast.properties"

ie.:
   # MySQL
   db.brand=mysql
   db.username=root
   db.password=
   db.url=jdbc:mysql://localhost/${db.schema}?useUnicode=true&characterEncoding=UTF-8

How to build from the source:
1. Run the "war" ANT target
2. Search the war in the "build" folder.

   