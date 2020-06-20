#!/usr/bin/env bash

#### Predefined functions

check_service_info() {
  if [[ ! -f ./service_info ]]; then
    return
  fi
}


#### main procedure

APP_NAME={{appname}}
URL_PREFIX={{urlPrefix}}
INSTANCE_ID=${APP_NAME}

echo "Your app name is ${APP_NAME}."
echo "If you're running multiple instances of this app, then\
 you need to provide a UNIQUE instance id for each of them."
printf "Instance ID ('${APP_NAME}'):"
read INSTANCE_ID

if [[ -n ${INSTANCE_ID} ]]; then
  INSTANCE_ID=${APP_NAME}
fi

echo "downloaing launcher.jar ..."
curl -s ${URL_PREFIX}/launcher.jar -o launcher.jar

echo "Launching app \'${APP_NAME}\'..."
java -jar launcher.jar ${URL_PREFIX}/apps/${APP_NAME} {{args}}