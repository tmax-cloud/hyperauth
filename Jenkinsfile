node {
    def gitHubBaseAddress = "github.com"
    def gitHyperAuthAddress = "${gitHubBaseAddress}/tmax-cloud/hyperauth.git"
    def gitInstallHyperAuthLegacyAddress = "${gitHubBaseAddress}/tmax-cloud/install-hyperauth-legacy.git"
    def buildDir = "/var/lib/jenkins/workspace/hyperauth_v1"
    def installLegacyDir = "${buildDir}/install-legacy"
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

        if(type == 'distribution') {
            sh "sed -i 's/hyperauth_version/${imageTag}/g' themes/tmax/account/theme.properties"
            sh "sed -i 's/hyperauth_version/${imageTag}/g' themes/tmax/admin/theme.properties"
            sh "sed -i 's/hyperauth_version/${imageTag}/g' themes/tmax/email/theme.properties"
            sh "sed -i 's/hyperauth_version/${imageTag}/g' themes/tmax/login/theme.properties"
            sh "sed -i 's/hyperauth_version/${imageTag}/g' themes/tmax/welcome/theme.properties"
        } else if(type == 'test'){
            sh "sed -i 's/hyperauth_version/b${testVersion}/g' themes/tmax/account/theme.properties"
            sh "sed -i 's/hyperauth_version/b${testVersion}/g' themes/tmax/admin/theme.properties"
            sh "sed -i 's/hyperauth_version/b${testVersion}/g' themes/tmax/email/theme.properties"
            sh "sed -i 's/hyperauth_version/b${testVersion}/g' themes/tmax/login/theme.properties"
            sh "sed -i 's/hyperauth_version/b${testVersion}/g' themes/tmax/welcome/theme.properties"
        }
    }
    
    stage('Maven build from Hyperauth-spi & git push') {
    	withMaven(
            maven: 'M353',
            mavenSettingsConfig: 'd8df29f7-3dc7-4506-b839-e2474e4fc051',
            jdk: 'jdk-1.8')
        {
            sh "mvn install:install-file -Dfile=lib/com/tmax/tibero/jdbc/6.0/tibero6-jdbc.jar -DgroupId=com.tmax.tibero -DartifactId=jdbc -Dversion=6.0 -Dpackaging=jar -DgeneratePom=true"
            sh "mvn install:install-file -Dfile=lib/com/tmax/hyperauth/server-spi-private/11.0.2/keycloak-server-spi-private-11.0.2.jar -DgroupId=com.tmax.hyperauth -DartifactId=server-spi-private -Dversion=11.0.2 -Dpackaging=jar -DgeneratePom=true"
        }
        mavenInstall("${buildDir}", "${globalVersion}")
    }

    stage('image build & push'){
        if(type == 'distribution') {

            sh" sudo docker login hyperregistry.tmaxcloud.org -u admin -p admin"
            sh "sudo docker build --tag hyperregistry.tmaxcloud.org/hyperauth/hyperauth:${iZmageTag} --build-arg HYPERAUTH_VERSION=${imageTag} ."
            sh "sudo docker push hyperregistry.tmaxcloud.org/hyperauth/hyperauth:${imageTag}"
            sh "sudo docker rmi hyperregistry.tmaxcloud.org/hyperauth/hyperauth:${imageTag}"
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
            sh "sed -i 's/${imageTag}/hyperauth_version/g' themes/tmax/account/theme.properties"
            sh "sed -i 's/${imageTag}/hyperauth_version/g' themes/tmax/admin/theme.properties"
            sh "sed -i 's/${imageTag}/hyperauth_version/g' themes/tmax/email/theme.properties"
            sh "sed -i 's/${imageTag}/hyperauth_version/g' themes/tmax/login/theme.properties"
            sh "sed -i 's/${imageTag}/hyperauth_version/g' themes/tmax/welcome/theme.properties"

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

        stage('git pull from install-hyperauth-legacy && Make hyperauth-legacy.tar') {
            dir(installLegacyDir){
                git branch: "master",
                credentialsId: '${githubUserName}',
                url: "http://${gitInstallHyperAuthLegacyAddress}"

                sh "git checkout master"
                sh "git fetch --all"
                sh "git reset --hard origin/master"
                sh "git pull origin master"

                sh "rm -rf themes/"
                sh "cp -rf ../themes ./"
                sh "rm -rf standalone/deployments/hyperauth-spi.jar"
                sh "cp -rf ../target/keycloak-spi-jar-with-dependencies.jar standalone/deployments/hyperauth-spi.jar"

                sh "tar cvfz hyperauth-legacy-${imageTag}.tar ./*"
                sh "sudo cp hyperauth-legacy-${imageTag}.tar /root/hyperauth-legacy-tar/hyperauth-legacy-${imageTag}.tar"
                sh "rm -rf hyperauth-legacy-${imageTag}.tar"

                sh "git add -A"
                sh (script:'git commit -m "[Distribution] Install HyperAuth Legacy- ${version} " || true')
                sh "git tag v${version}"
                sh "git remote set-url origin https://${githubUserToken}@github.com/tmax-cloud/install-hyperauth-legacy.git"
                sh "sudo git push -u origin +master"
                sh "sudo git push origin v${version}"
            }
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
        mavenSettingsConfig: 'd8df29f7-3dc7-4506-b839-e2474e4fc051',
        jdk: 'jdk-1.8') {
            sh "mvn clean install -N"
            sh "mvn clean install"
        }
    }
}



