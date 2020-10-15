def checkoutrepo(reponame)
{
    sh """

    git clone http://10.157.49.43:8080/community/${reponame}.git -b master

    """
}

def sonarscan()
{
    println("Scanning")

}

def BuildDockerImage(reponame,imagename)
{
    sh """
    
    cd ${reponame}
    
    ls -l

    docker build  --tag "sunishsurendrank/$imagename:v$BUILD_NUMBER" .
    
    docker push "sunishsurendrank/$imagename:v$BUILD_NUMBER"

    export GoDockerBuildTag="$BUILD_NUMBER"

    """

}

def getLastSuccessfulBuildNumber(String jobPath){
	/*
	params:
		jobPath: type String :: path of job including folder name.
						 Ex:  DAaaS-k8s-CI-mergereq2master/CI-INTEGRATION_TEST
	*/
	
	def lastSuccessfulBuildNumber = ""
	try{
		if (jobPath.contains('/')){
			def job = Jenkins.getInstance().getItemByFullName(jobPath, Job.class)
			lastSuccessfulBuildNumber = job.lastSuccessfulBuild.displayName
		}else{
			lastSuccessfulBuildNumber = Jenkins.instance.getItem(jobPath).lastSuccessfulBuild.displayName
		}
		
	}catch(Exception){
		println(String.format("job: %s is invalid", jobPath))
		println("exception: "+Exception)
	}
	println(String.format("Last Successful Build number of job: %s  is %s", jobPath, lastSuccessfulBuildNumber))
	return lastSuccessfulBuildNumber.replace("#", "")
}