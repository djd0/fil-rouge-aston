pipeline {
    agent any
    tools {
        nodejs "NodeJS"
    }
    options {
        parallelsAlwaysFailFast()
    }
    stages {
        stage('Install dependencies ') {
            steps {
                sh "npm install"
            }
        }
        stage('Tests Analysis') {
            when {
                expression {
                    env.BRANCH_NAME == 'master'
                }
            }
            steps {
                sh "ng lint"
            }
        }
        stage('Angular Build preprod') {
            when {
                expression {
                    env.BRANCH_NAME == 'master'
                }
            }
            steps {
                sh "ng build --configuration='preproduction'"
            }
        }
        stage('Angular Build prod') {
            when {
                expression {
                    env.BRANCH_NAME == 'production'
                }
            }
            steps {
                sh "ng build --configuration='production'"
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

                    def version = readFile(file: '.version').trim()
                    sh "tar czf 'dist-${version}.tgz' -C dist ."
                    nexusArtifactUploader(
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        nexusUrl: NEXUS_URL,
                        groupId: "fr.bibliotheque",
                        version: version,
                        repository: NEXUS_REPOSITORY,
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        artifacts: [
                            [
                                artifactId: "bibliotheque-ui",
                                classifier: '',
                                file: "dist-${version}.tgz",
                                type: "tgz"
                            ]
                        ]
                    );
                }
            }
        }
        stage('Nexus prod') {
            environment {
                NEXUS_VERSION = "nexus3"
                NEXUS_PROTOCOL = "http"
                NEXUS_URL = "161.97.140.32:8081/"
                NEXUS_REPOSITORY = "maven-releases"
                NEXUS_CREDENTIAL_ID = "Nexus"
            }
            when {
                expression {
                    env.BRANCH_NAME == 'production'
                }
            }
            steps {
                script {

                    def version = readFile(file: '.version').trim()
                    sh "tar czf 'dist-${version}.tgz' -C dist ."
                    nexusArtifactUploader(
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        nexusUrl: NEXUS_URL,
                        groupId: "fr.bibliotheque",
                        version: version,
                        repository: NEXUS_REPOSITORY,
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        artifacts: [
                            [
                                artifactId: "bibliotheque-ui",
                                classifier: '',
                                file: "dist-${version}.tgz",
                                type: "tgz"
                            ]
                        ]
                    );
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
                                    tags: "loginDockerhub, buildAndPushPreprodUiNexusImage",
                                    limit: "localhost"
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
                                    tags: "loginDockerhub, buildAndPushProdUiNexusImage",
                                    limit: "localhost"
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
                                    tags: "loginDockerhub, runUiImage",
                                    limit: "devfilrouge"
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
                                    tags: "loginDockerhub, runUiImage",
                                    limit: "prodfilrouge"
                                )
            }
        }
    }
}
