CREATE DATABASE  IF NOT EXISTS `sd10db` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `sd10db`;
-- MySQL dump 10.13  Distrib 5.5.16, for osx10.5 (i386)
--
-- Host: localhost    Database: sd10db
-- ------------------------------------------------------
-- Server version	5.5.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `subscriptions_notifications`
--

DROP TABLE IF EXISTS `subscriptions_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscriptions_notifications` (
  `subscriptions_id` int(11) NOT NULL,
  `unreadNotifications_id` int(11) NOT NULL,
  KEY `FKB347CB5F7377D0D5` (`subscriptions_id`),
  KEY `FKB347CB5F2E2ACF20` (`unreadNotifications_id`),
  CONSTRAINT `FKB347CB5F2E2ACF20` FOREIGN KEY (`unreadNotifications_id`) REFERENCES `notifications` (`id`),
  CONSTRAINT `FKB347CB5F7377D0D5` FOREIGN KEY (`subscriptions_id`) REFERENCES `subscriptions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions_notifications`
--

LOCK TABLES `subscriptions_notifications` WRITE;
/*!40000 ALTER TABLE `subscriptions_notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscriptions_notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `DTYPE` varchar(31) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  PRIMARY KEY (`mail`),
  UNIQUE KEY `mail` (`mail`),
  UNIQUE KEY `password` (`password`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('subscriber','alessandro.montanar5@studio.unibo.it','Alessandro','5b4a9e08555d97c5e8bc2fb4f3a89ddd','Montanari'),('User','enrico.denti@uunibo.it','Enrico','c44386c0d8a91e6dd5ec8dcc9120c23a','Denti'),('User','giuseppe.bellavia@uunibo.it','Giuseppe','2890f325b5f43cad18b68747fb952251','Bellavia'),('subscriber','lorena.qendro@studio.unibo.it','Lorena','220aae9fe969374a41688cf588244dcf','Qendro'),('User','luca.gialli@sstudio.unibo.it','Luca','a05bd890c4868ea1807f8564055d1fba','Gialli'),('User','marco.verdi@sstudio.unibo.it','Marco','3829486b93ec44395f0b980424bae9b6','Verdi'),('User','mario.rossi@sstudio.unibo.it','Mario','addb47291ee169f330801ce73520b96f','Rossi'),('User','paolo.bellavista@uunibo.it','Paolo','f5973ee9c413d0967b98e0944688acb2','Bellavista'),('User','piero.bianchi@sstudio.unibo.it','Piero','fef08a8996bada775d592da28d26ee6a','Bianchi'),('User','silvano.martello@uunibo.it','Silvano','37b418aea1bafd89ab9cdca29f2b3b0b','Martello');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `roleName` (`roleName`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (26,'admin'),(28,'professor'),(27,'student');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscriptions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `teaching_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7674CAF664BC94E` (`teaching_id`),
  CONSTRAINT `FK7674CAF664BC94E` FOREIGN KEY (`teaching_id`) REFERENCES `teachings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
INSERT INTO `subscriptions` VALUES (14,145),(15,145);
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contents_users`
--

DROP TABLE IF EXISTS `contents_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contents_users` (
  `contents_id` int(11) NOT NULL,
  `authors_mail` varchar(255) NOT NULL,
  PRIMARY KEY (`contents_id`,`authors_mail`),
  KEY `FK1E2410A3994C7830` (`contents_id`),
  KEY `FK1E2410A342D7B7CD` (`authors_mail`),
  CONSTRAINT `FK1E2410A342D7B7CD` FOREIGN KEY (`authors_mail`) REFERENCES `users` (`mail`),
  CONSTRAINT `FK1E2410A3994C7830` FOREIGN KEY (`contents_id`) REFERENCES `contents` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contents_users`
--

LOCK TABLES `contents_users` WRITE;
/*!40000 ALTER TABLE `contents_users` DISABLE KEYS */;
INSERT INTO `contents_users` VALUES (3877,'paolo.bellavista@uunibo.it'),(3878,'giuseppe.bellavia@uunibo.it');
/*!40000 ALTER TABLE `contents_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `changeType` varchar(255) DEFAULT NULL,
  `contentId` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_roles` (
  `users_mail` varchar(255) NOT NULL,
  `roles_id` int(11) NOT NULL,
  PRIMARY KEY (`users_mail`,`roles_id`),
  KEY `FKF6CCD9C6EC42BF07` (`roles_id`),
  KEY `FKF6CCD9C69070D20D` (`users_mail`),
  CONSTRAINT `FKF6CCD9C69070D20D` FOREIGN KEY (`users_mail`) REFERENCES `users` (`mail`),
  CONSTRAINT `FKF6CCD9C6EC42BF07` FOREIGN KEY (`roles_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES ('alessandro.montanar5@studio.unibo.it',26),('alessandro.montanar5@studio.unibo.it',27),('lorena.qendro@studio.unibo.it',27),('luca.gialli@sstudio.unibo.it',27),('marco.verdi@sstudio.unibo.it',27),('mario.rossi@sstudio.unibo.it',27),('piero.bianchi@sstudio.unibo.it',27),('enrico.denti@uunibo.it',28),('giuseppe.bellavia@uunibo.it',28),('paolo.bellavista@uunibo.it',28),('silvano.martello@uunibo.it',28);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teachings`
--

DROP TABLE IF EXISTS `teachings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teachings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cfu` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `ssd` varchar(255) NOT NULL,
  `yearOfCourse` int(11) NOT NULL,
  `contentsRoot_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `contentsRoot_id` (`contentsRoot_id`),
  KEY `FKE4F02AC63975F10E` (`contentsRoot_id`),
  CONSTRAINT `FKE4F02AC63975F10E` FOREIGN KEY (`contentsRoot_id`) REFERENCES `contents` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teachings`
--

LOCK TABLES `teachings` WRITE;
/*!40000 ALTER TABLE `teachings` DISABLE KEYS */;
INSERT INTO `teachings` VALUES (139,6,'Scienza delle merendine','MAT',1,3871),(140,6,'Scienza delle costruzioni','MAT',1,3872),(141,6,'Statistica','MAT',1,3873),(142,6,'Fon. Informatica','MAT',1,3874),(143,6,'Ingegneria vs Matematica','MAT',1,3875),(144,6,'Denti vs Trenitalia','MAT',1,3876),(145,6,'Ricerca Operativa','MAT',1,3877),(146,6,'Linguaggi','MAT',1,3878);
/*!40000 ALTER TABLE `teachings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_subscriptions`
--

DROP TABLE IF EXISTS `users_subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_subscriptions` (
  `users_mail` varchar(255) NOT NULL,
  `subscriptions_id` int(11) NOT NULL,
  UNIQUE KEY `subscriptions_id` (`subscriptions_id`),
  KEY `FK37B3FCFF7377D0D5` (`subscriptions_id`),
  KEY `FK37B3FCFFEE435BAA` (`users_mail`),
  CONSTRAINT `FK37B3FCFF7377D0D5` FOREIGN KEY (`subscriptions_id`) REFERENCES `subscriptions` (`id`),
  CONSTRAINT `FK37B3FCFFEE435BAA` FOREIGN KEY (`users_mail`) REFERENCES `users` (`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_subscriptions`
--

LOCK TABLES `users_subscriptions` WRITE;
/*!40000 ALTER TABLE `users_subscriptions` DISABLE KEYS */;
INSERT INTO `users_subscriptions` VALUES ('alessandro.montanar5@studio.unibo.it',14),('lorena.qendro@studio.unibo.it',15);
/*!40000 ALTER TABLE `users_subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contents`
--

DROP TABLE IF EXISTS `contents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contents` (
  `type` varchar(31) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contentType` varchar(255) NOT NULL,
  `creationDate` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `modificationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `title` varchar(255) NOT NULL,
  `body` longtext,
  `format` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `creator_mail` varchar(255) NOT NULL,
  `modifier_mail` varchar(255) NOT NULL,
  `parentContent_id` int(11) DEFAULT NULL,
  `root_id` int(11) DEFAULT NULL,
  `editor_mail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKDE2F5B1A2B31D68` (`root_id`),
  KEY `FKDE2F5B1A9C5E9669` (`creator_mail`),
  KEY `FKDE2F5B1A43DAEE7D` (`parentContent_id`),
  KEY `FKDE2F5B1AC0E895C8` (`editor_mail`),
  KEY `FKDE2F5B1AE43C5210` (`parentContent_id`),
  KEY `FKDE2F5B1A90CB069E` (`modifier_mail`),
  CONSTRAINT `FKDE2F5B1A2B31D68` FOREIGN KEY (`root_id`) REFERENCES `contents` (`id`),
  CONSTRAINT `FKDE2F5B1A43DAEE7D` FOREIGN KEY (`parentContent_id`) REFERENCES `contents` (`id`),
  CONSTRAINT `FKDE2F5B1A90CB069E` FOREIGN KEY (`modifier_mail`) REFERENCES `users` (`mail`),
  CONSTRAINT `FKDE2F5B1A9C5E9669` FOREIGN KEY (`creator_mail`) REFERENCES `users` (`mail`),
  CONSTRAINT `FKDE2F5B1AC0E895C8` FOREIGN KEY (`editor_mail`) REFERENCES `users` (`mail`),
  CONSTRAINT `FKDE2F5B1AE43C5210` FOREIGN KEY (`parentContent_id`) REFERENCES `contents` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4090 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contents`
--

LOCK TABLES `contents` WRITE;
/*!40000 ALTER TABLE `contents` DISABLE KEYS */;
INSERT INTO `contents` VALUES ('contents_root',3871,'CONTENTS_ROOT','2012-05-19 13:43:15','','2012-05-19 11:43:15','Scienza delle merendine Contents Root',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',NULL,NULL,'silvano.martello@uunibo.it'),('contents_root',3872,'CONTENTS_ROOT','2012-05-19 13:43:15','','2012-05-19 11:43:15','Scienza delle costruzioni Contents Root',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',NULL,NULL,'silvano.martello@uunibo.it'),('contents_root',3873,'CONTENTS_ROOT','2012-05-19 13:43:15','','2012-05-19 11:43:15','Statistica Contents Root',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',NULL,NULL,'silvano.martello@uunibo.it'),('contents_root',3874,'CONTENTS_ROOT','2012-05-19 13:43:15','','2012-05-19 11:43:15','Fon. Informatica Contents Root',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',NULL,NULL,'enrico.denti@uunibo.it'),('contents_root',3875,'CONTENTS_ROOT','2012-05-19 13:43:15','','2012-05-19 11:43:15','Ingegneria vs Matematica Contents Root',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',NULL,NULL,'enrico.denti@uunibo.it'),('contents_root',3876,'CONTENTS_ROOT','2012-05-19 13:43:15','','2012-05-19 11:43:15','Denti vs Trenitalia Contents Root',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',NULL,NULL,'enrico.denti@uunibo.it'),('contents_root',3877,'CONTENTS_ROOT','2012-05-19 13:43:15','','2012-05-19 11:43:15','Ricerca Operativa Contents Root',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',NULL,NULL,'silvano.martello@uunibo.it'),('contents_root',3878,'CONTENTS_ROOT','2012-05-19 13:43:15','','2012-05-19 11:43:15','Linguaggi Contents Root',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',NULL,NULL,'enrico.denti@uunibo.it'),('category',3879,'CATEGORY','2012-05-19 13:43:15','','2012-05-19 11:43:15','Categoria2 Ricerca Operativa',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3877,3877,NULL),('information',3880,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:15','Informazione1 Ricerca Operativa','',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3879,3877,NULL),('material',3881,'MATERIAL','2012-05-19 13:43:15','','2012-05-19 11:43:15','Materiale2 Ricerca Operativa',NULL,'OTHER','example-files/Interfaccia.gstencil',0,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3879,3877,NULL),('category',3882,'CATEGORY','2012-05-19 13:43:15','','2012-05-19 11:43:15','Categoria1 Ricerca Operativa',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3879,3877,NULL),('material',3883,'MATERIAL','2012-05-19 13:43:15','','2012-05-19 11:43:15','Materiale1 Ricerca Operativa',NULL,'OTHER','example-files/Interfaccia.gstencil',0,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3882,3877,NULL),('category',3884,'CATEGORY','2012-05-19 13:43:15','','2012-05-19 11:43:16','Categoria3 Ricerca Operativa',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3877,3877,NULL),('information',3885,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info0','body of info0',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3886,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info1','body of info1',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3887,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info2','body of info2',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3888,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info3','body of info3',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3889,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info4','body of info4',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3890,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info5','body of info5',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3891,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info6','body of info6',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3892,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info7','body of info7',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3893,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info8','body of info8',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3894,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info9','body of info9',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3895,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info10','body of info10',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3896,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info11','body of info11',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3897,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info12','body of info12',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3898,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info13','body of info13',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3899,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info14','body of info14',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3900,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info15','body of info15',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3901,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info16','body of info16',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3902,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info17','body of info17',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3903,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info18','body of info18',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3904,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info19','body of info19',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3905,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info20','body of info20',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3906,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info21','body of info21',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3907,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info22','body of info22',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3908,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info23','body of info23',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3909,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info24','body of info24',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3910,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info25','body of info25',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3911,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info26','body of info26',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3912,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info27','body of info27',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3913,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info28','body of info28',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3914,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info29','body of info29',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3915,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info30','body of info30',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3916,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info31','body of info31',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3917,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info32','body of info32',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3918,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info33','body of info33',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3919,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info34','body of info34',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3920,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info35','body of info35',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3921,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info36','body of info36',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3922,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info37','body of info37',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3923,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info38','body of info38',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3924,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info39','body of info39',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3925,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info40','body of info40',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3926,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info41','body of info41',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3927,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info42','body of info42',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3928,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info43','body of info43',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3929,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info44','body of info44',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3930,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info45','body of info45',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3931,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info46','body of info46',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3932,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info47','body of info47',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3933,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info48','body of info48',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3934,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info49','body of info49',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3935,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info50','body of info50',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3936,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info51','body of info51',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3937,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info52','body of info52',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3938,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info53','body of info53',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3939,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info54','body of info54',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3940,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info55','body of info55',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3941,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info56','body of info56',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3942,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info57','body of info57',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3943,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info58','body of info58',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3944,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info59','body of info59',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3945,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info60','body of info60',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3946,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info61','body of info61',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3947,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info62','body of info62',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3948,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info63','body of info63',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3949,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info64','body of info64',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3950,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info65','body of info65',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3951,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info66','body of info66',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3952,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info67','body of info67',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3953,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info68','body of info68',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3954,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info69','body of info69',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3955,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info70','body of info70',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3956,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info71','body of info71',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3957,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info72','body of info72',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3958,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info73','body of info73',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3959,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info74','body of info74',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3960,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info75','body of info75',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3961,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info76','body of info76',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3962,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info77','body of info77',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3963,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info78','body of info78',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3964,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info79','body of info79',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3965,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info80','body of info80',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3966,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info81','body of info81',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3967,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info82','body of info82',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3968,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info83','body of info83',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3969,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info84','body of info84',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3970,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info85','body of info85',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3971,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info86','body of info86',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3972,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info87','body of info87',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3973,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info88','body of info88',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3974,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info89','body of info89',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3975,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info90','body of info90',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3976,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info91','body of info91',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3977,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info92','body of info92',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3978,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info93','body of info93',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3979,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info94','body of info94',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3980,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info95','body of info95',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3981,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info96','body of info96',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3982,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info97','body of info97',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3983,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info98','body of info98',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3984,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info99','body of info99',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3985,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info100','body of info100',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3986,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info101','body of info101',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3987,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info102','body of info102',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3988,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info103','body of info103',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3989,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info104','body of info104',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3990,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info105','body of info105',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3991,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info106','body of info106',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3992,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info107','body of info107',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3993,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info108','body of info108',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3994,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info109','body of info109',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3995,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info110','body of info110',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3996,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info111','body of info111',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3997,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info112','body of info112',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3998,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info113','body of info113',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',3999,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info114','body of info114',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4000,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info115','body of info115',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4001,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info116','body of info116',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4002,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info117','body of info117',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4003,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info118','body of info118',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4004,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info119','body of info119',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4005,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info120','body of info120',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4006,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info121','body of info121',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4007,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info122','body of info122',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4008,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info123','body of info123',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4009,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info124','body of info124',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4010,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info125','body of info125',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4011,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info126','body of info126',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4012,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info127','body of info127',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4013,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info128','body of info128',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4014,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info129','body of info129',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4015,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info130','body of info130',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4016,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info131','body of info131',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4017,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info132','body of info132',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4018,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info133','body of info133',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4019,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info134','body of info134',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4020,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info135','body of info135',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4021,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info136','body of info136',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4022,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info137','body of info137',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4023,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info138','body of info138',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4024,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info139','body of info139',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4025,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info140','body of info140',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4026,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info141','body of info141',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4027,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info142','body of info142',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4028,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info143','body of info143',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4029,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info144','body of info144',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4030,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info145','body of info145',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4031,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info146','body of info146',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4032,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info147','body of info147',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4033,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info148','body of info148',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4034,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info149','body of info149',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4035,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info150','body of info150',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4036,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info151','body of info151',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4037,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info152','body of info152',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4038,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info153','body of info153',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4039,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info154','body of info154',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4040,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info155','body of info155',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4041,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info156','body of info156',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4042,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info157','body of info157',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4043,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info158','body of info158',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4044,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info159','body of info159',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4045,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info160','body of info160',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4046,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info161','body of info161',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4047,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info162','body of info162',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4048,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info163','body of info163',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4049,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info164','body of info164',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4050,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info165','body of info165',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4051,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info166','body of info166',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4052,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info167','body of info167',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4053,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info168','body of info168',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4054,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info169','body of info169',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4055,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info170','body of info170',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4056,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info171','body of info171',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4057,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info172','body of info172',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4058,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info173','body of info173',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4059,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info174','body of info174',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4060,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info175','body of info175',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4061,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info176','body of info176',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4062,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info177','body of info177',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4063,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info178','body of info178',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4064,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info179','body of info179',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4065,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info180','body of info180',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4066,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info181','body of info181',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4067,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info182','body of info182',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4068,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info183','body of info183',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4069,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info184','body of info184',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4070,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info185','body of info185',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4071,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info186','body of info186',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4072,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info187','body of info187',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4073,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info188','body of info188',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4074,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info189','body of info189',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4075,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info190','body of info190',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4076,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info191','body of info191',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4077,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info192','body of info192',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4078,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info193','body of info193',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4079,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info194','body of info194',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4080,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info195','body of info195',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4081,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info196','body of info196',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4082,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info197','body of info197',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4083,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info198','body of info198',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('information',4084,'INFORMATION','2012-05-19 13:43:15','','2012-05-19 11:43:16','Info199','body of info199',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3884,3877,NULL),('category',4085,'CATEGORY','2012-05-19 13:43:16','','2012-05-19 11:43:16','Categoria1 Linguaggi',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',3878,3878,NULL),('information',4086,'INFORMATION','2012-05-19 13:43:16','','2012-05-19 11:43:16','Informazione1 Linguaggi','',NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',4085,3878,NULL),('material',4087,'MATERIAL','2012-05-19 13:43:16','','2012-05-19 11:43:16','Materiale2 Linguaggi',NULL,'OTHER','example-files/Interfaccia.gstencil',0,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',4085,3878,NULL),('material',4088,'MATERIAL','2012-05-19 13:43:16','','2012-05-19 11:43:16','Materiale1 Linguaggi',NULL,'OTHER','example-files/Interfaccia.gstencil',0,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',4085,3878,NULL),('category',4089,'CATEGORY','2012-05-19 13:43:16','','2012-05-19 11:43:16','Categoria2 Linguaggi',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',4085,3878,NULL);
/*!40000 ALTER TABLE `contents` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-05-19 13:44:10
