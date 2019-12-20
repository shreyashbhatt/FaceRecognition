--<ScriptOptions statementTerminator=";"/>

CREATE TABLE registration (
	First_Name VARCHAR(30),
	Last_Name VARCHAR(30),
	email VARCHAR(50) NOT NULL,
	address VARCHAR(120),
	password VARCHAR(20),
	PRIMARY KEY (email)
) ENGINE=InnoDB;

CREATE TABLE user_database (
	image_path VARCHAR(100),
	image_name VARCHAR(40)
) ENGINE=InnoDB;

CREATE TABLE login (
	email VARCHAR(50),
	password VARCHAR(20)
) ENGINE=InnoDB;

CREATE INDEX fk_email ON login (email ASC);

