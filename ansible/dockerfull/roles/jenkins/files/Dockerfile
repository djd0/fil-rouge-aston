FROM jenkins/jenkins:lts-jdk11

USER root

# prerequisites for docker
RUN apt-get update && \
    apt-get -y install \
        apt-transport-https \
        ca-certificates \
        curl \
        software-properties-common \
        sshpass && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
    apt-key fingerprint 0EBFCD88 && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" && \
    apt-get update && \
    apt-get -y install docker-ce-cli docker-ce && \
    curl -L https://github.com/docker/compose/releases/download/1.16.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose && \
    apt-get install python3-pip -y && \
    pip3 install wheel && \
    pip3 install requests && \
    pip3 install docker && \
    pip3 install docker-compose && \
    pip3 install ansible && \
    service docker start

COPY daemon.json /etc/docker/daemon.json

USER jenkins
