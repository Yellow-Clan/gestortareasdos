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
	saldo_bancos_do	REAL		NULL,
	saldo_bancos_so	REAL		NULL,
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
	adelanto_do	REAL			NULL, -- o pago_do al prov lo que le debo (egre)
	adelanto_so	REAL			NULL, -- o pago_so
	cobro_do		REAL			NULL, -- o deposito_do (ingreso)
	cobro_so		REAL			NULL, -- o deposito_so
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
	saldo_porpagar_do	REAL	NULL, -- le voy a deber al prov
	saldo_porpagar_so	REAL	NULL,
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
    clie_nom	TEXT			NULL,
    glosa		TEXT			NULL,
	esdolares 	INTEGER		NOT	NULL,
	escobro 	INTEGER		NOT	NULL,
	
	devol_do	REAL			NULL, -- o abono o devolucion al banco (egreso)
	devol_so	REAL			NULL, -- 
	cobro_do	REAL			NULL, -- o lo que me debe el bnco (ingreso)
	cobro_so	REAL			NULL, -- o o el adelanto que me da el banco

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
	
	onza		REAL	NOT NULL,
    porc		REAL	NOT NULL,
	ley			REAL	NOT NULL,
	sistema		REAL	NOT NULL,
	tcambio		REAL	NOT NULL,
	precio_do	REAL	NOT NULL,
	precio_so	REAL	NOT NULL,

	total_do	REAL	NOT NULL,
	total_so	REAL	NOT NULL,
	saldo_porcobrar_do	REAL	NULL, -- el banco me va deber
	saldo_porcobrar_so	REAL	NULL,
	fecha_depago		datetime	NOT NULL,
	user		INTEGER			NULL,
	activo		INTEGER		default 1,
	date_created datetime default current_timestamp,
    last_updated datetime default current_timestamp,
	FOREIGN KEY (clie_id) REFERENCES cliente (id) 
	ON UPDATE RESTRICT ON DELETE RESTRICT
);
