def holaMundo = "HOLAAAAA"

pipeline {
    agent any
    triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage("Compile") {
            steps {
                echo "El nombre de la rama es ${BRANCH_NAME}"
                sh "./gradlew compileJava"
            }
        }
        stage("Unit test") {
            steps {
                sh "./gradlew test"
            }
        }
        stage("Push Notification") {
            /*when {
                expression {
                    GIT_BRANCH = 'origin/' + sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                    return GIT_BRANCH == 'origin/master' || params.FORCE_FULL_BUILD
                    }
                }*/
            steps {

                script{
                    withCredentials([string(credentialsId: 'HTTP_TOKEN', variable: 'TOKEN'),
                                    string(credentialsId: 'CHAT_ID', variable: 'ID')]) {
                        sh "curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id=${ID} -d parse_mode='HTML' -d text='<b>Project</b> : POC \
                        <b>Branch</b>: ${BRANCH_NAME} \
                        <b>Build </b> : OK \
                        <b>Test suite</b> = Passed '"
                        //final String url = "http://localhost:8080/job/Demos/job/maven-pipeline-demo/job/sdkman/2/api/json"
                        //final String response = sh(script: "curl -s $url", returnStdout: true).trim()
                    }
                }
            }
        }
    }
    post {
        failure {
            withCredentials([string(credentialsId: 'HTTP_TOKEN', variable: 'TOKEN'),
                             string(credentialsId: 'CHAT_ID', variable: 'ID')]) {
                //def githubApiCurl = "curl https://api.github.com/repos/juanmaMacGyverCode/SpringBoot-TelegramBotPipelineMessage/commits"
                sh "curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id=${ID} -d parse_mode='HTML' -d text='<b>Project</b> : POC \
                <b>Branch</b>: ${BRANCH_NAME} \
                <b>Build </b>: OK \
                <b>Test suite</b> = FAILURE '"

                    //final String url = "http://localhost:8080/job/Demos/job/maven-pipeline-demo/job/sdkman/2/api/json"
                    //final String response = sh(script: "curl -s $url", returnStdout: true).trim()
            }
        }
    }
}
