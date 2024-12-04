echo "\n***** BUILDING TEST JAR *****\n"
mvn clean install -Ptest

echo ""
echo "\n***** BUILDING AND PUSHING DOCKER IMAGE TO 10.42.78.138 STORAGE *****\n"
echo ""
mvn docker:build -DpushImageTag -Ptest
