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
                 withMaven(globalMavenSettingsConfig: '006ddf1b-dc24-4057-87cb-6b58230a9f21', mavenSettingsConfig: '9ea7bb72-cb40-47e4-8af0-e7cb98aeb62c') {
                     sh 'mvn -B clean package -Dmaven.test.skip=true'
                 }
             }
         }
         stage('Test') {
                     steps {
                        withMaven(globalMavenSettingsConfig: '006ddf1b-dc24-4057-87cb-6b58230a9f21', mavenSettingsConfig: '9ea7bb72-cb40-47e4-8af0-e7cb98aeb62c') {
                         sh 'mvn test'
                         }
                     }
                 }
         stage('Publish') {
             steps {
                 configFileProvider([configFile(fileId: 'DeployMicroservice', variable: 'deploy')])
                 {
                    withMaven(globalMavenSettingsConfig: '006ddf1b-dc24-4057-87cb-6b58230a9f21', mavenSettingsConfig: '9ea7bb72-cb40-47e4-8af0-e7cb98aeb62c') {
                        sshagent(credentials: ['a65d8fb8-0920-4060-a29d-2c46c3c2f994']){
                            sh 'chmod 775 $deploy'
                            sh '$deploy'
                 }
             }
             }
             }
         }
     }
 }