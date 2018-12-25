#!/usr/bin/env bash
echo Hello, this is \'{{appname}}\'!

curl -s {{urlPrefix}}/launcher.jar -o launcher.jar
java -jar launcher.jar {{urlPrefix}}/apps/hello {{args}}