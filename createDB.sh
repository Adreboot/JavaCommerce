#!/bin/bash
dbName=petstoreDB
dbUser=root
dbPassword=''

if [ $# -ge 1 ]
then
    dbUser=$1
fi
if [ $# -ge 2 ]
then
    dbPassword=$2
fi

containerId=

# check if docker is in the path
docker --version >/dev/null 2>/dev/null
if [ $? -eq 0 ]
then
    # check if docker daemon is running
    docker ps >/dev/null 2>/dev/null
    if [ $? -eq 0 ]
    then
	containerId=$(docker ps | grep mysql-petstore | awk '{print $1}' )
    else
	echo "WARNING : docker daemon is not running"
    fi
else
    echo "WARNING : docker not in the path ... trying to run mysql cli if available"
 fi

sql_command='drop database if exists '$dbName';create database '$dbName';'; 
run_sql_command="echo '$sql_command' | mysql --user=$dbUser --password=$dbPassword"

if [ -z $containerId ]
then
    echo "WARNING : mysql-petsore docker container not found : DOCKER NOT LAUNCHED OR NOT INSTALLED";
    # run sql CLI if available
    mysql --version >/dev/null 2>/dev/null
    if [ $? -eq 0 ]
    then
	echo "$run_sql_command" | sh
	if [ $? -eq 0 ]
	then
	    echo "$dbName created"	
	    exit 0
	else
	    echo "WARNING: database $dbName could not be created"
	    exit 7
	fi
    else
	echo "mysql cli not in the path : you should create the database $dbName manually'" 
	exit 0
    fi
fi

# run SQL inside docker container
docker exec $containerId sh -c "$run_sql_command ; exit \$?" &>/dev/null

if [ $? -ne 0 ]
then
    echo "WARNING: database $dbName could not be created"
else
    echo "$dbName created"	
fi
