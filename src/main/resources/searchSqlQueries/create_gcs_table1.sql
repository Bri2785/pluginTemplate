CREATE TABLE `briteideasupdate`.`gcs_seed`(
  `id` INT NOT NULL AUTO_INCREMENT,
  `commonName` VARCHAR(60) NOT NULL,
  `scientificName` VARCHAR(252),
  PRIMARY KEY (`id`),
  KEY `name` (`commonName`)
)