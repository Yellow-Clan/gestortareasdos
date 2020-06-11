CREATE TABLE IF NOT EXISTS parametro (
	id			INTEGER PRIMARY KEY,
	onza		REAL	NOT NULL,
    porc		REAL	NOT NULL,
	ley			REAL	NOT NULL,
	sistema		REAL	NOT NULL,
	tcambio		REAL	NOT NULL,
	precio_do	REAL	NOT NULL,
	precio_so	REAL	NOT NULL,
    last_updated datetime default current_timestamp
);

CREATE TABLE IF NOT EXISTS user (
	id		INTEGER PRIMARY KEY AUTOINCREMENT,
	rol		INTEGER	NOT NULL,
    pin		TEXT	NOT NULL
);

CREATE TABLE IF NOT EXISTS caja_aper_cierre (
	id			INTEGER PRIMARY KEY AUTOINCREMENT,
	fecha		datetime	NOT NULL,
    esaper	INTEGER		NOT	NULL,
	saldo_do	REAL			NULL,
	saldo_so	REAL			NULL,
	saldo_do_bancos	REAL		NULL,
	saldo_so_bancos	REAL		NULL,
	gramos		REAL			NULL,
	user		INTEGER			NULL,
	date_created datetime default current_timestamp,
    last_updated datetime default current_timestamp
);


CREATE TABLE IF NOT EXISTS proveedor (
	id			INTEGER PRIMARY KEY AUTOINCREMENT,
	nombres		TEXT	NOT NULL,
    infoadic	TEXT	NULL,
	fecha_nac	datetime	NULL,
	date_created datetime default current_timestamp,
    last_updated datetime default current_timestamp
);

CREATE TABLE IF NOT EXISTS prove_mov (
	id			INTEGER PRIMARY KEY AUTOINCREMENT,
	fecha		datetime	NOT NULL,
    prove_id	INTEGER		NOT	NULL,
	prove_nom	TEXT			NULL,
	glosa		TEXT			NULL,
	esdolares 	INTEGER		NOT	NULL,
	esadelanto 	INTEGER		NOT	NULL,
	adelanto_do	REAL			NULL,
	adelanto_so	REAL			NULL,
	cobro_do		REAL			NULL,
	cobro_so		REAL			NULL,
	user		INTEGER			NULL,
	activo		INTEGER		default 1,
	date_created datetime default current_timestamp,
    last_updated datetime default current_timestamp,
	FOREIGN KEY (prove_id) REFERENCES proveedor (id) 
	ON UPDATE RESTRICT  ON DELETE RESTRICT 
);

CREATE TABLE IF NOT EXISTS compra (
	id			INTEGER PRIMARY KEY AUTOINCREMENT,
	fecha		datetime	NOT NULL,
    prove_id	INTEGER		NOT	NULL,
	prove_nom	TEXT			NULL,
	cant_gr		REAL		NOT	NULL,
	esdolares 	INTEGER		NOT	NULL,
	
	onza		REAL	NOT NULL,
    porc		REAL	NOT NULL,
	ley			REAL	NOT NULL,
	sistema		REAL	NOT NULL,
	tcambio		REAL	NOT NULL,
	precio_do	REAL	NOT NULL,
	precio_so	REAL	NOT NULL,

	total_do	REAL	NOT NULL,
	total_so	REAL	NOT NULL,
	saldo_do_porpagar	REAL	NULL,
	saldo_so_porpagar	REAL	NULL,
	user		INTEGER			NULL,
	activo		INTEGER		default 1,
	date_created datetime default current_timestamp,
    last_updated datetime default current_timestamp,
	FOREIGN KEY (prove_id) REFERENCES proveedor (id) 
	ON UPDATE RESTRICT ON DELETE RESTRICT
);



CREATE TABLE IF NOT EXISTS cliente (
	id			INTEGER PRIMARY KEY AUTOINCREMENT,
	nombres		VARCHAR(100)	NOT NULL,
    infoadic	VARCHAR(260)	NULL
);
CREATE TABLE IF NOT EXISTS clie_mov (
	id			INTEGER PRIMARY KEY AUTOINCREMENT,
	fecha		datetime	NOT NULL,
    clie_id		INTEGER		NOT	NULL,
	glosa		TEXT			NULL,
	adelanto_do	REAL			NULL,
	adelanto_so	REAL			NULL,
	porcobrar_do		REAL			NULL,
	porcobrar_so		REAL			NULL,
	user		INTEGER			NULL,
	activo		INTEGER		default 1,
	date_created datetime default current_timestamp,
    last_updated datetime default current_timestamp,
	FOREIGN KEY (clie_id) REFERENCES cliente (id) 
	ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS venta (
	id			INTEGER PRIMARY KEY AUTOINCREMENT,
	fecha		datetime	NOT NULL,
    clie_id		INTEGER		NOT	NULL,
	clie_nom	TEXT			NULL,
	cant_gr		REAL		NOT	NULL,
	esdolares 	INTEGER		NOT	NULL,
	tipo_cambio	REAL			NULL,
	precio_do	REAL			NULL,
	precio_so	REAL			NULL,
	total_do	REAL			NULL,
	total_so	REAL			NULL,
	saldo_do_porcobrar	REAL	NULL,
	saldo_so_porcobrar	REAL	NULL,
	user		INTEGER			NULL,
	activo		INTEGER		default 1,
	date_created datetime default current_timestamp,
    last_updated datetime default current_timestamp,
	FOREIGN KEY (clie_id) REFERENCES cliente (id) 
	ON UPDATE RESTRICT ON DELETE RESTRICT
);
