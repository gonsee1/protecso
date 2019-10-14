/*
Ejecutar:
 1) creador.sql
 2) datos_iniciales.sql

*/
USE GAF_FACTURACION_ESCENARIO;

-- Datos compartidos
/*
Para lanzar un cirre de Enero 2016 

-- Producto
1 ==> ('Cargos Fijos Satelital - INTERNET','1','2','1'),
2 ==> ('Cargos Fijos Satelital - DATOS','1','2','1')
---- Moneda
1 ==> ('SOLES','S/.'),
2 ==> ('USD','$')
--- Tipo Facturación
1 ==> ('Vencida')
2 ==> ('Adelantada')
--- Periodo
1 ==> ('Mensual'),
2 ==> ('Trimestral'),
3 ==> ('Semestral'),
4 ==> ('Anual')
*/
INSERT INTO [dbo].[TMPLAN]
	(
		CO_PROD,
		NO_PLAN,
		IM_MONTO,
		CO_MONE_FACT
	)
	values
	(1,'Internet Dolares 250',250,2),
	(1,'Internet soles 500',500,1),
	(1,'Internet dolares 300',300,2),
	(1,'Internet dolares 600',600,2),

	(2,'Datos soles 250',250,1),
	(2,'Datos soles 500',500,1),
	(2,'Datos dolares 300',300,2),
	(2,'Datos dolares 600',600,2)
;
GO
INSERT INTO [dbo].[TMDEPA]
	(
		NO_DEPA
	)
	VALUES 
	(
		'Lima'
	)
;
GO
INSERT INTO [dbo].[TMPROV]
	(
		NO_PROV,
		CO_DEPA
	)
	VALUES 
	(
		'Lima',
		1
	)
;
GO
INSERT INTO [dbo].[TMDIST]
	(
		NO_DIST,
		CO_PROV
	)
	VALUES 
	(
		'Lima',
		1
	)
;
GO
INSERT INTO [dbo].[TMCLIE]
	(
		NO_RAZO,
		NO_CLIE,
		AP_CLIE,
		DE_CODI_BUM,
		DE_DIGI_BUM,
		CO_SUCU_FISC,
		CO_CONT_CLIE_REPR_LEGA,
		CO_TIPO_CLIE,
		CO_TIPO_DOCU,
		DE_NUME_DOCU,
		DE_EJEC,
		DE_SUB_GERE,
		DE_SEGM,
		CO_XLS,
		ST_ELIM,
		CO_USUA_CREO,
		CO_USUA_MODI,
		FH_CREO,
		FH_MODI
	)	
VALUES 
	('Cliente de prueba 1','Cliente de prueba 1','Escenario 1','1','1',NULL,NULL,1,1,'123456',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL) -- id 1
;
GO
INSERT INTO [dbo].[TMSUCU] 
	(
		DE_DIRE,		
		CO_DIST,
		CO_CLIE
	)
	VALUES
	('Av Republica de panana 3505',1,1), -- id 1
	('Aramburu',1,1) -- id 2
;
GO
UPDATE [dbo].[TMCLIE] SET CO_SUCU_FISC=2;
GO
DECLARE @id_temporal INT;
-- *** Escenario 1 - Cortes y reconexiones en el mes X

-- Negocio 10001
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(10001,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(10001,1,'2015-01-01',NULL,0,10001001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,10001001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(10001,1,'2015-12-10','2015-12-20',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,10001;
END

-- Negocio 10002
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(10002,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(10002,1,'2015-01-01',NULL,0,10002001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,10002001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(10002,1,'2015-12-10','2015-12-31',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,10002;
END

-- Negocio 10003
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(10003,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(10003,1,'2015-01-01',NULL,0,10003001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,10003001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(10003,1,'2015-12-10','2016-01-10',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,10003;
END

-- *** Escenario 2 - cortes y suspensiones en el mes X

-- Negocio 20001
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20001,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20001,1,'2015-01-01',NULL,0,20001001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20001001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20001,1,'2015-12-10','2015-12-15',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20001001,'2015-12-19','2015-12-25',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20001;
END

-- Negocio 20002
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20002,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20002,1,'2015-01-01',NULL,0,20002001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20002001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20002,1,'2015-12-10','2015-12-15',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20002001,'2015-12-15','2015-12-25',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20002;
END

-- Negocio 20003
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20003,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20003,1,'2015-01-01',NULL,0,20003001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20003001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20003,1,'2015-12-10','2015-12-20',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20003001,'2015-12-15','2015-12-25',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20003;
END

-- Negocio 20004
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20004,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20004,1,'2015-01-01',NULL,0,20004001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20004001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20004,1,'2015-12-10','2015-12-20',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20004001,'2015-12-10','2015-12-20',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20004;
END

-- Negocio 20005
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20005,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20005,1,'2015-01-01',NULL,0,20005001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20005001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20005,1,'2015-12-10','2015-12-15',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20005001,'2015-12-10','2015-12-20',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20005;
END

-- Negocio 20006
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20006,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20006,1,'2015-01-01',NULL,0,20006001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20006001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20006,1,'2015-12-10','2015-12-15',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20006001,'2015-12-10','2015-12-31',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20006;
END

-- Negocio 20007
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20007,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20007,1,'2015-01-01',NULL,0,20007001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20007001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20007,1,'2015-12-10','2015-12-15',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20007001,'2015-12-10',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20007;
END

-- Negocio 20008
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20008,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20008,1,'2015-01-01',NULL,0,20008001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20008001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20008,1,'2015-12-10','2015-12-15',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20008001,'2015-12-12',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20008;
END

-- Negocio 20009
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20009,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20009,1,'2015-01-01',NULL,0,20009001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20009001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20009,1,'2015-12-10','2015-12-15',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20009001,'2015-12-20',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20009;
END

-- Negocio 20010
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(20010,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(20010,1,'2015-01-01',NULL,0,20010001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,20010001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(20010,1,'2015-12-15','2015-12-20',1,NULL)
;
INSERT INTO [dbo].[TMSUSP]
	(
		CO_NEGO_SUCU,
		CO_PROD,
		ST_SOAR,
		CO_OIT_INST,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(@id_temporal,1,0,20010001,'2015-12-12',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,20010;
END

-- *** Escenario 3 - cortes y bajas en el mes X
-- Negocio 30001 
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(30001,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(30001,1,'2015-01-01',NULL,0,30001001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,30001001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01','2015-12-25','2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(30001,1,'2015-12-10','2015-12-15',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,30001;
END
-- Negocio 30002 
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(30002,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(30002,1,'2015-01-01',NULL,0,30002001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,30002001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01','2015-12-17','2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(30002,1,'2015-12-14','2015-12-25',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,30002;
END

-- Negocio 30003
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(30003,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(30003,1,'2015-01-01',NULL,0,30003001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,30003001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01','2015-12-17','2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(30003,1,'2015-12-17','2015-12-25',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,30003;
END

-- Negocio 30004
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(30004,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(30004,1,'2015-01-01',NULL,0,30004001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,30004001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01','2015-12-17','2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(30004,1,'2015-12-22','2015-12-25',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,30004;
END

-- Negocio 30005
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(30005,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(30005,1,'2015-01-01',NULL,0,30005001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,30005001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01','2015-12-17','2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(30005,1,'2015-12-22',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,30005;
END

-- Negocio 30006
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(30006,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(30006,1,'2015-01-01',NULL,0,30006001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,30006001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01','2015-12-17','2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(30006,1,'2015-12-17',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,30006;
END

-- Negocio 30007
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(30007,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(30007,1,'2015-01-01',NULL,0,30007001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,30007001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01','2015-12-17','2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(30007,1,'2015-12-10',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,30007;
END

--- *** Escenario 4 - Cortes y reconexiones mes X+1


-- Negocio 40001
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(40001,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(40001,1,'2015-01-01',NULL,0,40001001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,40001001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(40001,1,'2015-11-10','2015-11-20',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,40001;
END

-- Negocio 40002
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(40002,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(40002,1,'2015-01-01',NULL,0,40002001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,40002001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(40002,1,'2015-11-10','2015-11-30',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,40002;
END

-- Negocio 40003
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(40003,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(40003,1,'2015-01-01',NULL,0,40003001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,40003001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(40003,1,'2015-11-10','2015-12-25',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,40003;
END


--- *** Escenario 5 - Cortes y reconexiones mes X+2

-- Negocio 50001
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(50001,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(50001,1,'2015-01-01',NULL,0,50001001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,50001001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(50001,1,'2015-10-10','2015-10-15',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,50001;
END

-- Negocio 50002
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(50002,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(50002,1,'2015-01-01',NULL,0,50002001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,50002001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(50002,1,'2015-10-10','2015-10-31',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,50002;
END

-- Negocio 50003
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(50003,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(50003,1,'2015-01-01',NULL,0,50003001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,50003001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(50003,1,'2015-10-10','2015-11-15',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,50003;
END



-- Negocio 50004
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(50004,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(50004,1,'2015-01-01',NULL,0,50004001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,50004001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(50004,1,'2015-10-10','2015-11-30',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,50004;
END

-- Negocio 50005
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(50005,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(50005,1,'2015-01-01',NULL,0,50005001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,50005001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(50005,1,'2015-10-10','2015-12-15',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,50005;
END

--- *** Escenario 6 - Corte sin reconexión

-- Negocio 60001
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(60001,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(60001,1,'2015-01-01',NULL,0,60001001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,60001001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(60001,1,'2015-12-10',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,60001;
END

-- Negocio 60002
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(60002,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(60002,1,'2015-01-01',NULL,0,60002001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,60002001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(60002,1,'2015-11-10',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,60002;
END


-- Negocio 60003
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(60003,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(60003,1,'2015-01-01',NULL,0,60003001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,60003001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(60003,1,'2015-10-10',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,60003;
END


--- *** Escenario 7 - Corte + reconexión + corte en el mes X

-- Negocio 70001
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(70001,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(70001,1,'2015-01-01',NULL,0,70001001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,70001001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(70001,1,'2015-12-08','2015-12-12',1,NULL),
	(70001,1,'2015-12-15','2015-12-20',1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,70001;
END

-- Negocio 70002
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(70002,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(70002,1,'2015-01-01',NULL,0,70002001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,70002001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(70002,1,'2015-12-08','2015-12-12',1,NULL),
	(70002,1,'2015-12-15',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,70002;
END

-- Negocio 70003
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(70003,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(70003,1,'2015-01-01',NULL,0,70003001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,70003001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(70003,1,'2015-12-08','2015-12-12',1,NULL),
	(70003,1,'2015-12-12',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,70003;
END


-- Negocio 70004
BEGIN
INSERT INTO [dbo].[TMNEGO]
	(
		CO_NEGO,
		CO_SUCU_CORR,
		CO_CLIE,
		CO_PROD, -- (INTERNET,DATOS)
		CO_TIPO_FACT, -- (Vencida,Adelantada)
		CO_MONE_FACT,-- (SOLES,DOLARES)
		CO_PERI_FACT-- (Mensual,Trimestral,Semestral,Anual)
	)
	VALUES
	(70004,1,1,1,2,2,1);
;
INSERT INTO [dbo].[TMNEGO_SUCU]
	(
		CO_NEGO,
		CO_SUCU,
		FE_INIC,
		FE_FIN,
		ST_SOAR_INST,
		CO_OIT_INST
	) VALUES
	(70004,1,'2015-01-01',NULL,0,70004001) -- id 1
;
SET @id_temporal=@@IDENTITY;
INSERT INTO [dbo].[TMNEGO_SUCU_PLAN]
	(
		CO_NEGO_SUCU,
		ST_SOAR_INST,
		CO_OIT_INST,
		CO_PLAN,
		NO_PLAN,
		FE_INIC,
		FE_FIN,
		FE_ULTI_FACT,
		IM_MONTO
	)
	VALUES
	(@id_temporal,0,70004001,1,(SELECT NO_PLAN FROM [dbo].[TMPLAN] WHERE CO_PLAN=1),'2015-01-01',NULL,'2015-12-31',(SELECT IM_MONTO FROM [dbo].[TMPLAN] WHERE CO_PLAN=1)) -- id 1
;
INSERT INTO [dbo].[TMCORT]
	(
		CO_NEGO,
		CO_PROD,
		FE_INIC,
		FE_FIN,
		CO_CIER_INIC,
		CO_CIER_FIN
	) VALUES 
	(70004,1,'2015-12-05','2015-12-12',1,NULL),
	(70004,1,'2015-12-08',NULL,1,NULL)
;
EXECUTE  sp_crear_detalle 2015,1,2015,12,70004;
END

