#Remove all Containers running
#-----------------------------
cd ..
cd ..
Write-Host "Removing all Docker Containers in the local Machine"
docker rm -f $(docker ps -a -q)

#Remove all Conatiner images
Write-Host "------------------------------------------" -ForegroundColor green
Write-Host "Removing all Docker Images in the local Machine" -ForegroundColor Yellow
docker rmi -f $(docker images -a -q) 

#Create Docker Image for python_web_service
Write-Host "------------------------------------------" -ForegroundColor green
Write-Host "Creating Docker Image for python_web_service" -ForegroundColor Yellow
cd python_web_service
docker build --tag sunishsurendrank/python_web_service:v1 .
cd ..
#Create Docker Image for go_web_service
Write-Host "------------------------------------------" -ForegroundColor green
Write-Host "Creating Docker Image for go_web_service" -ForegroundColor Yellow
cd go_web_service
docker build --tag sunishsurendrank/go_web_service:v1 .
cd ..
#Create Docker Image for frontend
Write-Host "------------------------------------------" -ForegroundColor green
Write-Host "Creating Docker Image for frontend" -ForegroundColor Yellow
cd frontend
docker build --tag sunishsurendrank/frontend:v1 .
cd ..
cd cicd/ManualDeployment 
#Create a custom network called demo
Write-Host "------------------------------------------" -ForegroundColor green
Write-Host "Creating Docker network called demo" -ForegroundColor Yellow
docker network create demo

#Run python_web_service container
Write-Host "------------------------------------------" -ForegroundColor green
Write-Host "Running Docker Conatiner python_web_service" -ForegroundColor Yellow
docker run -d -p "5000:5000" --network demo --name python_web_webservice -i sunishsurendrank/python_web_service:v1

#Run go_web_service container
Write-Host "------------------------------------------" -ForegroundColor green
Write-Host "Running Docker Conatiner go_web_service" -ForegroundColor Yellow
docker run -d -p "9090:9090" --network demo --name go_web_webservice -i sunishsurendrank/go_web_service:v1

#Run frontend container
Write-Host "------------------------------------------" -ForegroundColor green
Write-Host "Running Docker Conatiner frontend" -ForegroundColor Yellow
docker run -d -p "8080:80" --network demo --name frontend -i sunishsurendrank/frontend:v1

Write-Host "Your application is ready to use , Please access http://127.0.0.1:8080/" -ForegroundColor green