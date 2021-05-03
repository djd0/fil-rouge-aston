#!/bin/bash
# Récupération de fichier
#get ./titi.txt
connexion à la machine distance
SUSER=aws
SPASS=install!
SHOST=10.94.11.14

ssh $SUSER@$SHOST -p 22
cd Desktop
#put titi.txt
scp titi.txt aws@10.94.11.14:Desktop
exit
