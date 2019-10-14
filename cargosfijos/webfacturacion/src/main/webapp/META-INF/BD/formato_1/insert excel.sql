USE GAF_FACTURACION;
GO
-- Usuario
INSERT INTO [GAF_FACTURACION].[dbo].[TMPERF]
([NO_PERF]) VALUES 
('Administrador'),
('Facturación'),
('Contratos')
;
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMUSUA]
(CO_PERF,NO_USUA,AP_USUA,DE_CORR,DE_USER,DE_PASS) VALUES
(1,'ADMIN','ADMIN','ADMIN@LOCALHOST.COM','admin',upper(substring(sys.fn_sqlvarbasetostr(hashbytes('MD5','12345')),3,32))),
(2,'Leslie Allison','Rodriguez Crispin','lrodriguez@americatel.com.pe','lrodriguez',upper(substring(sys.fn_sqlvarbasetostr(hashbytes('MD5','12345')),3,32))),
(3,'contratos','contratos','contratos@americatel.com.pe','contratos',upper(substring(sys.fn_sqlvarbasetostr(hashbytes('MD5','12345')),3,32)))
;
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMMODU]
(NO_MODU,DE_PACK) VALUES 
('Administrar de modulos','MODU'),
('Administrar de Items de modulo','ITEM_MODU'),
('Administrar Perfiles','PERF'),
('Administrar Usuarios','USUA'),
('Administrar Tipo de Facturación','TIPO_FACT'),-- 5
('Administrar Moneda Facturación','MONE_FACT'),
('Administrar Periodo Facturación','PERI_FACT'),
('Administrar UBIGEO','UBIGEO'),
('Administrar Tipo de Documento','TIPO_DOCU'),
('Administrar Clientes','CLIE'), -- 10
('Administrar Productos','PROD'),
('Administrar Negocios','NEGO'),
('Administrar Planes','PLAN'),
('Administrar Servicio Suplementario','SERV_SUPL'),
('Administrar Sucursales','SUCU'),-- 15
('Administrar Cortes','CORT'),
('Administrar Suspensiones','SUSP'),
('Administrar Cierres','CIER'),
('Administrar Reportes','REPO'),
('Administrar Servicio Unicos','SERV_UNIC'), -- 20
('Administrar Contactos Cliente','CONT_CLIE') ,
('Administrar Ajustes','AJUS'),
('Procesos Manuales','PROC_MANU')
;
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMITEM_MODU]  
(CO_MODU,NO_ITEM_MODU,DE_PACK) VALUES
(1,'Agregar modulos','MODU.Insertar'),
(1,'Administrar modulos','MODU.Administrar'),
(2,'Agregar item de modulos','ITEM_MODU.Insertar'),
(2,'Administrar item de modulos','ITEM_MODU.Administrar'),
(3,'Agregar Perfiles','PERF.Insertar'),
(3,'Administrar Perfiles','PERF.Administrar'),
(3,'Asignar Permisos','PERF.AsignarPermisos'),
(4,'Agregar Usuarios','USUA.Insertar'),
(4,'Administrar Usuarios','USUA.Administrar'),
(5,'Agregar Tipo Facturación','TIPO_FACT.Insertar'), -- 10
(5,'Administrar Tipo Facturación','TIPO_FACT.Administrar'),
(6,'Agregar Tipo Moneda','MONE_FACT.Insertar'),
(6,'Administrar Tipo Moneda','MONE_FACT.Administrar'),
(7,'Agregar Periodo Facturación','PERI_FACT.Insertar'),
(7,'Administrar Periodo Facturación','PERI_FACT.Administrar'),
(8,'Agregar Departamento','UBIGEO.InsertarDepartamento'),
(8,'Administrar Departamento','UBIGEO.AdministrarDepartamento'),
(8,'Agregar Provincia','UBIGEO.InsertarProvincia'),
(8,'Administrar Provincia','UBIGEO.AdministrarProvincia'),
(8,'Agregar Distrito','UBIGEO.InsertarDistrito'), -- 20
(8,'Administrar Distrito','UBIGEO.AdministrarDistrito'),
(9,'Agregar Tipo de Documento','TIPO_DOCU.Insertar'),
(9,'Administrar Tipo de Documento','TIPO_DOCU.Administrar'),
(10,'Agregar Cliente','CLIE.Insertar'),
(10,'Administrar Cliente','CLIE.Administrar'),
(11,'Agregar Producto','PROD.Insertar'),
(11,'Administrar Productos','PROD.Administrar'),
(12,'Agregar Negocio','NEGO.Insertar'),-- 28
(12,'Administrar Negocio','NEGO.Administrar'),-- 29
(13,'Agregar Plan','PLAN.Insertar'),-- 30
(13,'Administrar Planes','PLAN.Administrar'),
(14,'Agregar Servicio Suplementario','SERV_SUPL.Insertar'),
(14,'Administrar Servicio Suplementario','SERV_SUPL.Administrar'),
(15,'Agregar Sucursal Cliente','SUCU.Insertar'),
(15,'Administrar Sucursal Cliente','SUCU.Administrar'), -- 35
(16,'Cargar Cortes','CORT.Cargar'),
(16,'Listar Cortes','CORT.Listar'),
(17,'Cargar Suspensiones','SUSP.Cargar'),
(17,'Listar Suspensiones','SUSP.Listar'),
(18,'Lanzar Cierre','CIER.Lanzar'), -- 40
(19,'Reportes de Cierre','REPO.Cierre'),
(18,'Matenimiento Recibos','CIER.MantenimientoRecibos'),
(20,'Agregar Servicio Unicos','SERV_UNIC.Insertar'),
(20,'Administrar Servicio Unicos','SERV_UNIC.Administrar'),
(21,'Agregar Contactos','CONT_CLIE.Insertar'), -- 45
(21,'Administrar Contactos','CONT_CLIE.Administrar') ,
(22,'Administrar Ajustes','AJUS.AdministrarPendientes') -- ,
-- (23,'Desactivación Masiva','PROC_MANU.DesactivacionMasiva')
;

GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMPERF_ITEM_MODU]  
(CO_PERF,CO_ITEM_MODU) VALUES 
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),
(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39),(1,40),(1,41),(1,42),(1,43),(1,44),(1,45),(1,46),(1,47),-- (1,48),

(2,28),(2,29),(2,24),(2,25),(2,30),(2,31),(2,32),(2,33),(2,34),(2,35),(2,36),(2,37),(2,38),(2,39),(2,40),(2,41),(2,42),(2,43),(2,44),(2,45),(2,46),(2,47),-- (2,48),

(3,28),(3,29)
;
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMTIPO_FACT] 
(NO_TIPO_FACT) VALUES 
('Vencida'),('Adelantada');
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMMONE_FACT]
([NO_MONE_FACT],[DE_SIMB]) VALUES 
('SOLES','S/.'),
('USD','$')
;
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMPERI_FACT]
([NO_PERI_FACT]) VALUES 
('Mensual'),
('Trimestral'),
('Semestral'),
('Anual')
;
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMTIPO_CLIE] 
(NO_TIPO_CLIE) VALUES 
('Natural'),('Juridica');
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMTIPO_DOCU]
([NO_TIPO_DOCU]) VALUES 
('RUC'),
('DNI'),
('CARNET EXTRANJERIA'),
('PASAPORTE'),
('NO DOMICILIADO'),
('OTRO DOCUMENTO')
;
GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMPROD]
([NO_PROD],[CO_PERI_FACT],[CO_MONE_FACT],[CO_TIPO_FACT]) VALUES 
('Cargos Fijos Satelital - INTERNET','1','2','1'),
('Cargos Fijos Satelital - DATOS','1','2','1')
-- ('NGN','1','2','1')
;
GO
-- facturacion
INSERT INTO [GAF_FACTURACION].[dbo].[TMCIER]
(CO_PROD,NU_ANO,NU_PERI,ST_CIER,DE_RAIZ_RECI,DE_RAIZ_FACT,FE_EMIS,FE_VENC,NU_TIPO_CAMB) VALUES
(1,2015,8,1,9998852000,2098852000,'2014-08-06','2014-08-20',3.2),
(2,2015,8,1,4412102000,1098852000,'2014-08-06','2014-08-20',3.2)
;

GO
INSERT INTO [GAF_FACTURACION].[dbo].[TMMOTI_DESC]
(NO_MOTI_DESC) VALUES 
('SAC'),('MOROSIDAD'),('CESIÓN CONTRACTUAL'),('MUDANZA'),('INTERNO'),('CAMBIO DE MONEDA'),('OTROS');
