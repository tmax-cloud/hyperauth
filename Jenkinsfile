node {
    def gitHubBaseAddress = "github.com"
    def gitHyperAuthAddress = "${gitHubBaseAddress}/tmax-cloud/hyperauth.git"
    def buildDir = "/var/lib/jenkins/workspace/hyperauth"
    def scriptHome = "${buildDir}/scripts"
    def version = "${params.majorVersion}.${params.minorVersion}.${params.tinyVersion}.${params.hotfixVersion}"
    def preVersion = "${params.preVersion}"
    def imageTag = "b${version}"
    def globalVersion = "HyperAuth-server:b${version}"
    def githubUserName = "ck-jenkins"
    def githubUserToken = "${params.githubUserToken}"
    def userEmail = "taegeon_woo@tmax.co.kr"
    
    stage('git pull from hyperauth') {
    	git branch: "${params.buildBranch}",
        credentialsId: '${githubUserName}',
        url: "http://${gitHyperAuthAddress}"

        sh "git checkout ${params.buildBranch}"
        sh "git fetch --all"
        sh "git reset --hard origin/${params.buildBranch}"
        sh "git pull origin ${params.buildBranch}"
    }
    
    stage('Maven build from Hyperauth-spi & git push') {
    	withMaven(
            maven: 'M353',
            mavenSettingsConfig: 'd8df29f7-3dc7-4506-b839-e2474e4fc051')
        {
            sh "mvn install:install-file -Dfile=lib/com/tmax/tibero/jdbc/6.0/tibero6-jdbc.jar -DgroupId=com.tmax.tibero -DartifactId=jdbc -Dversion=6.0 -Dpackaging=jar -DgeneratePom=true"
            sh "mvn install:install-file -Dfile=lib/com/tmax/hyperauth/server-spi-private/11.0.2/keycloak-server-spi-private-11.0.2.jar -DgroupId=com.tmax.hyperauth -DartifactId=server-spi-private -Dversion=11.0.2 -Dpackaging=jar -DgeneratePom=true"
        }
        mavenInstall("${buildDir}", "${globalVersion}")
    }

    stage('image build & push'){
        if(type == 'distribution') {
            sh "sudo docker build --tag tmaxcloudck/hyperauth:${imageTag} --build-arg HYPERAUTH_VERSION=${imageTag} ."
            sh "sudo docker tag tmaxcloudck/hyperauth:${imageTag} tmaxcloudck/hyperauth:latest"
            sh "sudo docker push tmaxcloudck/hyperauth:${imageTag}"
            sh "sudo docker push tmaxcloudck/hyperauth:latest"
            sh "sudo docker rmi tmaxcloudck/hyperauth:${imageTag}"

        } else if(type == 'test'){
            sh "sudo docker build --tag 192.168.9.12:5000/hyperauth-server:b${testVersion} --build-arg HYPERAUTH_VERSION=b${testVersion} ."
            sh "sudo docker push 192.168.9.12:5000/hyperauth-server:b${testVersion}"
            sh "sudo docker rmi 192.168.9.12:5000/hyperauth-server:b${testVersion}"

        }
    }

	if(type == 'distribution') {
        stage('make change log'){
            sh "sudo sh ${scriptHome}/hyper-auth-changelog.sh ${version} ${preVersion}"
        }

        stage('git push'){
            sh "git checkout ${params.buildBranch}"
            sh "git add -A"

            sh (script:'git commit -m "[Distribution] Hyper Auth Server- ${version} " || true')
            sh "git tag v${version}"

            sh "git remote set-url origin https://${githubUserToken}@github.com/tmax-cloud/hyperauth.git"

            sh "sudo git push -u origin +${params.buildBranch}"
            sh "sudo git push origin v${version}"

            sh "git fetch --all"
            sh "git reset --hard origin/${params.buildBranch}"
            sh "git pull origin ${params.buildBranch}"

        }
	}

	stage('clear repo'){
        sh "sudo rm -rf *"
    }
}

void mavenInstall(dirPath,globalVersion) {
    dir (dirPath) {
        withMaven(
        maven: 'M353',
        mavenSettingsConfig: 'd8df29f7-3dc7-4506-b839-e2474e4fc051') {
            sh "mvn clean install -N"
            sh "mvn clean install"
        }
    }
}



