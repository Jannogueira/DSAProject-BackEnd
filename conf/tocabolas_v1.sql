-- --------------------------------------------------------
-- Host:                         localhost
-- Versión del servidor:         11.7.2-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para tocabolas
CREATE DATABASE IF NOT EXISTS `tocabolas` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE `tocabolas`;

-- Volcando estructura para tabla tocabolas.inventario
CREATE TABLE IF NOT EXISTS `inventario` (
  `ID user` int(11) DEFAULT NULL,
  `ID objeto` int(11) DEFAULT NULL,
  `Cantidad` int(11) DEFAULT NULL,
  KEY `ID user` (`ID user`),
  KEY `ID objeto` (`ID objeto`),
  CONSTRAINT `ID objetos` FOREIGN KEY (`ID objeto`) REFERENCES `items` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ID users` FOREIGN KEY (`ID user`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla tocabolas.inventario: ~0 rows (aproximadamente)

-- Volcando estructura para tabla tocabolas.items
CREATE TABLE IF NOT EXISTS `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `url_icon` varchar(500) DEFAULT NULL,
  `precio` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla tocabolas.items: ~3 rows (aproximadamente)
INSERT INTO `items` (`id`, `nombre`, `descripcion`, `url_icon`, `precio`) VALUES
	(1, 'Bomba', 'Objeto para explotar cualquier bola al a', 'http://dsa2.upc.edu/imagenes/bomba.jpg', 300),
	(2, 'Delete', 'Elimina una simple bola o incluso una bola grande!', 'http://dsa2.upc.edu/imagenes/delete.jpg', 500),
	(3, 'Oro', 'Multiplica x2 tu oro obtenida durante 30 minutos!', 'http://dsa2.upc.edu/imagenes/oro.jpg', 1000);

-- Volcando estructura para tabla tocabolas.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(13) DEFAULT NULL,
  `correo` varchar(30) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `score` int(11) DEFAULT 0,
  `money` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Volcando datos para la tabla tocabolas.users: ~4 rows (aproximadamente)
INSERT INTO `users` (`id`, `usuario`, `correo`, `password`, `score`, `money`) VALUES
	(1, 'Jan', 'jan@gmail.com', '123', 0, 0),
	(2, 'Omar', 'omar@gmail.com', '1234', 0, 0),
	(3, 'Victor', 'victor@gmail.com', '123', 0, 0),
	(4, 'Laura', 'laura@gmail.com', '1234', 0, 0);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
