#!groovy
import groovy.json.JsonSlurper
def holaMundo = "HOLAAAAA"

def getCardById() {

        URL apiUrl = new URL("https://api.github.com/repos/juanmaMacGyverCode/SpringBoot-TelegramBotPipelineMessage/commits")
        def card = new JsonSlurper().parseText(apiUrl.getText())
        return card
}

def FAILED_STAGE

pipeline {
    agent any
    triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage("Compile") {
            steps {
                script {
                    FAILED_STAGE=env.STAGE_NAME
                }
                echo "El nombre de la rama es ${BRANCH_NAME}"
                sh "./gradlew compileJava"
            }
        }
        stage("Unit test") {
            steps {
                script {
                    FAILED_STAGE=env.STAGE_NAME
                }
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
                    FAILED_STAGE=env.STAGE_NAME
                    env.GIT_COMMIT_MSG = sh (script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
                    GIT_NAME= sh (script: 'git --no-pager show -s --format=%an ${GIT_COMMIT}', returnStdout: true).trim()
                    GIT_EMAIL= sh (script: 'git --no-pager show -s --format=%ae ${GIT_COMMIT}', returnStdout: true).trim()
                    def gitTemp = env.GIT_URL
                    def urlShort = gitTemp.substring(0, gitTemp.length()-4)
                    def urlWithCodeCommit = urlShort + "/commit/" + env.GIT_COMMIT
                    withCredentials([string(credentialsId: 'HTTP_TOKEN', variable: 'TOKEN'),
                                    string(credentialsId: 'CHAT_ID', variable: 'ID')]) {
                        def url = "https://api.github.com/repos/juanmaMacGyverCode/SpringBoot-TelegramBotPipelineMessage/commits"
                        //def githubApiCurl = sh(script: "curl -s ${url}", returnStdout: false).trim()
                        //def apiUrl = new URL(url)
                        //def githubApiCurl = new JsonSlurper().parseText(apiUrl.text)
                        //def longitud = githubApiCurl.getClass()
                        //def apiObject = new GetCardService()
                        //def jsonApiGitHub = getCardById()

                        sh "curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id='-425187469' -d parse_mode='HTML' -d text='<b>Project</b> : Huella positiva <br>\
                        <b>Branch</b>: ${BRANCH_NAME} <br>\
                        <b>Autor Commit</b>: ${GIT_NAME}<br>\
                        <b>Email Commit</b>: ${GIT_EMAIL}<br>\
                        <b>Mensaje Commit</b>: ${env.GIT_COMMIT_MSG} <br>\
                        <b>Código commit</b>: ${GIT_URL}<br>\
                        <b>Estado </b> : SUCCESSFUL <br> \
                        <b>Enlace a Git</b>: ${urlWithCodeCommit}'"

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
                <b>Failed step</b> : ${FAILED_STAGE} \
                <b>Un saludete</b> = ${holaMundo}'"

                    //final String url = "http://localhost:8080/job/Demos/job/maven-pipeline-demo/job/sdkman/2/api/json"
                    //final String response = sh(script: "curl -s $url", returnStdout: true).trim()
            }
        }
    }
}
