# Money Transfer Challenge

## Overview
- Simple REST api application to support concurrent money transfer requests
- Application server listens on localhost:8080
- Uses In-memmory synchronized datastore with minimalistic locking
- Ability to Create, get account and transfer money between accounts
- Validations
	- Duplicate Acount
	- Account Doesn't Exist
	- Insufficient Funds for transfer
	- Illegal transfer amount (< 0)
- Libraries
	- Jetty server
	- Jersey
	- swagger code-gen
	- Jackson
	- Junit5
	- lombok
	- Jersey Test Framework

## RUN

Using Java:
	- java -jar target/payments-1.0-SNAPSHOT-jar-with-dependencies.jar

