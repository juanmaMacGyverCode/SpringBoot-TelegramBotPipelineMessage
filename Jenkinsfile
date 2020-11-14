#!groovy
import groovy.json.JsonSlurper
import java.time.*
import java.time.format.DateTimeFormatter

def holaMundo = "HOLAAAAA"

def getCardById() {

        URL apiUrl = new URL("https://api.github.com/repos/juanmaMacGyverCode/SpringBoot-TelegramBotPipelineMessage/commits")
        def card = new JsonSlurper().parseText(apiUrl.getText())
        return card
}

def funcionExito() {
    def branch = BRANCH_NAME
    if (branch == "master") {
        FAILED_STAGE = env.STAGE_NAME
        env.GIT_COMMIT_MSG = sh(script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
        GIT_NAME = sh(script: 'git --no-pager show -s --format=%an ${GIT_COMMIT}', returnStdout: true).trim()
        GIT_EMAIL = sh(script: 'git --no-pager show -s --format=%ae ${GIT_COMMIT}', returnStdout: true).trim()
        env.GIT_REPO_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
        def gitTemp = env.GIT_URL
        def urlShort = gitTemp.substring(0, gitTemp.length() - 4)
        def urlWithCodeCommit = urlShort + "/commit/" + env.GIT_COMMIT
        def now = LocalDateTime.now()

        def dateTime = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        withCredentials([string(credentialsId: 'HTTP_TOKEN', variable: 'TOKEN'),
                         string(credentialsId: 'CHAT_ID', variable: 'ID')]) {

            def htmlMessageBot = "<b>+-+-+-+-+-+-+-+-+-+-+-+-+</b>\n\n<b>Estado</b>: <b>SUCCESSFUL</b> \n\n <b>Project</b>: ${env.GIT_REPO_NAME} \n<b>Branch</b>: ${BRANCH_NAME} \n<b>Fecha del commit</b>: ${dateTime} \n<b>Autor Commit</b>: ${GIT_NAME} \n<b>Email Commit</b>: ${GIT_EMAIL} \n<b>Mensaje Commit</b>: ${env.GIT_COMMIT_MSG} \n<b>Código commit</b>: ${GIT_COMMIT} \n<b>Enlace a Git</b>: ${urlWithCodeCommit}\n\n<b>+-+-+-+-+-+-+-+-+-+-+-+-+</b>"
            sh "curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id='${ID}' -d parse_mode='HTML' -d text='${htmlMessageBot}'"

        }
    }
}

def funcionError() {
    if (branch == "master") {
        env.GIT_REPO_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
        env.GIT_COMMIT_MSG = sh(script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
        GIT_NAME = sh(script: 'git --no-pager show -s --format=%an ${GIT_COMMIT}', returnStdout: true).trim()
        GIT_EMAIL = sh(script: 'git --no-pager show -s --format=%ae ${GIT_COMMIT}', returnStdout: true).trim()

        def gitTemp = env.GIT_URL
        def urlShort = gitTemp.substring(0, gitTemp.length() - 4)
        def urlWithCodeCommit = urlShort + "/commit/" + env.GIT_COMMIT

        def now = LocalDateTime.now()
        def dateTime = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        withCredentials([string(credentialsId: 'HTTP_TOKEN', variable: 'TOKEN'),
                         string(credentialsId: 'CHAT_ID', variable: 'ID')]) {
            def htmlMessageBot = "<b>+-+-+-+-+-+-+-+-+-+-+-+-+</b>\n\n<b>Estado</b>: <b>FAILURE</b> \n\n<b>Project</b>: ${env.GIT_REPO_NAME} \n<b>Branch</b>: ${BRANCH_NAME} \n<b>Fecha del commit</b>: ${dateTime} \n<b>Autor Commit</b>: ${GIT_NAME} \n<b>Email Commit</b>: ${GIT_EMAIL} \n<b>Mensaje Commit</b>: ${env.GIT_COMMIT_MSG} \n<b>Código commit</b>: ${GIT_COMMIT} \n<b>Failed step</b>: ${FAILED_STAGE} \n<b>Enlace a Git</b>: ${urlWithCodeCommit}\n\n<b>+-+-+-+-+-+-+-+-+-+-+-+-+</b>"
            sh "curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage -d chat_id='${ID}' -d parse_mode='HTML' -d text='${htmlMessageBot}'"
        }
    }
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
            steps {
                script{
                    funcionExito()

                }
            }
        }
    }
    post {
        failure {
            script{
                funcionError()
            }
        }
    }
}
