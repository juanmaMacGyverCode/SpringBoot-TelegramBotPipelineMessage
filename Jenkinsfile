#!groovy
import groovy.json.JsonSlurper
def holaMundo = "HOLAAAAA"

def getCardById() {

        URL apiUrl = new URL("https://api.github.com/repos/juanmaMacGyverCode/SpringBoot-TelegramBotPipelineMessage/commits")
        def card = new JsonSlurper().parseText(apiUrl.getText())
        return card
}


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
                        def url = "https://api.github.com/repos/juanmaMacGyverCode/SpringBoot-TelegramBotPipelineMessage/commits"
                        //def githubApiCurl = sh(script: "curl -s ${url}", returnStdout: false).trim()
                        //def apiUrl = new URL(url)
                        //def githubApiCurl = new JsonSlurper().parseText(apiUrl.text)
                        //def longitud = githubApiCurl.getClass()
                        //def apiObject = new GetCardService()
                        //def jsonApiGitHub = getCardById()

                        sh "curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id=${ID} -d parse_mode='HTML' -d text='<b>Project</b> : POC \
                        <b>Branch</b>: ${BRANCH_NAME} \
                        <b>Autor</b>: ${GITHUB_COMMENT_AUTHOR} \
                        <b>Build </b> : OK \
                        <b>Test suite</b> = Passed \
                        <b>Un saludete</b> = ${holaMundo} \
                        <b>Visitando toda la api</b> = Por ahora un hola'"

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
                <b>Test suite</b> = FAILURE \
                <b>Un saludete</b> = ${holaMundo}'"

                    //final String url = "http://localhost:8080/job/Demos/job/maven-pipeline-demo/job/sdkman/2/api/json"
                    //final String response = sh(script: "curl -s $url", returnStdout: true).trim()
            }
        }
    }
}
