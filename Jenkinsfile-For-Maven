pipeline {
    /* insert Declarative Pipeline here */
    agent any
    def mvnHome

    stages {
        stage('Preparation') { // for display purposes
            // Get some code from a GitHub repository
            git 'https://github.com/wayne07/JTubeDownloaderFx.git'
            // Get the Maven tool.
            // ** NOTE: This 'M3' Maven tool must be configured in the global configuration.
            mvnHome = tool 'M3'
        }
        stage('Build') {
            steps {
                echo 'Building..'
                withMaven(maven : 'M3') {
                    sh 'mvn clean compile'
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                withMaven(maven : 'M3') {
                    sh 'mvn clean test'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}