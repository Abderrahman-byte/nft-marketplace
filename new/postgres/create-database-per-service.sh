#!/bin/bash

set -e
set -u

function create_user_and_database() {
	local database=$1
	local user=$2
	local password=$3
    
	echo "  Creating user and database '$database'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE USER $user WITH PASSWORD '$password';
	    CREATE DATABASE $database;
	    REVOKE ALL PRIVILEGES ON DATABASE $database FROM public;
	    GRANT ALL PRIVILEGES ON DATABASE $database TO $user;
EOSQL
}

for service in auth_service; do
    create_user_and_database ${service}_db ${service}_user ${POSTGRES_PASSWORD}
done