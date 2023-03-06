# Money Transfer Challenge

## Overview
- Simple REST api application to support concurrent money transfer requests
- Application server listens on localhost:8080
- Uses In-memory synchronized datastore with minimalistic locking
- Ability to Create, get account and transfer money between accounts
- Validations
	- Duplicate Account
	- Account Doesn't Exist
	- Insufficient Funds for transfer
	- Illegal transfer amount (< 0)
- Libraries
	- swagger
	- Junit
	- lombok
	

## RUN

Using Java:
	- java -jar target/payments-1.0-SNAPSHOT-jar-with-dependencies.jar

## My  thoughts regarding tasks:

- We should have an actuator endpoint enabled to check application health and get the information from running applications.
- We should also have a proper logging mechanism with correlation  ID for all the requests so we can stream out logs to some third party logging system like Splunk or Kibana.
- Using Splunk or kibana we can create some dashboard for application health status and also we can create alerts as well to notify dev teams in case of any issue with running applications.
- We can also create BDD test cases to test end to end application with acceptance criteria
- We can also have integration tests and acceptance tests.
- We can also set up CICD pipeline jobs (build, deploy and release) to manage application deployments efficiently.
- We can also have docker based deployment so we can deploy it easily and more stable.
