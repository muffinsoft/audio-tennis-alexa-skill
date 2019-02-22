# audio-tennis-alexa-skill
Skill game for Amazon Alexa

## Documentation

## Build
#### with tests
mvn clean assembly:assembly -DdescriptorId=jar-with-dependencies package
#### without tests
mvn clean assembly:assembly -DdescriptorId=jar-with-dependencies -DskipTests package

#### 
Time to time needs `export MAVEN_OPTS=-Xss32m`