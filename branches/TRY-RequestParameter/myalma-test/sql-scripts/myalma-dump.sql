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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (24,'admin'),(18,'professor'),(25,'student');
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
INSERT INTO `subscriptions` VALUES (12,137),(13,137);
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
INSERT INTO `contents_users` VALUES (3329,'paolo.bellavista@uunibo.it'),(3330,'giuseppe.bellavia@uunibo.it'), (3329,'enrico.denti@uunibo.it');
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
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
INSERT INTO `users_roles` VALUES ('enrico.denti@uunibo.it',18),('giuseppe.bellavia@uunibo.it',18),('paolo.bellavista@uunibo.it',18),('silvano.martello@uunibo.it',18),('alessandro.montanar5@studio.unibo.it',24),('alessandro.montanar5@studio.unibo.it',25),('lorena.qendro@studio.unibo.it',25),('luca.gialli@sstudio.unibo.it',25),('marco.verdi@sstudio.unibo.it',25),('mario.rossi@sstudio.unibo.it',25),('piero.bianchi@sstudio.unibo.it',25);
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
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teachings`
--

LOCK TABLES `teachings` WRITE;
/*!40000 ALTER TABLE `teachings` DISABLE KEYS */;
INSERT INTO `teachings` VALUES (131,6,'Scienza delle merendine','INF',4,3323),(132,6,'Scienza delle costruzioni','MAT',3,3324),(133,6,'Statistica','MAT',1,3325),(134,6,'Fon. Informatica','MAT',1,3326),(135,6,'Ingegneria vs Matematica','MAT',1,3327),(136,6,'Denti vs Trenitalia','MAT',1,3328),(137,6,'Ricerca Operativa','MAT',7,3329),(138,6,'Linguaggi','MAT',1,3330);
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
INSERT INTO `users_subscriptions` VALUES ('alessandro.montanar5@studio.unibo.it',12),('lorena.qendro@studio.unibo.it',13);
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
) ENGINE=InnoDB AUTO_INCREMENT=3871 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contents`
--

LOCK TABLES `contents` WRITE;
/*!40000 ALTER TABLE `contents` DISABLE KEYS */;
INSERT INTO `contents` VALUES ('contents_root',3323,'CONTENTS_ROOT','2012-03-21 22:56:19','','2012-03-21 21:56:19','Scienza delle merendine Contents Root',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',NULL,NULL,'silvano.martello@uunibo.it'),('contents_root',3324,'CONTENTS_ROOT','2012-03-21 22:56:19','','2012-03-21 21:56:19','Scienza delle costruzioni Contents Root',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',NULL,NULL,'silvano.martello@uunibo.it'),('contents_root',3325,'CONTENTS_ROOT','2012-03-21 22:56:19','','2012-03-21 21:56:19','Statistica Contents Root',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',NULL,NULL,'silvano.martello@uunibo.it'),('contents_root',3326,'CONTENTS_ROOT','2012-03-21 22:56:19','','2012-03-21 21:56:19','Fon. Informatica Contents Root',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',NULL,NULL,'enrico.denti@uunibo.it'),('contents_root',3327,'CONTENTS_ROOT','2012-03-21 22:56:19','','2012-03-21 21:56:19','Ingegneria vs Matematica Contents Root',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',NULL,NULL,'enrico.denti@uunibo.it'),('contents_root',3328,'CONTENTS_ROOT','2012-03-21 22:56:19','','2012-03-21 21:56:19','Denti vs Trenitalia Contents Root',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',NULL,NULL,'enrico.denti@uunibo.it'),('contents_root',3329,'CONTENTS_ROOT','2012-03-21 22:56:19','','2012-03-21 21:56:19','Ricerca Operativa Contents Root',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',NULL,NULL,'silvano.martello@uunibo.it'),('contents_root',3330,'CONTENTS_ROOT','2012-03-21 22:56:19','','2012-03-21 21:56:19','Linguaggi Contents Root',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',NULL,NULL,'enrico.denti@uunibo.it'),('category',3331,'CATEGORY','2012-03-21 22:56:19','','2012-03-21 21:56:19','Categoria2 Ricerca Operativa',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3329,3329,NULL),('category',3334,'CATEGORY','2012-03-21 22:56:19','','2012-05-09 21:48:02','Categoria1 Ricerca Operativa',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3331,3329,NULL),('category',3336,'CATEGORY','2012-03-21 22:56:19','','2012-03-21 21:56:20','Categoria3 Ricerca Operativa',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3329,3329,NULL),('category',3537,'CATEGORY','2012-03-21 22:56:20','','2012-03-21 21:56:20','Categoria1 Linguaggi',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',3330,3330,NULL),('category',3541,'CATEGORY','2012-03-21 22:56:20','','2012-03-21 21:56:20','Categoria2 Linguaggi',NULL,NULL,NULL,NULL,'enrico.denti@uunibo.it','enrico.denti@uunibo.it',3537,3330,NULL),('category',3849,'CATEGORY','2012-05-10 09:02:37','dasdsadasdas','2012-05-10 07:02:49','Categoria1',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3323,3323,NULL),('category',3850,'CATEGORY','2012-05-16 13:59:25','Descrizione','2012-05-16 11:59:39','Categoria2',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3323,3323,NULL),('category',3851,'CATEGORY','2012-05-16 13:59:49','Descrizione','2012-05-16 11:59:59','Categoria1bis',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3849,3323,NULL),('category',3852,'CATEGORY','2012-05-16 14:00:02','Descrizione','2012-05-16 12:00:15','Categoria1bisbis',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3851,3323,NULL),('information',3853,'INFORMATION','2012-05-16 14:00:29','Descrizione','2012-05-16 12:01:35','Informazione','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. Ut suscipit faucibus nunc lobortis bibendum. Integer tincidunt dolor ut lectus ultricies vulputate. Aliquam erat volutpat. Integer posuere aliquet posuere. In venenatis enim quis quam rhoncus condimentum vehicula libero ultrices. Cras lacinia leo a purus vehicula semper vel vitae justo. Quisque nisl magna, aliquam vitae rhoncus et, venenatis et massa.\r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3850,3323,NULL),('information',3854,'NOTICE','2012-05-16 14:01:56','Descrizione','2012-05-16 12:02:18','Notizia','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. Ut suscipit faucibus nunc lobortis bibendum. Integer tincidunt dolor ut lectus ultricies vulputate. Aliquam erat volutpat. Integer posuere aliquet posuere. In venenatis enim quis quam rhoncus condimentum vehicula libero ultrices. Cras lacinia leo a purus vehicula semper vel vitae justo. Quisque nisl magna, aliquam vitae rhoncus et, venenatis et massa.\r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3852,3323,NULL),('category',3855,'CATEGORY','2012-05-16 14:02:50','Descrizione','2012-05-16 12:02:58','Categoria',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3324,3324,NULL),('information',3856,'INFORMATION','2012-05-16 14:03:12','Descrizione','2012-05-16 12:03:25','Informazione1','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. Ut suscipit faucibus nunc lobortis bibendum. Integer tincidunt dolor ut lectus ultricies vulputate. Aliquam erat volutpat. Integer posuere aliquet posuere. In venenatis enim quis quam rhoncus condimentum vehicula libero ultrices. Cras lacinia leo a purus vehicula semper vel vitae justo. Quisque nisl magna, aliquam vitae rhoncus et, venenatis et massa.\r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3331,3329,NULL),('information',3857,'INFORMATION','2012-05-16 14:03:29','Descrizione','2012-05-16 12:03:41','Informazione2','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. Ut suscipit faucibus nunc lobortis bibendum. Integer tincidunt dolor ut lectus ultricies vulputate. Aliquam erat volutpat. Integer posuere aliquet posuere. In venenatis enim quis quam rhoncus condimentum vehicula libero ultrices. Cras lacinia leo a purus vehicula semper vel vitae justo. Quisque nisl magna, aliquam vitae rhoncus et, venenatis et massa.\r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3331,3329,NULL),('information',3858,'INFORMATION','2012-05-16 14:03:44','Descrizione','2012-05-16 12:03:59','Informazione3','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. Ut suscipit faucibus nunc lobortis bibendum. Integer tincidunt dolor ut lectus ultricies vulputate. Aliquam erat volutpat. Integer posuere aliquet posuere. In venenatis enim quis quam rhoncus condimentum vehicula libero ultrices. Cras lacinia leo a purus vehicula semper vel vitae justo. Quisque nisl magna, aliquam vitae rhoncus et, venenatis et massa.\r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3331,3329,NULL),('material',3859,'MATERIAL','2012-05-16 14:04:15','Descrizione','2012-05-16 12:04:33','Materiale',NULL,'TEXT','./server/default/data/myalma/README.txt',2551,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3334,3329,NULL),('material',3860,'MATERIAL','2012-05-16 14:04:49','Descrizione','2012-05-16 12:05:04','Materiale2',NULL,'TEXT','./server/default/data/myalma/LICENSE.txt',26530,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3334,3329,NULL),('material',3861,'MATERIAL','2012-05-16 14:05:08','Descrizione','2012-05-16 12:05:26','Materiale3',NULL,'TEXT','./server/default/data/myalma/copyright.txt',6135,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3334,3329,NULL),('information',3862,'NOTICE','2012-05-16 14:05:36','Descrizione','2012-05-16 12:05:51','Notizia','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. \r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3336,3329,NULL),('category',3863,'CATEGORY','2012-05-16 14:06:04','Descrizione','2012-05-16 12:06:32','Categoria4',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3329,3329,NULL),('category',3864,'CATEGORY','2012-05-16 14:08:35','Descrizione','2012-05-16 12:08:44','DA ELIMINARE',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3325,3325,NULL),('category',3865,'CATEGORY','2012-05-16 14:09:04','','2012-05-16 12:09:11','Categoria',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3864,3325,NULL),('information',3866,'INFORMATION','2012-05-16 14:09:14','Descrizione','2012-05-16 12:09:29','Informazione','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. Ut suscipit faucibus nunc lobortis bibendum. Integer tincidunt dolor ut lectus ultricies vulputate. Aliquam erat volutpat. Integer posuere aliquet posuere. In venenatis enim quis quam rhoncus condimentum vehicula libero ultrices. Cras lacinia leo a purus vehicula semper vel vitae justo. Quisque nisl magna, aliquam vitae rhoncus et, venenatis et massa.\r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3865,3325,NULL),('information',3867,'NOTICE','2012-05-16 14:09:32','Descrizione','2012-05-16 12:09:49','Notizia','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. \r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3865,3325,NULL),('material',3868,'MATERIAL','2012-05-16 14:09:53','Descrizione','2012-05-16 12:10:12','Materiale',NULL,'OTHER','./server/default/data/myalma/jar-versions.xml',205077,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3865,3325,NULL),('category',3869,'CATEGORY','2012-05-16 14:10:18','','2012-05-16 12:10:23','Categoria',NULL,NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3325,3325,NULL),('information',3870,'INFORMATION','2012-05-16 14:10:26','Descrizione','2012-05-16 12:10:38','DA ELIMINARE','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pulvinar gravida nulla, et euismod est congue vel. Donec eleifend tempus luctus. Ut suscipit faucibus nunc lobortis bibendum. Integer tincidunt dolor ut lectus ultricies vulputate. Aliquam erat volutpat. Integer posuere aliquet posuere. In venenatis enim quis quam rhoncus condimentum vehicula libero ultrices. Cras lacinia leo a purus vehicula semper vel vitae justo. Quisque nisl magna, aliquam vitae rhoncus et, venenatis et massa.\r\n',NULL,NULL,NULL,'silvano.martello@uunibo.it','silvano.martello@uunibo.it',3869,3325,NULL);
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

-- Dump completed on 2012-05-16 14:18:48
