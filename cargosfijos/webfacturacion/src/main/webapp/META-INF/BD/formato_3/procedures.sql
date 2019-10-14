USE [AMTEL_FSATELITAL]
GO
/****** Object:  StoredProcedure [dbo].[SP_REPORTE_GENERAL_Q01]    Script Date: 19/12/2016 11:56:47 a.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =================================================================================
-- Empresa			:	Americatel Peru S.A
-- Sistema			:	Facturaci�n Internet y Datos Satelital
-- Desarrollado por	:	Frank Or� Orihuela	
-- Fecha Creacion	:	22/02/2016
-- Base de Datos	:	AMTEL_FSATELITAL
-- Invoca a SP		:
-- Invocado por SP	:		
-- =================================================================================
-- Description:	Stored Procedure para la generaci�n del reporte general
-- =================================================================================
-- Modificacion 1
-- Realizado por:
-- Fecha		:
-- Comentarios	:
-- =================================================================================	
-- SP_REPORTE_GENERAL_Q01
-- =================================================================================	

ALTER PROCEDURE [dbo].[SP_REPORTE_GENERAL_Q01]
	

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	/****** Script para el comando SelectTopNRows de SSMS  ******/

	SELECT [NEG].[CO_NEGO] -- NEGOCIO
	  ,[NEG].[CO_NEGO_ORIG]
	  ,[TFACT].[NO_TIPO_FACT]
	  ,[PFACT].[NO_PERI_FACT]
	  ,[NSUC].[CO_NEGO_SUCU]
	  ,ISNULL([NSUC].[CO_OIT_INST],'') [CO_OIT_INST] -- OIT DE INSTALACION
      ,CAST(CASE WHEN [PROD].[CO_PROD] = 1 THEN 'IPB SAT' WHEN [PROD].[CO_PROD] = 2 THEN 'ADD SAT' ELSE '' END AS VARCHAR(20)) [NO_SERV] -- TIPO DE SERVICIO
	  -- ,ISNULL([MED].[NO_PLAN_MEDI_INST],'') [NO_PLAN_MEDI_INST] -- MEDIO
	  ,CAST([CLIE].[DE_CODI_BUM] AS VARCHAR(15)) + '-' +CAST([CLIE].[DE_DIGI_BUM] AS VARCHAR(15)) [CO_CLIE] -- CODIGO CLIENTE
	  ,RTRIM(LTRIM(CASE WHEN [CLIE].[CO_TIPO_DOCU] = 2 THEN ISNULL([CLIE].[AP_CLIE],'') + ' ' + ISNULL([CLIE].[NO_CLIE],'')  ELSE ISNULL([CLIE].[NO_RAZO],'') END)) [NO_CLIE] -- RAZON SOCIAL
      ,[CLIE].[DE_NUME_DOCU] -- RUC
	  ,ISNULL([SUCFIS].[DE_DIRE],'') [DE_DIRE_FISC] -- DIRECCION FISCAL
	  ,ISNULL([SUCU].[DE_DIRE],'') [DE_DIRE_INST]-- DIRECCION DE INSTALACION
	  ,[MONE].[NO_MONE_FACT] -- MONEDA
	 INTO #TMREPO_GRAL_001	   
	FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO] NEG 
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMCLIE] CLIE ON (NEG.CO_CLIE = CLIE.CO_CLIE)
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMSUCU] SUCFIS ON (CLIE.CO_SUCU_FISC = SUCFIS.CO_SUCU)
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMMONE_FACT] MONE ON (NEG.CO_MONE_FACT = MONE.CO_MONE_FACT)
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMPROD] PROD ON (NEG.CO_PROD = PROD.CO_PROD)
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU] NSUC ON (NEG.CO_NEGO = NSUC.CO_NEGO AND NSUC.ST_ELIM=0) -- FILTRAR SOLO LAS SUCURSALES ACTIVAS
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMSUCU] SUCU ON (NSUC.CO_SUCU = SUCU.CO_SUCU)
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMTIPO_FACT] TFACT ON (TFACT.CO_TIPO_FACT=NEG.CO_TIPO_FACT)
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMPERI_FACT] PFACT ON (PFACT.CO_PERI_FACT=NEG.CO_PERI_FACT)
	--LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_PLAN] NSPLAN ON (NSUC.CO_NEGO_SUCU = NSPLAN.CO_NEGO_SUCU)
	--LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMPLAN] PLN ON (NSPLAN.CO_PLAN = PLN.CO_PLAN)
	--LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMPLAN_MEDI_INST] MED ON (PLN.CO_PLAN_MEDI_INST = MED.CO_PLAN_MEDI_INST)
	
		
	-- Agrupamos los registros para evitar duplicados

	SELECT [CO_NEGO]
	    ,[CO_NEGO_ORIG]
		,[NO_TIPO_FACT]
		,[NO_PERI_FACT]
		,MAX([CO_NEGO_SUCU]) [CO_NEGO_SUCU]
		,[CO_OIT_INST]
		,[NO_SERV]
		--,[NO_PLAN_MEDI_INST]
		,[CO_CLIE]
		,[NO_CLIE]
		,[DE_NUME_DOCU]
		,[DE_DIRE_FISC]
		,[DE_DIRE_INST]
		,[NO_MONE_FACT]	
		INTO #TMREPO_GRAL_002
	FROM #TMREPO_GRAL_001
	GROUP BY 
		 [CO_NEGO]
		,[CO_NEGO_ORIG]
		,[NO_TIPO_FACT]
		,[NO_PERI_FACT]
		--,[CO_NEGO_SUCU]
		,[CO_OIT_INST]
		,[NO_SERV]
		--,[NO_PLAN_MEDI_INST]
		,[CO_CLIE]
		,[NO_CLIE]
		,[DE_NUME_DOCU]
		,[DE_DIRE_FISC]
		,[DE_DIRE_INST]
		,[NO_MONE_FACT]	

    -- Actualizamos el medio
	
	ALTER TABLE #TMREPO_GRAL_002 ADD [NO_PLAN_MEDI_INST] VARCHAR(25)

	-- Sucursales con plan activo

	SELECT NSPLAN.CO_NEGO_SUCU
		,ISNULL([MED].[NO_PLAN_MEDI_INST],'') [NO_PLAN_MEDI_INST] 
		INTO #TMNEGO_SUCU_PLAN_001
	FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_PLAN] NSPLAN 
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMPLAN] PLN ON (NSPLAN.CO_PLAN = PLN.CO_PLAN)
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMPLAN_MEDI_INST] MED ON (PLN.CO_PLAN_MEDI_INST = MED.CO_PLAN_MEDI_INST)
	WHERE NSPLAN.FE_FIN IS NULL AND NSPLAN.ST_ELIM=0

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.[NO_PLAN_MEDI_INST] = A.[NO_PLAN_MEDI_INST]
	FROM #TMNEGO_SUCU_PLAN_001 A
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU = A.CO_NEGO_SUCU

	

	-- Sucursales con planes desactivados

	SELECT NSPLAN.CO_NEGO_SUCU
		,ISNULL([MED].[NO_PLAN_MEDI_INST],'') [NO_PLAN_MEDI_INST] 
		INTO #TMNEGO_SUCU_PLAN_002
	FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_PLAN] NSPLAN 
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMPLAN] PLN ON (NSPLAN.CO_PLAN = PLN.CO_PLAN)
	LEFT JOIN [AMTEL_FSATELITAL].[dbo].[TMPLAN_MEDI_INST] MED ON (PLN.CO_PLAN_MEDI_INST = MED.CO_PLAN_MEDI_INST)
	WHERE NSPLAN.FE_FIN IS NOT NULL AND NSPLAN.ST_ELIM=0
	ORDER BY NSPLAN.CO_NEGO_SUCU_PLAN DESC

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.[NO_PLAN_MEDI_INST] = A.[NO_PLAN_MEDI_INST]
	FROM #TMNEGO_SUCU_PLAN_002 A
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU = A.CO_NEGO_SUCU
	AND #TMREPO_GRAL_002.[NO_PLAN_MEDI_INST] IS NULL

	
	-- Actualizamos el estado de la sucursal y servicio

	ALTER TABLE #TMREPO_GRAL_002 ADD NO_ESTA_SUCU VARCHAR(50)


	SELECT [CO_NEGO],[CO_NEGO_SUCU] INTO #TMREPO_GRAL_003
	FROM #TMREPO_GRAL_002
	GROUP BY [CO_NEGO],[CO_NEGO_SUCU]

	ALTER TABLE #TMREPO_GRAL_003 ADD ID INT IDENTITY(1,1)
	ALTER TABLE #TMREPO_GRAL_003 ADD NO_ESTA_SUCU VARCHAR(50)
	
	
	DECLARE @CO_NEGO_SUCU INT
	DECLARE @CO_NEGO INT
	DECLARE @id INT
	DECLARE @i INT
	DECLARE @NO_ESTA_SUCU VARCHAR(50)
	DECLARE @cnt int
	DECLARE @arr int 
	DECLARE @des bit

	SET @i = 1
	SET @CNT = 0
	SET @arr = 0

	SELECT @id = MAX(ID) FROM #TMREPO_GRAL_003

	CREATE TABLE #TMREPO_GRAL_004(
	CO_NEGO INT NOT NULL
	)

	CREATE TABLE #TMREPO_GRAL_005(
	CO_NEGO_SUCU INT NOT NULL,
	IM_MONT DECIMAL(12,2)
	)

	CREATE TABLE #TMREPO_GRAL_006(
	CO_NEGO_SUCU INT NOT NULL,
	[DE_TIPO] VARCHAR(15),
	[IM_VALO] DECIMAL(12,2),
	[DE_PERI_INIC] INT,
    [DE_ANO_INIC] INT,
    [DE_PERI_FIN] INT,
    [DE_ANO_FIN]  INT
	)


	WHILE(@i<=@id)
	BEGIN

		SET @NO_ESTA_SUCU = 'ACTIVO'
		SET @arr = 0
		SET @des = 0
		SET @cnt = 0

		SELECT @CO_NEGO = CO_NEGO,@CO_NEGO_SUCU = CO_NEGO_SUCU FROM #TMREPO_GRAL_003 WHERE ID = @i
		
		/*
		El sistema analiza los estados por el siguiente orden:
		1. Suspendido
		2. Mudado
		3. Desactivado
		*/

		/*Verifica si una sucursal esta suspendida
		Si el contador es igual a cero entonces NO ESTA SUSPENDIDA,
		caso contrario est� SUSPENDIDA.
		*/
		SELECT @cnt = count(*) FROM TMSUSP WHERE CO_NEGO_SUCU= @CO_NEGO_SUCU
		AND FE_FIN IS NULL
		AND ST_ELIM=0;

		IF(@cnt > 0)
		BEGIN
			SET @NO_ESTA_SUCU = 'SUSPENDIDA'

		END

		
		/*Verifica si un negocio se encuentra en estado cortado
		Si el contador es igual a cero entonces NO ESTA CORTADO,
		caso contrario ESTA CORTADO
		*/
		SELECT @cnt = count(*) FROM TMCORT WHERE 
		CO_NEGO=@CO_NEGO AND 
		FE_FIN is null AND ST_ELIM=0;

		IF(@cnt > 0)
		BEGIN
			SET @NO_ESTA_SUCU = 'CORTADO'

		END

		/*Verifica si una sucursal est� desactivada
		Si el contador es igual a cero entonces NO ESTA DESACTIVADA,
		caso contrario esta activa.
		*/

		SELECT @cnt = COUNT(*) FROM TMNEGO_SUCU WHERE CO_NEGO_SUCU=@CO_NEGO_SUCU
		AND FE_FIN IS NOT NULL AND ST_ELIM=0;

		IF(@cnt > 0)
		BEGIN
			SET @NO_ESTA_SUCU = 'DESACTIVADA'

		END

		/*ESTADOS DEL NEGOCIO
		Si es uno entonces el negocio esta desactivado,
		caso contrario esta activo
		
		SELECT @des = CAST(CASE WHEN COUNT(*)=0 AND (select COUNT(*) from TMMIGR_NEGO_SUCU WHERE CO_NEGO_ORIG= @CO_NEGO)>0 THEN 1 
		WHEN COUNT(*)=0 THEN 0
		WHEN (SELECT COUNT(*) FROM TMNEGO_SUCU WHERE CO_NEGO= @CO_NEGO AND ST_ELIM=0 AND FE_FIN IS NULL)>0 THEN 0 
		ELSE 1 
		END AS bit) 
		FROM TMNEGO_SUCU WHERE CO_NEGO=@CO_NEGO AND ST_ELIM=0;

		IF(@des = 1)
		BEGIN
			SET @NO_ESTA_SUCU = 'DESACTIVADA'					

		END
		*/
		UPDATE #TMREPO_GRAL_003
		SET NO_ESTA_SUCU = @NO_ESTA_SUCU
		WHERE ID = @i


		-- Verificamos si el negocio tiene arrendamiento

		SELECT @arr =  COUNT(*)
		FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_PLAN]
		WHERE [NO_PLAN]  LIKE '%ARRENDAMIENTO%'
		AND CO_NEGO_SUCU= @CO_NEGO_SUCU AND ST_ELIM=0

		IF(@arr>0)
		BEGIN

		INSERT INTO #TMREPO_GRAL_004 (CO_NEGO)
		VALUES (@CO_NEGO)
		
		END

		-- Importe Mensual

		INSERT INTO #TMREPO_GRAL_005(CO_NEGO_SUCU,IM_MONT)
		SELECT TOP 1 [CO_NEGO_SUCU]
			,[IM_MONTO]
		FROM [dbo].[TMNEGO_SUCU_PLAN]
		WHERE CO_NEGO_SUCU= @CO_NEGO_SUCU AND ST_ELIM=0
		ORDER BY CO_NEGO_SUCU_PLAN DESC

		-- Promociones

		INSERT INTO #TMREPO_GRAL_006
		SELECT TOP 1
		   [CO_NEGO_SUCU]
		  ,CASE WHEN [DE_TIPO] = 1 THEN 'PORCENTAJE' WHEN [DE_TIPO] = 2 THEN 'MONTO' ELSE '' END [DE_TIPO]
		  ,[IM_VALO]
		  ,[DE_PERI_INIC]
		  ,[DE_ANO_INIC]
		  ,[DE_PERI_FIN]
		  ,[DE_ANO_FIN]   
		FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_PROM]
		WHERE CO_NEGO_SUCU= @CO_NEGO_SUCU
		ORDER BY [DE_PERI_INIC],[DE_ANO_INIC] DESC

	
		SET @i = @i + 1

	END

	

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.NO_ESTA_SUCU = A.NO_ESTA_SUCU
	FROM #TMREPO_GRAL_003 A
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU = A.CO_NEGO_SUCU
	
	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.NO_SERV = #TMREPO_GRAL_002.NO_SERV + ' - BU' 
	WHERE #TMREPO_GRAL_002.CO_NEGO IN (SELECT DISTINCT CO_NEGO FROM #TMREPO_GRAL_004)
	
	
	-- Calculamos el Saldo Pendiente

	ALTER TABLE #TMREPO_GRAL_002 ADD IM_SALD DECIMAL(12,2)
	/*
	SELECT [CO_NEGO]
      ,SUM([IM_MONT]) [IM_SALD]   
	INTO #TMNEGO_SALD_002
	FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SALD]
	WHERE [ST_ELIM] = 0
	GROUP BY [CO_NEGO]
	*/
	SELECT [CO_NEGO]
      ,[IM_MONT] [IM_SALD]
	INTO #TMNEGO_SALD_002
	FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SALD]
	WHERE [ST_ELIM] = 0 AND 
	CO_CIER IN (SELECT CO_CIER 
	FROM TMCIER WHERE CO_PROD IN 
	(SELECT CO_PROD FROM TMNEGO 
	WHERE CO_NEGO=[CO_NEGO]) AND ST_CIER<>4 AND ST_ELIM=0)

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.[IM_SALD] = 0

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.[IM_SALD] = A.[IM_SALD]
	FROM #TMNEGO_SALD_002 A
	WHERE #TMREPO_GRAL_002.CO_NEGO = A.CO_NEGO

	-- Calculamos el Saldo Pendiente Aproximado

	ALTER TABLE #TMREPO_GRAL_002 ADD IM_SALD_APROX DECIMAL(12,2)

	SELECT n.CO_NEGO,SUM(ISNULL(nsp.IM_SALD_APROX,0)) [IM_SALD_APROX] 
	INTO #TMNEGO_SALD_APROX_001
	FROM TMNEGO n 
	INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO 
	INNER JOIN TMNEGO_SUCU_PLAN nsp ON nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU 
	WHERE ns.ST_ELIM=0 AND nsp.ST_ELIM=0 -- 32/11/2016
	-- WHERE nsp.FE_FIN IS NULL
	GROUP BY n.CO_NEGO
	UNION 
	SELECT n.CO_NEGO,SUM(ISNULL(nss.IM_SALD_APROX,0)) [IM_SALD_APROX] 
	FROM TMNEGO n 
	INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO          
	INNER JOIN TMNEGO_SUCU_SERV_SUPL nss ON nss.CO_NEGO_SUCU=ns.CO_NEGO_SUCU 
	WHERE ns.ST_ELIM=0 AND nss.ST_ELIM=0 -- 32/11/2016
	-- WHERE nss.FE_FIN IS NULL
	GROUP BY n.CO_NEGO
	ORDER BY n.CO_NEGO

	SELECT CO_NEGO,SUM(ISNULL(IM_SALD_APROX,0)) [IM_SALD_APROX] 
	INTO #TMNEGO_SALD_APROX_002
	FROM #TMNEGO_SALD_APROX_001 
	GROUP BY CO_NEGO
	ORDER BY CO_NEGO

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.[IM_SALD_APROX] = 0

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.[IM_SALD_APROX] = A.[IM_SALD_APROX]
	FROM #TMNEGO_SALD_APROX_002 A
	WHERE #TMREPO_GRAL_002.CO_NEGO = A.CO_NEGO



	-- Actualizamos el monto de instalaci�n

	ALTER TABLE #TMREPO_GRAL_002 ADD IM_MONT_INST DECIMAL(12,2)

	SELECT [CO_NEGO_SUCU]
		,[IM_MONTO] INTO #TMSERV_UNIC_SUPL_001
	FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_SERV_UNIC] 
	WHERE NO_SERV_UNIC LIKE '%INSTALACI%' AND ST_ELIM=0 -- 23/11/2016
	UNION
	SELECT [CO_NEGO_SUCU]
		,[IM_MONTO]
	FROM  [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_SERV_SUPL]
	WHERE NO_SERV_SUPL LIKE '%INSTALACI%' AND ST_ELIM=0 -- 23/11/2016
	
	
	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.IM_MONT_INST = A.[IM_MONTO]
	FROM (SELECT [CO_NEGO_SUCU]
			,SUM([IM_MONTO]) [IM_MONTO]
		FROM #TMSERV_UNIC_SUPL_001
		GROUP BY [CO_NEGO_SUCU]) A
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU = A.CO_NEGO_SUCU


	-- Actualizamos el monto del servicio mensual

	ALTER TABLE #TMREPO_GRAL_002 ADD IM_MONT_MENS DECIMAL(12,2)

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.IM_MONT_MENS = A.[IM_MONT]
	FROM #TMREPO_GRAL_005 A
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU = A.CO_NEGO_SUCU
	


	-- Actualizamos el monto del Alquiler de equipos

	ALTER TABLE #TMREPO_GRAL_002 ADD IM_MONT_ALQU DECIMAL(12,2)

	SELECT [CO_NEGO_SUCU]
		,[IM_MONTO] INTO #TMSERV_UNIC_SUPL_002
	FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_SERV_UNIC] 
	WHERE NO_SERV_UNIC LIKE '%ALQUILER%' AND ST_ELIM=0 -- 23/11/2016
	UNION
	SELECT [CO_NEGO_SUCU]
		,[IM_MONTO]
	FROM  [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_SERV_SUPL]
	WHERE NO_SERV_SUPL LIKE '%ALQUILER%' AND ST_ELIM=0 -- 23/11/2016

	
	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.IM_MONT_ALQU = A.[IM_MONTO]
	FROM (SELECT [CO_NEGO_SUCU]
			,SUM([IM_MONTO]) [IM_MONTO]
		FROM #TMSERV_UNIC_SUPL_002
		GROUP BY [CO_NEGO_SUCU]) A
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU = A.CO_NEGO_SUCU

	-- Actualizamos Otros Montos

	ALTER TABLE #TMREPO_GRAL_002 ADD IM_MONT_OTRO DECIMAL(12,2)

	SELECT [CO_NEGO_SUCU]
		,[IM_MONTO] INTO #TMSERV_UNIC_SUPL_003
	FROM [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_SERV_UNIC] 
	WHERE NO_SERV_UNIC NOT LIKE '%INSTALACI%'
	AND NO_SERV_UNIC NOT LIKE '%ALQUILER%'
	AND ST_ELIM=0 -- 23/11/2016
	UNION
	SELECT [CO_NEGO_SUCU]
		,[IM_MONTO]
	FROM  [AMTEL_FSATELITAL].[dbo].[TMNEGO_SUCU_SERV_SUPL]
	WHERE NO_SERV_SUPL NOT LIKE '%INSTALACI%'
	AND NO_SERV_SUPL NOT LIKE '%ALQUILER%'
	AND ST_ELIM=0 -- 23/11/2016

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.IM_MONT_OTRO = A.[IM_MONTO]
	FROM (SELECT [CO_NEGO_SUCU]
			,SUM([IM_MONTO]) [IM_MONTO]
		FROM #TMSERV_UNIC_SUPL_003
		GROUP BY [CO_NEGO_SUCU]) A
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU = A.CO_NEGO_SUCU



	-- Actualizamos si es Punto Nuevo
	
	ALTER TABLE #TMREPO_GRAL_002 ADD IS_NUEV CHAR(2)

	SELECT [CO_NEGO_SUCU] INTO #TMNEGO_NUEV_003
	FROM [dbo].[TMNEGO_SUCU_PLAN]
	WHERE FE_ULTI_FACT IS NOT NULL 
	AND ST_ELIM=0 -- 23/11/2016
	UNION
	SELECT [CO_NEGO_SUCU]
	FROM [dbo].[TMNEGO_SUCU_SERV_SUPL]
	WHERE FE_ULTI_FACT IS NOT NULL
	AND ST_ELIM=0 -- 23/11/2016
	GROUP BY [CO_NEGO_SUCU]

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.IS_NUEV = 'SI'

	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.IS_NUEV = 'NO'
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU IN 
		(SELECT CO_NEGO_SUCU FROM #TMNEGO_NUEV_003)

		
	-- Actualizamos Promociones

	ALTER TABLE #TMREPO_GRAL_002 ADD IM_PROM DECIMAL(12,2)
	ALTER TABLE #TMREPO_GRAL_002 ADD [DE_TIPO] VARCHAR(15)
	ALTER TABLE #TMREPO_GRAL_002 ADD [DE_PERI_INIC] INT
	ALTER TABLE #TMREPO_GRAL_002 ADD [DE_ANO_INIC] INT
	ALTER TABLE #TMREPO_GRAL_002 ADD [DE_PERI_FIN] INT
	ALTER TABLE #TMREPO_GRAL_002 ADD [DE_ANO_FIN] INT

	
	UPDATE #TMREPO_GRAL_002
	SET #TMREPO_GRAL_002.[DE_TIPO] = A.[DE_TIPO]
		,#TMREPO_GRAL_002.[IM_PROM] = A.[IM_VALO]
		,#TMREPO_GRAL_002.[DE_PERI_INIC] = A.[DE_PERI_INIC]
		,#TMREPO_GRAL_002.[DE_ANO_INIC] = A.[DE_ANO_INIC]
		,#TMREPO_GRAL_002.[DE_PERI_FIN] = A.[DE_PERI_FIN]
		,#TMREPO_GRAL_002.[DE_ANO_FIN] = A.[DE_ANO_FIN]
	FROM #TMREPO_GRAL_006 A
	WHERE #TMREPO_GRAL_002.CO_NEGO_SUCU = A.CO_NEGO_SUCU 
	


	-- Retornamos la informaic�n

	SELECT 
		CO_NEGO
		,CO_NEGO_ORIG
		,NO_TIPO_FACT
		,NO_PERI_FACT
		,CO_OIT_INST
		,NO_SERV
		,NO_PLAN_MEDI_INST
		,CO_CLIE
		,NO_CLIE
		,DE_NUME_DOCU
		,DE_DIRE_FISC
		,DE_DIRE_INST
		,NO_ESTA_SUCU
		,NO_MONE_FACT		
		,IM_SALD
		,IM_SALD_APROX
		,IM_MONT_INST
		,IM_MONT_MENS
		,IM_MONT_ALQU
		,IM_MONT_OTRO
		,IS_NUEV
		,[IM_PROM]
		,[DE_TIPO]
		,[DE_PERI_INIC]
		,[DE_ANO_INIC]
		,[DE_PERI_FIN]
		,[DE_ANO_FIN]
		FROM #TMREPO_GRAL_002

END