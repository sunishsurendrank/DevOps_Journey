library 'common'

node {
    
    try{

        stage('Checkout') {
    
            common.checkoutrepo('Frontend')
    
        }
    
        stage('Sonar Scan'){
    
            common.sonarscan()
    
        }
    
        stage('Build') {
    
            common.BuildDockerImage('Frontend','frontend')
    
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