-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `clinicadb` DEFAULT CHARACTER SET utf8 ;
USE `clinicadb`;


-- -----------------------------------------------------
-- Table `Administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Administrador (
    id VARCHAR(20) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    clave VARCHAR(50) NOT NULL
)ENGINE=InnoDB;


-- -----------------------------------------------------
-- Table `Medico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Medico(
    id VARCHAR(20) NOT NULL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL,
    clave VARCHAR(45) NOT NULL,
    especialidad VARCHAR(45) NULL
    ) ENGINE=InnoDB;


-- -----------------------------------------------------
-- Table `Farmaceutico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Farmaceutico (
    id VARCHAR(20) NOT NULL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL,
    clave VARCHAR(45) NOT NULL
    ) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `Paciente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Paciente` (
  `id` VARCHAR(20) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `fechaNacimiento` DATE NULL,
  `telefono` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Medicamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Medicamento` (
  `codigo` VARCHAR(20) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `presentacion` VARCHAR(45) NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table`Receta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Receta` (
  `codigo` INT NOT NULL AUTO_INCREMENT,
  `fechaEmision` DATE NULL,
  `fechaRetiro` DATE NULL,
  `estado` VARCHAR(20) NULL,
  `Paciente_id` VARCHAR(20) NOT NULL,
  `Medico_id` VARCHAR(20) NULL,
  PRIMARY KEY (`codigo`),
  CONSTRAINT `fk_Receta_Paciente1`
    FOREIGN KEY (`Paciente_id`)
    REFERENCES `clinicadb`.`Paciente` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Receta_Medico1`
    FOREIGN KEY (`Medico_id`)
    REFERENCES `clinicadb`.`Medico` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prescripcion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Prescripcion` (
  `codigo` int NOT NULL AUTO_INCREMENT,
  `cantidad` INT NOT NULL,
  `indicaciones` VARCHAR(100) NULL,
  `duracion` INT NULL,
  `Receta_codigo` INT NOT NULL,
  `Medicamento_codigo` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`codigo`),
  CONSTRAINT `fk_Prescripcion_Receta1`
    FOREIGN KEY (`Receta_codigo`)
    REFERENCES `clinicadb`.`Receta` (`codigo`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Prescripcion_Medicamento1`
    FOREIGN KEY (`Medicamento_codigo`)
    REFERENCES `clinicadb`.`Medicamento` (`codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
