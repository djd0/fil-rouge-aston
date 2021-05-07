# Interface Web de l'application
## Prerequis

- Angular 10

## Lancement local

Pour lancer l'interface web, il vous faut tout d'abord installer les packages nodes.  
Pour ce faire, placez vous dans un terminal à la racine de bibliotheque-ui et tapez la commande : 

```shell
npm install
```

Une fois l'installation terminé, vous pouvez lancer l'application avec la commande : 

```shell
ng serve
```

L'interface sera disponible dans un navigateur à l'adresse http://localhost:4200

## Déploiement v4 (Jenkins)

Un Jenkinsfile est disponible dans le repertoire.  
Celui-ci vous permettra de builder et déployer votre projet directement via Jenkins.  
Jenkins est configuré pour écouter les push sur les branches master et production.  
Une fois votre evolution fonctionnelle, vous n'avez qu'a merger votre branche de travail avec le master et le pipeline s'enclenche.

### Pipeline Jenkins

![alt Pipeline Jenkins](pipelineUi.png "Pipeline Jenkins")

## Déploiement v3 (Ansible dockerfull)

L'application est déployable via Ansible directement.
Les tâches dans le role 'bibliothequeapplication' se chargeront de builder l'application, de l'envoyer vers dockerHub et de deployer directement la nouvelle version de l'image dans vos serveurs.  
Comme toujours avec Ansible, vérifiez que l'adresse de votre/vos serveurs est la bonne dans le fichier .inventory d'ansible et que les repertoires de destination des tâches correspondent aux votres.
Il vous faudra également être sûr que vos serveurs integrent les dépendances de ce role.
Vous pouvez vous réferer aux schémas dans le Readme d'ansible dockerfull.  

```shell
# Preprod
ansible-playbook playbook.yml --tags "buildAndPushPreprodUiImageWithSources, runUiImage, pruneImages"
# Prod
ansible-playbook playbook.yml --tags "buildAndPushProdUiImageWithSources, runUiImage, pruneImages"
```

## Déploiement v2 (Ansible dockerless)

Vous pouvez déployer l'application sur vos serveurs avec ansible.  
En effet, il existe des tâches sous le role 'filrougeangular' qui déploiront directement votre build sur vos serveurs.  
Pour ce faire, vérifiez que l'adresse de votre/vos serveurs est la bonne dans le fichier .inventory d'ansible et que les repertoires de destination des tâches correspondent aux votres.
Il vous faudra également être sûr que vos serveurs integrent les dépendances de ce role.   
Vous pouvez vous réferer aux schémas dans le Readme d'ansible.  
Si ce n'est pas le cas, vous pouvez provisionner vos serveurs via ansible en ciblant les rôles qui vous manque dans le playbook.  
En effet, toutes les dépendances necessaires pour faire tourner l'application sont déjà intégrées dans ansible.  
Enfin, il vous faudra aussi adapter les fichiers d'environnement de l'application afin de cibler vos noms de domaine ou adresses.  
Ceux-ci sont situés dans bibliotheque-ui/src/environments.

Pour déployer avec ansible, il faut dans un premier temps builder votre projet avec l'une de ces commande en fonction de votre environnement de deploiement : 

```shell
# local
ng build
# pre-production
ng build --preprod
# production
ng build --prod
```

Ensuite récupérer le contenu du dossier 'dist' et compressez-le dans une archive : 

```shell
# Vous pouvez changer la version si vous le désirez
# Le nom de l'archive peut aussi être changé mais il faudra dans ce cas changer aussi le nom dans les tâches ansible associées au déploiement
tar -cvf fil-rouge-aston-1.0.0.tar.gz dist/
```

Copiez l'archive dans le repertoire 'files' du role 'filrougeangular'.  
Enfin, lancez la commande ansible suivante à la racine du dossier ansible : 

```shell
ansible-playbook playbook.yml --tags "freshDeployAngularFilRougeApp"
```

## Déploiement v1 (Manuel)

Buildez l'application avec l'une de ces commande en fonction de votre environnement de deploiement :

```shell
# local
ng build
# pre-production
ng build --preprod
# production
ng build --prod
```

Copiez le dossier 'dist' sur votre serveur dans le repertoire du serveur web (/var/www/html)

Changez les droits sur ce dossier et attribuez le au groupe www-data : 

```shell
chown www-data:www-data dist -R  
chmod 665 dist -R  
```

Relancez le serveur web

```shell
systemctl restart apache2
```
