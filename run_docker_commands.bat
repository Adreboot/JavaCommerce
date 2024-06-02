@echo off

REM Démarrer le conteneur MySQL temporaire
docker run -v keycloak_mysql_data:/var/lib/mysql -d --name tempMySQLContainer -e MYSQL_ROOT_PASSWORD=cnam -p 3307:3306 mysql

REM Attendre quelques secondes pour s'assurer que le conteneur est complètement démarré
timeout /t 5 /nobreak > nul

REM Créer le répertoire /backup dans le conteneur
docker exec tempMySQLContainer mkdir /backup

REM Copier le fichier backup.tar dans le conteneur
docker cp C:\Users\User\Desktop\GLG203\testDocker\backup.tar tempMySQLContainer:/backup/backup.tar

REM Vérifier si le fichier a bien été copié
docker exec tempMySQLContainer dir /backup

REM Extraire le fichier backup.tar dans le conteneur
docker exec tempMySQLContainer bash -c "cd /var/lib/mysql && tar xvf /backup/backup.tar --strip 3"

REM Supprimer le conteneur temporaire
docker rm -f tempMySQLContainer

REM Exécuter docker-compose pour démarrer les services définis dans docker-compose_all.yml
docker-compose -f docker-compose_all.yml up -d
