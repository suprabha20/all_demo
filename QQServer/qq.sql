/*
SQLyog Community v9.20 
MySQL - 5.5.17 : Database - qq
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`qq` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `qq`;

/*Table structure for table `_2016` */

DROP TABLE IF EXISTS `_2016`;

CREATE TABLE `_2016` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  `isOnline` varchar(5) NOT NULL,
  `img` int(11) DEFAULT NULL,
  `qq` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `_2016` */

insert  into `_2016`(`id`,`name`,`isOnline`,`img`,`qq`) values (1,'wei','1',2,2017),(2,'qq','1',1,2018);

/*Table structure for table `_2017` */

DROP TABLE IF EXISTS `_2017`;

CREATE TABLE `_2017` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  `isOnline` varchar(5) NOT NULL,
  `img` int(11) DEFAULT NULL,
  `qq` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `_2017` */

insert  into `_2017`(`id`,`name`,`isOnline`,`img`,`qq`) values (1,'way','1',3,2016),(2,'qq','1',1,2018);

/*Table structure for table `_2018` */

DROP TABLE IF EXISTS `_2018`;

CREATE TABLE `_2018` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  `isOnline` varchar(5) NOT NULL,
  `img` int(11) DEFAULT NULL,
  `qq` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `_2018` */

insert  into `_2018`(`id`,`name`,`isOnline`,`img`,`qq`) values (1,'way','1',3,2016),(2,'wei','1',2,2017);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(50) NOT NULL COMMENT '昵称',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `isOnline` int(20) NOT NULL DEFAULT '0' COMMENT '是否在线',
  `img` int(20) DEFAULT '0' COMMENT '用户头像',
  PRIMARY KEY (`id`,`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2019 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`password`,`email`,`isOnline`,`img`) values (2016,'way','202cb962ac59075b964b07152d234b70','1320438999@qq.com',1,3),(2017,'wei','202cb962ac59075b964b07152d234b70','304912256@qq.com',0,2),(2018,'qq','202cb962ac59075b964b07152d234b70','158342219@qq.com',1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
