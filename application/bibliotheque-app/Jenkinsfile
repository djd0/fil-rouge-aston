pipeline {
    agent any
    tools {
        maven "Maven 3.8.1"
    }
    options {
        parallelsAlwaysFailFast()
    }
    stages {
        stage('Maven Build') {
            steps {
                sh "mvn -f pom.xml clean package"
                archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
            }
        }
        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                  sh "mvn -f pom.xml clean package sonar:sonar -Dsonar.host_url=$SONAR_HOST_URL "
                }
            }
        }
        stage('Nexus preprod') {
            environment {
                NEXUS_VERSION = "nexus3"
                NEXUS_PROTOCOL = "http"
                NEXUS_URL = "161.97.140.32:8081/"
                NEXUS_REPOSITORY = "maven-snapshots"
                NEXUS_CREDENTIAL_ID = "Nexus"
            }
            when {
                expression {
                    env.BRANCH_NAME == 'master'
                }
            }
            steps {
                script {
                    def version = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true
                    pom = readMavenPom file: "pom.xml";
                    // Find built artifact under target folder
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    // Print some info from the artifact found
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    // Extract the path from the File found
                    artifactPath = filesByGlob[0].path;
                    // Assign to a boolean response verifying If the artifact name exists
                    artifactExists = fileExists artifactPath;

                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}, id: ${pom.artifactId}";

                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [
                                    artifactId: pom.artifactId,
                                    classifier: '',
                                    file: artifactPath,
                                    type: pom.packaging
                                ]
                            ]
                        );

                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
            }
        }
        stage('Nexus prod') {
            when {
                expression {
                    env.BRANCH_NAME == 'production'
                }
            }
            steps {
                script {
                    def version = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true
                    nexusPublisher nexusInstanceId: 'Nexus', nexusRepositoryId: 'maven-releases', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "target/bibliotheque-services-${version}.jar"]], mavenCoordinate: [artifactId: 'bibliotheque-services', groupId: 'fr.bibliotheque', packaging: 'jar', version: version]]]
                }
            }
        }
        stage('Push preprod') {
            when {
                expression {
                    env.BRANCH_NAME == 'master'
                }
            }
            steps {
                ansiblePlaybook(
                                    vaultCredentialsId: 'AnsibleVault',
                                    inventory: '.inventory',
                                    playbook: 'playbook.yml',
                                    tags: "loginDockerhub, buildAndPushPreprodAppNexusImage"
                                )
            }
        }
        stage('Push prod') {
            when {
                expression {
                    env.BRANCH_NAME == 'production'
                }
            }
            steps {
                ansiblePlaybook(
                                    vaultCredentialsId: 'AnsibleVault',
                                    inventory: '.inventory',
                                    playbook: 'playbook.yml',
                                    tags: "loginDockerhub, buildAndPushProdAppNexusImage"
                                )
            }
        }
        stage('Deploy preprod') {
            when {
                expression {
                    env.BRANCH_NAME == 'master'
                }
            }
            steps {
                ansiblePlaybook(
                                    vaultCredentialsId: 'AnsibleVault',
                                    inventory: '.inventory',
                                    playbook: 'playbook.yml',
                                    tags: "loginDockerhub, copyBibliothequeAppDockerCompose, removeOldBibliothequeApp, pullPreprodAppImage, runBibliothequeAppImage"
                                )
            }
        }
        stage('Deploy prod') {
            when {
                expression {
                    env.BRANCH_NAME == 'production'
                }
            }
            steps {
                ansiblePlaybook(
                                    vaultCredentialsId: 'AnsibleVault',
                                    inventory: '.inventory',
                                    playbook: 'playbook.yml',
                                    tags: "loginDockerhub, copyBibliothequeAppDockerCompose, removeOldBibliothequeApp, pullProdAppImage, runBibliothequeAppImage"
                                )
            }
        }
    }
}
