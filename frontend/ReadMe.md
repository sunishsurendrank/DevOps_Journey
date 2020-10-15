# How to Deploy the Application

This documentation willl help user to Install the application in windows/linux machine where docker is running.

- Author of this Document : Sunish Surendran Kannembath
- Developer : Sunish Surendran Kannembath
- Reach Sunish in LinkedIn : https://www.linkedin.com/in/sunishsurendrank/
- Reach Sunish in Twitter : @sunishsurendran

## Step 1 :
Open Chrome and  paste the below link and install the extension

```
https://chrome.google.com/webstore/detail/moesif-orign-cors-changer/digfbfaphojjndkpccljibejjbppifbc
```

## Step 2 :
Create a custom network called demo
```
docker network create demo
```
## Step 3 :
Download the webservice from dockerhub repo
```
docker run -d -p "9090:9090" --network demo --name webservice sunishsurendrank/webservice:v1
```
## Step 4 :
Download the webserver from dockerhub repo
```
docker run -d -p "8080:80" --network demo --name webserver sunishsurendrank/webserver:v1
```
## Step 5 :
Open chrome ON the Moesif CORS plugin and run the below command
```
http://localhost:8080/
```

