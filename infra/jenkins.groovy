pipeline {
    agent any
    stages {
        stage("QA"){
            parallel {
                stage("QA-1"){
                    agent any
                    steps {
                        echo "QA 1 PASSED"
                    }
                }

                stage("QA-2"){
                    agent any
                    steps {
                        echo "QA 2 PASSED"
                    }
                }
            }
        }
        stage("Sonar-Scan") {
            steps {
                withCredentials([string(credentialsId: 'idfs', variable: 'SONAR_TOKEN')]) {
                sh '''
                #export PATH=$PATH:/opt/sonar-scanner/bin
                #sonar-scanner -Dsonar.login=$SONAR_TOKEN -Dsonar.projectKey=idfsbank -Dsonar.organization=atulyw
                echo 'sonar-sucessfull'
                '''
             }
          }  
        }  
        stage("Build"){
            steps {
                sh '''
                mvn clean package
                tar -cvf $JOB_BASE_NAME-$BUILD_ID.tar **/**.war
                zip -u latest.zip **/*.war appspec.yml ./scripts/** code-deploy.sh
                '''    
            }
        }

        stage("push-artifact"){
            when {
                branch "develop"
            }

            steps {
                sh 'aws s3 cp latest-$BUILD_ID.zip s3://idfs-bank-artifacts/'
            }
        }

        stage("deploy-dev"){
            when {
                branch "develop"
            }

            steps {
               sh '''
                aws deploy create-deployment \
                --application-name idfsbank \
                --deployment-config-name CodeDeployDefault.AllAtOnce \
                --deployment-group-name idfsbank-dg \
                --s3-location bucket=idfs-bank-artifacts,bundleType=zip,key=latest-$BUILD_ID.zip > out
                DEP_ID=`cat out | grep deploymentId | awk '{print $2}' | tr -d '"'`
                aws deploy wait deployment-successful --deployment-id $DEP_ID               
               '''
            }
        }

        stage("push-test"){
            when {
                branch "release/*"
            }

            steps {
                echo "pushing artifact to s3"
            }
        }

        stage("deploy-test"){
            when {
                branch "release/*"
            }

            steps {
                echo "Deploying artifact to s3"
            }
        }

        stage("push-uat"){
            when {
                branch "release/*"
            }

            steps {
                echo "pushing artifact to s3"
            }
        }

        stage("deploy-uat"){
            when {
                branch "release/*"
            }

            steps {
                echo "Deploying artifact to s3"
            }
        }

        stage("push-prod"){
            when {
                branch "main"
            }

            steps {
                echo 'pushing artifact to s3 prod'
            }
        }

        stage("deploy-prod"){
            when {
                branch "main"
            }

            steps {
                echo "Deploying artifact to s3"
            }
        }
    }
}