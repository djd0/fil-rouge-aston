# Application de gestion d'une bibliotheque

Ce Readme à pour but de lancer l'application complete sous docker.  
A savoir : 
- Partie UI avec Angular
- Partie API REST avec Java
- Partie Base de donnée avec PostgreSQL

Si vous voulez lancer les parties de maniere indépendantes, référez-vous aux Readme disponible dans leurs sous repertoires respectif.  
Il vous faudra également déployer une base de donnée postgreSQL en local et modifier la configuration Java qui s'en rapporte.  

## Prerequis

- Docker 20.10.5
- Docker-compose 1.29.1

## Descrition

Il s'agit d'une petite application B2B qui permet de gérer les livres et reservations à destination d'une bibliotheque.

Elle permet de  : 

- Consulter les reservations à préparer pour la journée en cours
- Voir le detail complet de ces reservations
- Passer un livre en préparation pour une reservation proche
- Valider un retrait de reservation par un client
- Consulter les commandes de livres à effectuer si la quantité en stock ne suffit pas à combler la demande
- Voir le detail complet du livre à commander
- Passer une commande en préparation
- Valider le passage de la commande
- Consulter la liste complete des livres disponible
- Ajouter des livres
- Modifier des livres existants
- Supprimer des livres existants
- Consulter la liste complete des reservations passé et à venir
- Voir le detail complet de ces reservations
- Annuler des reservations

## Mise en place 

Pour lancer l'application complete, lancez la commande suivante : 

```shell
docker-compose up
```

La commande permet de lancer 3 containers contenant respectivement : 

- Une base de donnée PostgreSQL
- L'interface web sous Angular
- L'API REST sous Java

L'interface web est accessible depuis un navigateur à l'adresse http://localhost

Vous pouvez changer l'environnement de build dans les Dockerfile de l'UI et de l'API si nécessaire.  
Ceux-ci étant par défaut sur le profil développement.  

Des images intermediaires pour builder les applications étant créée, vous pouvez les supprimer après vos tests pour gagner de la place disque avec la commandes suivante : 

```shell
docker image prune --filter label=stage=build
```

Si vous voulez tout détruire : 

```shell
# Si vous stopper les container
docker-compose down
# Si vous stopper et detruire les images en conservant vos données postgreSQL
docker-compose down --rmi all
# Si tout detruire données comprises
docker-compose down --rmi all -v 
```
