pipeline {
    agent {
        docker {
            image 'maven:3'
             args '-v /root/.m2:/root/.m2'
         }
     }
     stages {
         stage('Build') {
             steps {
                     sh 'mvn -B clean package -Dmaven.test.skip=true'
             }
         }
         stage('Test') {
                     steps {
                         sh 'mvn test'
                     }
                 }
         stage('Publish') {
             steps {
                 configFileProvider([configFile(fileId: 'DeployMicroservice', variable: 'deploy')])
                 {
                        sshagent(credentials: ['a65d8fb8-0920-4060-a29d-2c46c3c2f994']){
                            sh 'chmod 775 $deploy'
                            sh '$deploy'
                 }

             }
             }
         }
     }
 }