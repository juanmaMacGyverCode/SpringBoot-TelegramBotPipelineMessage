pipeline {
    agent any
    triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage("Compile") {
            steps {
                sh "./gradlew compileJava"
            }
        }
        stage("Unit test") {
            steps {
                sh "./gradlew test"
            }
        }
        stage("Push Notification") {
            steps {
                script{
                    withCredentials([string(credentialsId: 'HTTP_TOKEN', variable: 'TOKEN'),
                                    string(credentialsId: 'CHAT_ID', variable: 'ID')]) {
                        sh "
                        curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id=${ID} -d parse_mode='HTML' -d text='<b>Project</b> : POC \
                        <b>Branch</b>: master \
                        <b>Build </b> : OK \
                        <b>Test suite</b> = Passed'
                        "
                    }
                }
            }
        }
    }
}
