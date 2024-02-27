pipeline {
    agent any

    stages {
       stage('CLONE')
        {
        steps{
           git branch : 'prod', credentialsId : 'git-token' ,url : 'https://github.com/HyndaiFinalProject/hclub-auth.git'
            sh '''
            cd /var/jenkins_home/workspace/hclub-auth 
            '''
        }
           
        }
        stage('PUT application.yml'){
            steps{
                sh '''
                cd /var/jenkins_home/workspace/hclub-auth
                echo 'current dir ' ${PWD}
                cp /var/jenkins_home/config/application.yml /var/jenkins_home/workspace/hclub-auth/src/main/resources
                '''
                
            }
        }
        
        stage('Docker Build')
        {
        steps{
           
            sh '''
           echo 'Build dir' ${PWD}
           cd /var/jenkins_home/workspace/hclub-auth
           docker stop hclub-auth || true
           docker rm hclub-auth || true
           
           docker rmi popopododo/hclub-auth || true
           
           docker build -t popopododo/hclub-auth .
            '''
        }
           
        }
        
        stage('Docker Deploy')
        {
        steps{
           
            sh '''
           docker run --name hclub-auth -d -p 8080:8080 popopododo/hclub-auth
            '''
        }
           
        }
    }
   
}