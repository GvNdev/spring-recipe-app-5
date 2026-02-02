## Use to run mysql db docker image, optional if you're not using a local mysqldb
# docker run -d --name mysql -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -v <local directory>:/var/lib/mysql mysql

# connect to mysql and run as root user
# Create Databases
CREATE DATABASE stmc_dev;
CREATE DATABASE stmc_prod;

# Create database service accounts
CREATE USER 'stmc_dev_user'@'localhost' IDENTIFIED BY 'stmc';
CREATE USER 'stmc_prod_user'@'localhost' IDENTIFIED BY 'stmc';
CREATE USER 'stmc_dev_user'@'%' IDENTIFIED BY 'stmc';
CREATE USER 'stmc_prod_user'@'%' IDENTIFIED BY 'stmc';

# Database grants
GRANT SELECT ON stmc_dev.* to 'stmc_dev_user'@'localhost';
GRANT INSERT ON stmc_dev.* to 'stmc_dev_user'@'localhost';
GRANT DELETE ON stmc_dev.* to 'stmc_dev_user'@'localhost';
GRANT UPDATE ON stmc_dev.* to 'stmc_dev_user'@'localhost';
GRANT SELECT ON stmc_prod.* to 'stmc_prod_user'@'localhost';
GRANT INSERT ON stmc_prod.* to 'stmc_prod_user'@'localhost';
GRANT DELETE ON stmc_prod.* to 'stmc_prod_user'@'localhost';
GRANT UPDATE ON stmc_prod.* to 'stmc_prod_user'@'localhost';
GRANT SELECT ON stmc_dev.* to 'stmc_dev_user'@'%';
GRANT INSERT ON stmc_dev.* to 'stmc_dev_user'@'%';
GRANT DELETE ON stmc_dev.* to 'stmc_dev_user'@'%';
GRANT UPDATE ON stmc_dev.* to 'stmc_dev_user'@'%';
GRANT SELECT ON stmc_prod.* to 'stmc_prod_user'@'%';
GRANT INSERT ON stmc_prod.* to 'stmc_prod_user'@'%';
GRANT DELETE ON stmc_prod.* to 'stmc_prod_user'@'%';
GRANT UPDATE ON stmc_prod.* to 'stmc_prod_user'@'%';