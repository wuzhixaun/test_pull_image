
// 阿里云私服地址
def docker_host = "registry.cn-hangzhou.aliyuncs.com"

def dockerRegistryName="${docker_host}/wuzhixuan"
// spring 配置文件
def SPRING_PROFILE = "local"
node{
    stage('拉取代码') {
        echo "1.git仓库下载代码"
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'github-auth', url: 'https://github.com/wuzhixaun/test_pull_image.git']]])
    }
        

    stage('解析pom文件') {
        echo "2.解析pom文件"
        // 读取pom文件
        pom = readMavenPom file: 'pom.xml'
        // 镜像名称
        docker_img_name = "${pom.groupId}-${pom.artifactId}"
        echo "group: ${pom.groupId}, artifactId: ${pom.artifactId}, version: ${pom.version}"
        
        echo "docker-img-name: ${docker_img_name}"
        script {
            build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
            if (env.BRANCH_NAME != 'master' && env.BRANCH_NAME != null) {
                build_tag = "${env.BRANCH_NAME}-${build_tag}"
            }
        }
        echo "build_tag:${build_tag}"
    }

    stage('预编译') {
        echo "4.预编译"
        sh "mvn test"
    }
    
    stage('删除/构建镜像') {
        echo "5.编译+ 删除本地镜像 + 构建当前镜像"
        sh "mvn package  -Dmaven.test.skip=true"
       
        // 删除本地镜像
        sh "docker rmi -f ${docker_img_name}:${build_tag}"
        sh "docker rmi -f ${dockerRegistryName}/${docker_img_name}:${build_tag}"
        sh "docker rmi -f ${dockerRegistryName}/${docker_img_name}:latest"
        sh "docker rmi -f ${dockerRegistryName}/${docker_img_name}:${pom.version}"
        // 构建当前镜像
        sh "docker build -t ${docker_img_name}:${build_tag} " +
                " --build-arg ${SPRING_PROFILE} " +
                " --build-arg JAR_FILE=target/${pom.artifactId}-${pom.version}.jar " +
                " ."
    }

    stage('推送镜像到阿里云私服') {
        echo "6.推送镜像到阿里云私服"
        
        // 将镜像打上tag
        sh "docker tag ${docker_img_name}:${build_tag} ${dockerRegistryName}/${docker_img_name}:latest"
        sh "docker tag ${docker_img_name}:${build_tag} ${dockerRegistryName}/${docker_img_name}:${pom.version}"
        
        // 登录并且push阿里云镜像私服
        withCredentials([usernamePassword(credentialsId: 'docker-register', passwordVariable: 'dockerPassword', usernameVariable: 'dockerUser')]) {
            sh "docker login -u ${dockerUser} -p ${dockerPassword} ${docker_host}"
            sh "docker push ${dockerRegistryName}/${docker_img_name}:latest"
            sh "docker push ${dockerRegistryName}/${docker_img_name}:${pom.version}"
            sh "docker push ${dockerRegistryName}/${docker_img_name}:${build_tag}"
        }
    }

    stash 'complete-build'

}


if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == null) {
    timeout(time: 10, unit: 'MINUTES') {
        input '确认要部署线上环境吗？'
    }
}
 
 
node{
    stage('Deploy') {
        //unstash 'complete-build'
        echo "5. Deploy Stage"
 
        sh "sed -i 's/<IMG_NAME>/${img_name}:${build_tag}/' location/k8s.yaml"
        sh "sed -i 's/<BRANCH_NAME>/${env.BRANCH_NAME}/' location/k8s.yaml"
        sh "/data/opt/kubernetes/client/bin/kubectl apply -f ${WORKSPACE}/location/k8s.yaml --record"
    }
}