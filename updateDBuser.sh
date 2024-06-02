#!/bin/sh

if test $# -le 0
then
    echo nombre de parametres invalide
    echo "Usage : sh $0 dbUser [ dbPassword ]"
    echo "     ex 1 : sh $0 petstoreDB_User  petstoreDB_Password "
    echo "     ex 2 : sh $0 petstoreDB  "
    echo "     ex 3 : sh $0 root ''  "
    exit 2; 
fi

# dbUser=petstoreDB
dbUser=$1
dbPassword=7002n*

dt_update=$(date +"%Y%m%d_%H%M%S")
SCRIPT_NAME=$(basename $0 .sh)
LOGFILE=$SCRIPT_NAME.log

echo "debut de $SCRIPT_NAME - $dt_update" | tee -a $LOGFILE

if test $# -eq 2
then
    dbPassword=$2
fi

if [ -f docker-compose_mysql.yml ]
then
    actualDockerSqlPassword=`grep -F MYSQL_ROOT_PASSWORD docker-compose_mysql.yml | awk '{print $2}' | tr -d '\r' `
    if [ "$actualDockerSqlPassword" != "$dbPassword" ]
    then
        echo "modification de docker-compose_mysql.yml" | tee -a $LOGFILE
        cp -p docker-compose_mysql.yml docker-compose_mysql.yml_${dt_update}

	sed -i.bak "s/MYSQL_ROOT_PASSWORD: $actualDockerSqlPassword/MYSQL_ROOT_PASSWORD: $dbPassword/" docker-compose_mysql.yml
    fi
fi

ANT_BUILDS=`find . -name build.xml`
if [ -n "$ANT_BUILDS" ]
then
    echo "modification de $ANT_BUILDS" | tee -a $LOGFILE
    cp -p $ANT_BUILDS ${ANT_BUILDS}_${dt_update}
    sed -i.bak -e "s/\"db.user\" value=\".*\"/\"db.user\" value=\"$dbUser\"/;s/\"db.password\" value=\".*\"/\"db.password\" value=\"$dbPassword\"/"  $ANT_BUILDS
fi

GRADLE_BUILDS=`find . -name build.gradle`
if [ -n "$GRADLE_BUILDS" ]
then
    for fic in $GRADLE_BUILDS
    do
        grep -F dbPassword $fic >/dev/null
        if [ $? -eq 0 ]
        then
            echo "modification de $fic" | tee -a $LOGFILE
            cp -p $fic ${fic}_${dt_update}
            sed -i.bak -e "s/dbUser = '.*'/dbUser = '$dbUser'/;s/dbPassword = '.*'/dbPassword = '$dbPassword'/;"  $fic
        fi
    done
fi

GRADLE_PROPERTIES=`find . -name gradle.properties `
if [ -n "$GRADLE_PROPERTIES" ]
then
    for fic in $GRADLE_PROPERTIES
    do
        grep -F dbPassword $fic >/dev/null
        if [ $? -eq 0 ]
        then
            echo "modification de $fic" | tee -a $LOGFILE
            cp -p $fic ${fic}_${dt_update}
            sed -i.bak -e "s/dbUser=.*/dbUser=$dbUser/;s/dbPassword=.*/dbPassword=$dbPassword/;"  $fic
        fi
    done
fi

DataAccessConstants=`find . -name DataAccessConstants.java | grep -F src`
if [ -n "$DataAccessConstants" ]
then
    echo "modification de $DataAccessConstants" | tee -a $LOGFILE
    cp -p $DataAccessConstants ${DataAccessConstants}_${dt_update}
    sed -i.bak -e "s/USER_DB = \".*\"/USER_DB = \"$dbUser\"/;s/PASSWD_DB = \".*\"/PASSWD_DB = \"$dbPassword\"/" $DataAccessConstants
fi

DataSourceConfig=`find . -name DataSourceConfig.java | grep -F src`
if [ -n "$DataSourceConfig" ]
then
    echo "modification de $DataSourceConfig" | tee -a $LOGFILE
    cp -p $DataSourceConfig ${DataSourceConfig}_${dt_update}
    sed -i.bak -e "s/username(\".*\"/username(\"$dbUser\"/;s/password(\".*\"/password(\"$dbPassword\"/" $DataSourceConfig
fi

DataSourceProps=`find . -name application.properties | grep -F src`
if [ -n "$DataSourceProps" ]
then
    for fic in $DataSourceProps
    do
	grep -F datasource.password $fic >/dev/null
        if [ $? -eq 0 ]
        then	
            echo "modification de $fic" | tee -a $LOGFILE
            cp -p $fic ${fic}_${dt_update}
            sed -i.bak -e "s/datasource.username=.*/datasource.username=$dbUser/;s/datasource.password=.*/datasource.password=$dbPassword/" $fic
        fi
    done
fi

context_xml=`find . -name context.xml`
if [ -n "$context_xml" ]
then
    echo "modification de $context_xml" | tee -a $LOGFILE
    cp -p $context_xml ${context_xml}_${dt_update}
    sed -i.bak -e  "s/username=\".*\" password=\".*\"/username=\"$dbUser\" password=\"$dbPassword\"/" $context_xml
fi

persistence_xml=`find . -name persistence.xml`
if [ -n "$persistence_xml" ]
then
    echo "modification de $persistence_xml" | tee -a $LOGFILE
    cp -p $persistence_xml ${persistence_xml}_${dt_update}
    sed -i.bak -e "s/persistence.jdbc.user\" value=\".*\"/persistence.jdbc.user\" value=\"$dbUser\"/;s/persistence.jdbc.password\" value=\".*\"/persistence.jdbc.password\" value=\"$dbPassword\"/" $persistence_xml
fi

glassfish_resources_xml=`find . -name glassfish-resources.xml`
if [ -n "$glassfish_resources_xml" ]
then
    echo "modification de $glassfish_resources_xml" | tee -a $LOGFILE
    cp -p $glassfish_resources_xml ${glassfish_resources_xml}_${dt_update}
    sed -i.bak -e "s/name=\"User\" value=\".*\"/name=\"User\" value=\"$dbUser\"/;s/name=\"Password\" value=\".*\"/name=\"Password\" value=\"$dbPassword\"/" $glassfish_resources_xml
fi
