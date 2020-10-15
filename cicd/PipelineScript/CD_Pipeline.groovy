library 'common'

node {
    
    try{
    
        stage('Checkout') {
        
            common.checkoutrepo('CICD')
        
        }
    
        stage('Update Helm'){
            
         def GoDockerImageTag = common.getLastSuccessfulBuildNumber("CI/CI_Pipeline_GO_Web_Service")
         def PythonDockerImageTag = common.getLastSuccessfulBuildNumber("CI/CI_Pipeline_Python_Web_Service")
         def FrontendDockerImageTag = common.getLastSuccessfulBuildNumber("CI/CI_Pipeline_Frontend")
         
         print(GoDockerImageTag)
         print(PythonDockerImageTag)
         print(FrontendDockerImageTag)
         
         
         
         sh """
         cd CICD/helmcharts/productdeployment
         
         cat values.yaml
         sed -i  's/pythonwebserviceTagValue/v$PythonDockerImageTag/g' values.yaml
         sed -i  's/gowebserviceTagValue/v$GoDockerImageTag/g' values.yaml
         sed -i  's/frontendTagValue/v$FrontendDockerImageTag/g' values.yaml
         cat values.yaml
         
         """
            
        }
        stage('Deploy')
        {
          sh '''
          
            cd CICD/helmcharts/productdeployment
          
            STR=`helm list| grep product`
            
            if [ "$STR" != "" ]; then
              echo "Deployment exist,Hence upgrading product"
              versionkey=`cat Chart.yaml| grep version:`
              echo $versionkey
              versionvalue=`echo $versionkey|awk '{print $2}'`
              echo $versionvalue
              newversionvalue=$((versionvalue+1))
              echo $newversionvalue
              sed -i "s/$versionkey/version: $newversionvalue/g" Chart.yaml
              cat Chart.yaml
              helm upgrade product .
              git add Chart.yaml
              git commit -m "Updating the Chart Version from CD pipeline"
              git config --global user.name "sunisk"
              git push http://test:Login123@10.157.49.43:8080/community/cicd.git
            else
              echo "Deployment not exist,Hence installing product"
              helm install product .
            fi
            
            helm list
          
          '''
            
        }
        
       }
    catch (Exception e) {
        
            throw e
    }

    finally {
            println("in Finally block")
            cleanWs()
            dir("${env.WORKSPACE}@tmp") { deleteDir() }
        
    }

}