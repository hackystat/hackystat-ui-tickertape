echo -n -e "\033]0;Tickertape $HACKYSTAT_VERSION\007"
java -Xmx128M -jar $HACKYSTAT_SERVICE_DIST/hackystat-ui-tickertape/tickertape.jar
