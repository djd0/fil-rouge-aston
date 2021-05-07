# API REST Java
## Prerequis

- Java 11
- Maven 3.6.3

## Lancement local

Importez le projet avec maven dans un IDE java.  
Lancez la class Main 'BibliothequeApplication'

## Déploiement v4 (Jenkins)

Un Jenkinsfile est disponible dans le repertoire.  
Celui-ci vous permettra de builder et déployer votre projet directement via Jenkins.  
Jenkins est configuré pour écouter les push sur les branches master et production.  
Une fois votre evolution fonctionnelle, vous n'avez qu'a merger votre branche de travail avec le master et le ipeline s'enclenche.

### Pipeline Jenkins

![alt Pipeline Jenkins](pipelineServices.png "Pipeline Jenkins")

## Déploiement v3 (Ansible dockerfull)

L'application est déployable via Ansible directement.
Les tâches dans le role 'bibliothequeapplication' se chargeront de builder l'application, de l'envoyer vers dockerHub et de deployer directement la nouvelle version de l'image dans vos serveurs.  
Comme toujours avec Ansible, vérifiez que l'adresse de votre/vos serveurs est la bonne dans le fichier .inventory d'ansible et que les repertoires de destination des tâches correspondent aux votres.
Il vous faudra également être sûr que vos serveurs integrent les dépendances de ce role.
Vous pouvez vous réferer aux schémas dans le Readme d'ansible dockerfull.

```shell
# Preprod
ansible-playbook playbook.yml --tags "buildAndPushPreprodAppImageWithSources, runUiImage, pruneImages"
# Prod
ansible-playbook playbook.yml --tags "buildAndPushProdAppImageWithSources, runAppImage, pruneImages"
```

## Déploiement v2 (Ansible dockerless)

Vous pouvez déployer l'application sur vos serveurs avec ansible.  
En effet, il existe des tâches sous le role 'filrougejava' qui déploiront directement votre build sur vos serveurs.  
Pour ce faire, vérifiez que l'adresse de votre/vos serveurs est la bonne dans le fichier .inventory d'ansible et que les repertoires de destination des tâches correspondent aux votres.
Il vous faudra également être sûr que vos serveurs integrent les dépendances de ce role.   
Vous pouvez vous réferer aux schémas dans le Readme d'ansible.  
Si ce n'est pas le cas, vous pouvez provisionner vos serveurs via ansible en ciblant les rôles qui vous manque dans le playbook.  
En effet, toutes les dépendances necessaires pour faire tourner l'application sont déjà intégrées dans ansible.  
Enfin, il vous faudra aussi adapter les fichiers d'environnement de l'application afin de cibler votre base de donnée.  
Ceux-ci sont situés dans bibliotheque-app/src/main/ressources.

Pour déployer avec ansible, il faut dans un premier temps builder votre projet avec l'une de ces commande en fonction de votre environnement de deploiement :

```shell
# local
mvn clean package
# pre-production
mvn clean package -P preprod
# production
mvn clean package -P prod
```

Ensuite récupérer le jar du dossier 'target' et copiez-le dans le repertoire 'files' du role 'filrougejava'.
Enfin, lancez la commande ansible suivante à la racine du dossier ansible :

```shell
ansible-playbook playbook.yml --tags "freshDeployJavaFilRougeApp"
```

## Déploiement v1 (Manuel)

Buildez l'application avec l'une de ces commande en fonction de votre environnement de deploiement :

```shell
# local
mvn clean package
# pre-production
mvn clean package -P preprod
# production
mvn clean package -P prod
```

Copiez le fichier jar situé dans le dossier 'target' sur votre serveur.  

Changez les droits sur ce fichier et attribuez le au groupe spécifique à l'execution de vos applications java :

```shell
# adapter le nom du fichier en fonction du votre
chown groupe2java:groupe2java fichier.jar
chmod 550 fichier.jar
```

Créer un lien symbolique : 

```shell
ln -s fichier.jar /etc/init.d/fil-rouge  
```

Relancer les daemons et lancer le jar : 

```shell
systemctl daemon-reload
systemctl start fil-rouge
```
