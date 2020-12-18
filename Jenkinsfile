pipeline {
    agent any

    stages {
        stage('拉取代码') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'github-auth', url: 'https://github.com/wuzhixaun/test_pull_image.git']]])
            }
        }

        stage('编译构建') { steps {
            sh label: '', script: 'mvn clean package'
         }
}


    }
}
