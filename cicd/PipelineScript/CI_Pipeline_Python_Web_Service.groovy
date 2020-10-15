library 'common'

node {
    
    try{

        stage('Checkout') {
    
            common.checkoutrepo('python_web_service')
    
        }
    
        stage('Sonar Scan'){
    
            common.sonarscan()
    
        }
    
        stage('Build') {
    
            common.BuildDockerImage('python_web_service','python_web_service')
    
        }
        stage('TriggerCD'){

           build job: "CD/CD_Pipeline", wait: false, propagate: false
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