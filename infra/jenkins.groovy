pipeline {
    agent any
    stages {
        // stage('Pull') { 
        //     // steps {
        //     //     git credentialsId: 'github', url: 'git@github.com:atulyw/idfsbank.git'
        //     // }
        // }
        stage('QA') { 
            steps {
                echo "$BUILD_NUMBER Pass for QA"
            }
        }
        stage('Build') { 
            steps {
               sh '''
               mvn clean package
               tar -cvf $JOB_BASE_NAME-$BUILD_ID.tar **/**.war
               ls -a
               ''' 
            }
        }
        stage('push-dev') {
         when {
            branch 'develop'
         }    
            steps {
                echo 'pushing artifact to s3'
            }
        }
        stage('deploy-dev') { 
         when {
            branch 'develop'
         }  
            steps {
                echo 'Deploying artifact to s3'
            }
        }
        stage('push-test') {
         when {
            branch 'release/*'
         }   
            steps {
                echo 'pushing artifact to s3'
            }
        }
        stage('deploy-test') {
         when {
            branch 'release/*'
         }             
            steps {
                echo 'Deploying artifact to s3'
            }
        }
        stage('push-uat') { 
         when {
            branch 'release/*'
         }            
            steps {
                echo 'pushing artifact to s3'
            }
        }
        stage('deploy-uat') {
         when {
            branch 'release/*'
         }             
            steps {
                echo 'Deploying artifact to s3'
            }
        }
        stage('push-prod') {
         when {
            branch 'main'
         }             
            steps {
                echo 'pushing artifact to s3 prod'
            }
        }
        stage('deploy-prod') {
         when {
            branch 'main'
         }            
            steps {
                echo 'Deploying artifact to s3'
            }
        }
    }
}
