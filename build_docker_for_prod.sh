echo "\n***** BUILDING PROD JAR *****\n"
mvn clean install -Pprod

echo ""
echo "\n***** BUILDING AND PUSHING DOCKER IMAGE TO STORAGE *****\n"
echo ""
mvn docker:build -DpushImageTag -Pprod
