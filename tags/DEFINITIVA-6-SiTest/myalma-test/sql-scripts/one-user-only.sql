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
INSERT INTO `users` VALUES ('User','enrico.denti@uunibo.it','Enrico','c44386c0d8a91e6dd5ec8dcc9120c23a','Denti');
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
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `changeType` varchar(255) DEFAULT NULL,
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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-05-16 14:18:48
