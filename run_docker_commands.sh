#!/bin/bash

# Vérifier si le fichier backup.tar existe localement
if [ ! -f ./backup.tar ]; then
    echo "Erreur : Le fichier backup.tar n'existe pas à l'emplacement spécifié."
    exit 1
fi

# Créer un répertoire temporaire pour le montage
TEMP_DIR=$(mktemp -d)

# Copier le fichier backup.tar dans ce répertoire temporaire
cp ./backup.tar $TEMP_DIR/

# Démarrer le conteneur MySQL temporaire avec un volume monté pour le répertoire temporaire
docker run -v keycloak_mysql_data:/var/lib/mysql -v $TEMP_DIR:/backup -d --name tempMySQLContainer -e MYSQL_ROOT_PASSWORD=cnam -p 3307:3306 mysql

# Attendre quelques secondes pour s'assurer que le conteneur est complètement démarré
sleep 5

# Vérifier le contenu du répertoire /backup dans le conteneur
docker exec tempMySQLContainer ls -la /backup

# Vérifier si le fichier a bien été copié
if docker exec tempMySQLContainer test -f /backup/backup.tar; then
    # Extraire le fichier backup.tar dans le conteneur
    docker exec tempMySQLContainer bash -c "cd /var/lib/mysql && tar xvf /backup/backup.tar --strip 3"
else
    echo "Erreur : Le fichier /backup/backup.tar n'a pas été trouvé dans le conteneur."
    exit 1
fi

# Supprimer le conteneur temporaire
docker rm -f tempMySQLContainer

# Supprimer le répertoire temporaire
rm -rf $TEMP_DIR

# Exécuter docker-compose pour démarrer les services définis dans docker-compose_all.yml
docker-compose -f docker-compose_all.yml up -d
