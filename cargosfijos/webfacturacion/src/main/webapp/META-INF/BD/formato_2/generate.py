#!/usr/bin/python
# -*- coding: utf-8 -*-
import pymssql
import codecs
from dateutil.relativedelta import relativedelta 
import datetime
#conn = pymssql.connect(host='10.17.20.15', user='sa', password='123456', database='EXPORT_EXCEL_DIC_2015', charset='UTF-8')
conn = pymssql.connect(host='10.17.20.16', user='sa', password='admin.123', database='EXPORT_EXCEL_ABRIL_2016', charset='UTF-8')
idDepa={}
idClie={}
idClieNombre={}
idNego={}
idMoneda={'USD':'2','SOLES':'1'}
idTipoFacturacion={'Adelantada':'2','Vencida':'1'}
idProducto={'INTERNET SATELITAL':{'id':1,'planes':{},'sss':{}},'ENLACE DE DATOS':{'id':2,'planes':{},'sss':{}}}
idMedioPlan={}
orden=0
idNego_Sucu_Plan={}
'''

idPlan={}
idMedioPlan={}

idSS={}
ordenTodo=0
'''

def getFile(name,raiz=False):
	global orden
	orden=orden+1
	if raiz:
		return codecs.open(str(orden)+" - "+name, mode='wb', encoding="utf-8", errors='strict', buffering=1)
	return codecs.open("sqls/"+str(orden)+" - "+name, mode='wb', encoding="utf-8", errors='strict', buffering=1)
	#return codecs.open("sqls/"+name, mode='wb', encoding="utf-8", errors='strict', buffering=1)

file_todo=getFile("data_excel.sql")	

def ubigeo():
	cur = conn.cursor()
	cur.execute('''
		SELECT W.DEPARTAMENTO,W.PROVINCIA,W.DISTRITO FROM (
		SELECT DISTRITO,PROVINCIA,DEPARTAMENTO FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] GROUP BY DISTRITO,PROVINCIA,DEPARTAMENTO UNION
		SELECT DISTRITO1 as DISTRITO,PROVINCIA1 as PROVINCIA,DEPARTAMENTO1 as DEPARTAMENTO FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] GROUP BY DISTRITO1,PROVINCIA1,DEPARTAMENTO1 UNION
		SELECT DISTRITO,PROVINCIA,DEPARTAMENTO FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO] GROUP BY DISTRITO,PROVINCIA,DEPARTAMENTO ) AS W
		GROUP BY W.DISTRITO,W.PROVINCIA,W.DEPARTAMENTO
		ORDER BY W.DEPARTAMENTO,W.PROVINCIA,W.DISTRITO;		
	''')

	generate=0
	depa_id=0
	prov_id=0
	dist_id=0
	depa_id_actual=0
	prov_id_actual=0
	dist_id_actual=0
	sql_depa="INSERT INTO TMDEPA (NO_DEPA) VALUES "
	sql_prov="INSERT INTO TMPROV (CO_DEPA,NO_PROV) VALUES "
	sql_dist="INSERT INTO TMDIST (CO_PROV,NO_DIST) VALUES "
	
	for reg in cur.fetchall():
		depa=reg[0].strip()
		prov=reg[1].strip()
		dist=reg[2].strip()
		if not idDepa.has_key(depa):
			depa_id+=1
			depa_id_actual=depa_id
			idDepa[depa]={'id':depa_id,'provs':{}}	
			sql_depa="%s ('%s')," % (sql_depa,depa)
		else:
			depa_id_actual=idDepa[depa]['id']
		
		if not idDepa[depa]['provs'].has_key(prov):
			prov_id+=1
			prov_id_actual=prov_id
			idDepa[depa]['provs'][prov]={'id':prov_id,'dists':{}}
			sql_prov="%s (%s,'%s')," % (sql_prov,depa_id_actual,prov)
		else:
			prov_id_actual=idDepa[depa]['provs'][prov]['id']

		if not idDepa[depa]['provs'][prov]['dists'].has_key(dist):
			dist_id+=1
			idDepa[depa]['provs'][prov]['dists'][dist]={'id':dist_id}
			sql_dist="%s (%s,'%s')," % (sql_dist,prov_id_actual,dist)	
	'''
	for depa in idDepa:
		sql_depa="%s (%s,'%s')," % (sql_depa,idDepa[depa]['id'],depa)
		depa_id=idDepa[depa]['id']
		for prov in idDepa[depa]['provs']:
			sql_prov="%s (%s,%s,'%s')," % (sql_prov,idDepa[depa]['provs'][prov]['id'],depa_id,prov)	
			prov_id=idDepa[depa]['provs'][prov]['id']
			for dist in idDepa[depa]['provs'][prov]['dists']:
				sql_dist="%s (%s,%s,'%s')," % (sql_dist,idDepa[depa]['provs'][prov]['dists'][dist]['id'],prov_id,dist)			
	'''
	cur.close()		
	
	file_todo.write(sql_depa[:-1]+";\r\n")
	file_todo.write(sql_prov[:-1]+";\r\n")
	file_todo.write(sql_dist[:-1]+";\r\n")

def cliente():
	#update ---> falsta suc fiscal, repre legal,
	sql='INSERT INTO TMCLIE (NO_RAZO,DE_CODI_BUM,DE_DIGI_BUM,CO_TIPO_CLIE,CO_TIPO_DOCU,DE_NUME_DOCU,DE_EJEC,DE_SUB_GERE,ST_ELIM) VALUES '
	cur = conn.cursor()
	cur.execute('''
		SELECT 
			(SELECT TOP 1 [CLIENTE ] FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] a WHERE a.[RUC]=x.[RUC]) as cliente,
			(SELECT TOP 1 [CODIGO] FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] a WHERE a.[RUC]=x.[RUC]) as codigo,
			(SELECT TOP 1 [DIGITO VERIFICADOR] FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] a WHERE a.[RUC]=x.[RUC]) as digito,
			(SELECT TOP 1 [RUC] FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] a WHERE a.[RUC]=x.[RUC]) as ruc,
			(SELECT TOP 1 [EJECUTIVO] FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] a WHERE a.[RUC]=x.[RUC]) as ejecutivo,
			(SELECT TOP 1 [SUBGERENTE] FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] a WHERE a.[RUC]=x.[RUC]) as subgerente
		  FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] x
		GROUP BY
			[RUC]		
	''')
	clie_id=0
	for reg in cur.fetchall():
		clie_id+=1
		razo=reg[0].strip()
		codigo=(long)(reg[1])
		digito=str(reg[2]).strip()
		try:
			digito=(float)(digito)
			digito=(int)(round(digito))
		except Exception, e:
			digito='NULL'
		ruc=(long)(reg[3])
		if reg[4]:
			eje=reg[4].strip()	
		else:
			eje=None
		if reg[5]:
			sub=reg[5].strip()
		else:
			sub=None
		
		idClie[ruc]={'id':clie_id,'contactos':{},'sucursales':{}}
		idClieNombre[razo]={'ruc':ruc}
		sql="%s ('%s','%s',%s,2,1,'%s',%s,%s,0)," % (sql,razo,codigo,digito,ruc,"'%s'" % eje if eje else 'null',"'%s'" % sub if sub else 'null')			

	file_todo.write(sql[:-1]+';\r\n')
	
def cliente_contacto():
	sql='INSERT INTO TMCONT_CLIE (CO_CLIE,AP_CONT_CLIE,ST_ELIM) VALUES'
	cur = conn.cursor()
	cur.execute('''
		SELECT
		[RUC],
		[Rep Legal]
		FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE]
		GROUP BY [Rep Legal],[RUC];	
	''')
	cont_id=0
	for reg in cur.fetchall():
		cont_id+=1
		ruc=(long)(reg[0])
		if reg[1]:
			ap=reg[1].strip()
		else:
			ap=None
		clie=idClie[ruc]
		sql="%s ('%s',%s,0)," % (sql,clie['id'],"'%s'" % ap if ap else 'null')
		clie['contactos'][ap]={'id':cont_id}
	file_todo.write(sql[:-1]+';\r\n')

def cliente_sucursal():
	cur = conn.cursor()
	cur.execute('''
		SELECT W.CLIENTE,W.RUC,W.DIRECCION,W.DISTRITO,W.PROVINCIA,W.DEPARTAMENTO FROM 
		(SELECT null as CLIENTE,[RUC],[DIRECCION FISCAL] as DIRECCION,[DISTRITO] as DISTRITO,[PROVINCIA] as PROVINCIA,[DEPARTAMENTO] as DEPARTAMENTO FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] GROUP BY [RUC],[DIRECCION FISCAL],[DISTRITO],[PROVINCIA],[DEPARTAMENTO] UNION
		SELECT null as CLIENTE,[RUC],[DIRECCION CORRESPONDENCA] as DIRECCION,[DISTRITO1] as DISTRITO,[PROVINCIA1] as PROVINCIA,[DEPARTAMENTO1] as DEPARTAMENTO FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] GROUP BY [RUC],[DIRECCION CORRESPONDENCA],[DISTRITO1],[PROVINCIA1],[DEPARTAMENTO1] UNION
		SELECT [CLIENTE],null as RUC,[UBICACION] as DIRECCION,[DISTRITO] as DISTRITO,[PROVINCIA] as PROVINCIA,[DEPARTAMENTO] as DEPARTAMENTO FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO] GROUP BY [CLIENTE],[UBICACION],[DISTRITO],[PROVINCIA],[DEPARTAMENTO]) AS W
		GROUP BY CLIENTE,RUC,W.DIRECCION,W.DISTRITO,W.PROVINCIA,W.DEPARTAMENTO
		ORDER BY CLIENTE,RUC,W.DEPARTAMENTO,W.PROVINCIA,W.DISTRITO
	''')
	suc_id=0
	sql='INSERT INTO TMSUCU (DE_DIRE,CO_DIST,CO_CLIE,ST_ELIM) VALUES '
	#print 'CONTRATISTAS GENERALES EN MINERIA JH S.A.C.' in idClieNombre
	for reg in cur.fetchall():
		suc_id+=1
		clie=None				
		if reg[0]:
			'''
			if not reg[0].strip() in idClieNombre:
				print reg[0].strip() in idClieNombre,reg[0].strip()
			'''
			clie=idClie[idClieNombre[reg[0].strip()]['ruc']]
		elif reg[1]:
			clie=idClie[(long)(reg[1])]

		dire=reg[2].strip().replace('\'','')
		depa=reg[5].strip()
		prov=reg[4].strip()
		dist=reg[3].strip()
		dist_id=idDepa[depa]['provs'][prov]['dists'][dist]['id']		
		sql="%s ('%s','%s','%s',0)," % (sql,dire,dist_id,clie['id'])
		clie['sucursales'][dire+depa+prov+dist]={'id':suc_id}

	file_todo.write(sql[:-1]+';\r\n')


def cliente_update():
	#update ---> falsta suc fiscal, repre legal,
	cur = conn.cursor()
	cur.execute('''
		SELECT 
			[CLIENTE ],
			[CODIGO],
			[DIGITO VERIFICADOR],
			[RUC],
			[EJECUTIVO],
			[SUBGERENTE],
			[DIRECCION FISCAL],
			[DISTRITO],
			[PROVINCIA],
			[DEPARTAMENTO],
			[REP LEGAL]
		  FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE];	
	''')	
	sql=''
	for reg in cur.fetchall():
		razo=reg[0].strip()
		codigo=(long)(reg[1])
		digito=str(reg[2]).strip()
		ruc=(long)(reg[3])
		if reg[4]:
			eje=reg[4].strip()
		else:
			eje=None
		if reg[5]:
			sub=reg[5].strip()	
		else:
			sub=None
		dire=reg[6].strip()	
		dist=reg[7].strip()	
		prov=reg[8].strip()	
		depa=reg[9].strip()	
		if reg[10]:
			rep=reg[10].strip()
		else:
			rep=None

		clie=idClie[ruc];
		sucu_fisc_id=clie['sucursales'][dire+depa+prov+dist]['id']
		if clie['contactos'][rep]['id']:
			repre_id=clie['contactos'][rep]['id']
		else:
			repre_id=None
		sql='%sUPDATE TMCLIE SET CO_CONT_CLIE_REPR_LEGA=%s, CO_SUCU_FISC=%s WHERE CO_CLIE=%s; ' % (sql,"'%s'" % repre_id if repre_id else 'null',sucu_fisc_id,clie['id'])

	file_todo.write(sql+';\r\n')


def negocio():
	cur = conn.cursor()
	cur.execute('''
		SELECT c.RUC,c.NEGOCIO,c.[DIRECCION CORRESPONDENCA],c.DISTRITO1,c.PROVINCIA1,c.DEPARTAMENTO1,sc.SERVICIO,sc.MONEDA, c.[TIPO DE FACTURACION]
		FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE] c
		INNER JOIN [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO] sc ON sc.NEGOCIO=c.[NEGOCIO ]
		GROUP BY c.RUC,c.NEGOCIO,c.[DIRECCION CORRESPONDENCA],c.DISTRITO1,c.PROVINCIA1,c.DEPARTAMENTO1,sc.SERVICIO,sc.MONEDA, c.[TIPO DE FACTURACION];
	''')
	nego_id=0
	sql='INSERT INTO TMNEGO (CO_NEGO,CO_SUCU_CORR,CO_CLIE,CO_PROD,CO_TIPO_FACT,CO_MONE_FACT,CO_PERI_FACT,ST_ELIM) VALUES '

	for reg in cur.fetchall():
		nego_id+=1			
		ruc=(long)(reg[0])
		nego=(long)(reg[1])
		dire=reg[2].strip().replace('\'','')
		dist=reg[3].strip()
		prov=reg[4].strip()
		depa=reg[5].strip()
		serv=reg[6].strip()
		mone=reg[7].strip()
		if (reg[8].strip()=='FACTURACION ADELANTADA'):
			tipo_fact=idTipoFacturacion['Adelantada']
		elif (reg[8].strip()=='FACTURACION VENCIDA'):
			tipo_fact=idTipoFacturacion['Vencida']
		clie=idClie[ruc]
		sucu_corr_id=clie['sucursales'][dire+depa+prov+dist]['id']
		prod_id=idProducto[serv]['id']
		sql="%s ('%s','%s','%s','%s','%s','%s',1,0)," % (sql,nego,sucu_corr_id,clie['id'],prod_id,tipo_fact,idMoneda[mone])

		idNego[nego]={'id':nego_id,'ruc':ruc,'tipo_fact':tipo_fact,'sucursales':{}}
		
	file_todo.write(sql[:-1]+';\r\n')


def negocio_sucursal():
	cur = conn.cursor()
	cur.execute('''
		SELECT 
			 [NEGOCIO ]
			,[UBICACION]
			,[DISTRITO]
			,[PROVINCIA]
			,[DEPARTAMENTO]
			,[Fecha inicio OIT]
			,[Fecha de desact]
			,[OIT INSTALACION]
			,[OIT desact#]
			,[orden servicio]
		FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO]
	''')
	nego_sucu_id=0
	sql='INSERT INTO TMNEGO_SUCU (CO_NEGO,CO_SUCU,FE_INIC,FE_FIN,ST_SOAR_INST,CO_OIT_INST,CO_CIRC,ST_SOAR_DESI,CO_OIT_DESI,DE_ORDE_SERV,ST_ELIM) VALUES '
	for reg in cur.fetchall():
		nego_sucu_id+=1		
		nego=(long)(reg[0])
		dire=reg[1].strip().replace('\'','')
		dist=reg[2].strip()
		prov=reg[3].strip()
		depa=reg[4].strip()		
		fi=reg[5].date()
		ff=reg[6]		
		oit_inst=reg[7].strip().upper()	if reg[7] else reg[7]		
		oit_desa=reg[8].strip().upper()	if reg[8] else reg[8]	
		orden=reg[9].strip()		
		regNego=idNego[nego]
		ruc=regNego['ruc']
		clie=idClie[ruc]
		sucu_id=clie['sucursales'][dire+depa+prov+dist]['id']
		soar_oit_inst=False
		num_oit_inst=None
		if oit_inst:
			if oit_inst.find('S')==-1:
				num_oit_inst=(long)(oit_inst)
				if oit_inst.find('O')==0:
					soar_oit_inst=True
			else:
				num_oit_inst=(long)(oit_inst[1:])

		soar_oit_desi=False
		num_oit_desi=None
		if oit_desa:
			if oit_desa.find('S')==-1:
				num_oit_desi=(long)(oit_desa)
				if oit_desa.find('O')==0:
					soar_oit_inst=True
			else:
				num_oit_desi=(long)(oit_desa[1:])

		regNego['sucursales'][num_oit_inst]={'id':nego_sucu_id}
		sql="%s ('%s','%s','%s',%s,%d,%d,NULL,%s,%s,'%s',0)," % (sql,nego,sucu_id,fi,"'%s'" % ff if ff else 'null', 1 if soar_oit_inst else 0,num_oit_inst,'NULL','NULL',orden)


	file_todo.write(sql[:-1]+';\r\n')

def medio_plan():
	cur = conn.cursor()
	cur.execute('''
		SELECT 
			[MEDIO]
		  FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO]
		  GROUP BY
			[MEDIO]
	''')
	medio_id=0
	sql='INSERT INTO TMPLAN_MEDI_INST (NO_PLAN_MEDI_INST) VALUES '
	for reg in cur.fetchall():
		medio_id+=1			
		nombre=reg[0].strip()
		idMedioPlan[nombre]={'id':medio_id}
		sql="%s ('%s')," % (sql,nombre)

	file_todo.write(sql[:-1]+';\r\n')	
		
def plan():
	cur = conn.cursor()
	cur.execute('''
		SELECT 
			[GLOSA],
			[SERVICIO],
			[MEDIO],
			[IMPORTE ],
			[MONEDA],
		    [VELOCIDAD BAJADA],
		    [VELOCIDAD SUBIDA]
		  FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO]
		  GROUP BY
		  	[GLOSA],
			[SERVICIO],
			[MEDIO],
			[IMPORTE ],
			[MONEDA],
		    [VELOCIDAD BAJADA],
		    [VELOCIDAD SUBIDA]
	''')
	plan_id=0
	sql='INSERT INTO TMPLAN (CO_PROD,NO_PLAN,DE_VELO_SUBI,DE_VELO_BAJA,CO_PLAN_MEDI_INST,IM_MONTO,CO_MONE_FACT,ST_ELIM) VALUES '
	for reg in cur.fetchall():
		plan_id+=1			
		glosa=reg[0].strip()
		producto=idProducto[reg[1].strip()]
		medio=idMedioPlan[reg[2].strip()]
		monto=reg[3]
		moneda=idMoneda[reg[4].strip()]
		vb=reg[5].strip()
		vs=reg[6].strip()
		sql="%s (%s,'%s','%s','%s','%s',%s,%s,0)," % (sql,producto['id'],glosa,vs,vb,medio['id'],monto,moneda)

		producto['planes'][glosa+str(monto)]={'id':plan_id}

	file_todo.write(sql[:-1]+';\r\n')
		
def negocio_sucursal_plan():
	cur = conn.cursor()
	cur.execute('''
		SELECT 
			[NEGOCIO ],
			[OIT INSTALACION],
			[GLOSA],
			[SERVICIO],
			[MEDIO],
			[IMPORTE ],
			[MONEDA],
		    [VELOCIDAD BAJADA],
		    [VELOCIDAD SUBIDA],
			[Fecha inicio OIT]
		  FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO]
		  GROUP BY 
		  	[NEGOCIO ],
			[OIT INSTALACION],
			[GLOSA],
			[SERVICIO],
			[MEDIO],
			[IMPORTE ],
			[MONEDA],
		    [VELOCIDAD BAJADA],
		    [VELOCIDAD SUBIDA],
			[Fecha inicio OIT];
	''')
	plan_id=0
	sql='INSERT INTO TMNEGO_SUCU_PLAN (CO_NEGO_SUCU,ST_SOAR_INST,CO_OIT_INST,ST_SOAR_DESI,CO_OIT_DESI,CO_PLAN,NO_PLAN,DE_VELO_SUBI,DE_VELO_BAJA,FE_INIC,FE_FIN,FE_ULTI_FACT,IM_MONTO,ST_ELIM) VALUES '
	van_idNego_Sucu_Plan=0
	for reg in cur.fetchall():
		van_idNego_Sucu_Plan+=1
		plan_id+=1			
		nego=(long)(reg[0])
		oit_inst=reg[1].strip().upper() if reg[1] else None

		soar_oit_inst=False
		num_oit_inst=None
		if oit_inst:
			if oit_inst.find('S')==-1:
				num_oit_inst=(long)(oit_inst)
				if oit_inst.find('O')==0:
					soar_oit_inst=True
			else:
				num_oit_inst=(long)(oit_inst[1:])


		glosa=reg[2].strip()
		producto=idProducto[reg[3].strip()]
		medio=idMedioPlan[reg[4].strip()]
		monto=reg[5]
		moneda=idMoneda[reg[6].strip()]
		vb=reg[7].strip()
		vs=reg[8].strip()
		fi=reg[9].date()

		regNego=idNego[nego]
		sucu=regNego['sucursales'][num_oit_inst]
		plan=producto['planes'][glosa+str(monto)]

		if regNego['tipo_fact']==idTipoFacturacion['Vencida']:
			uff='2016-03-31'
		else:
			uff='2016-04-30'
		sql="%s (%s,'%s','%s',NULL,NULL,%s,'%s','%s','%s','%s',NULL,'%s',%f,0)," % (sql,sucu['id'],1 if soar_oit_inst else 0,num_oit_inst,plan['id'],glosa,vs,vb,fi,uff,monto)
		idNego_Sucu_Plan[van_idNego_Sucu_Plan]={'fi':fi,'monto':monto,'moneda_id':moneda,'glosa':glosa,'sucu_id':sucu['id'],'nego':nego,'tipo_fact':regNego['tipo_fact'],'ss':{}}
		#producto['planes'][glosa+str(monto)]={'id':plan_id}

	file_todo.write(sql[:-1]+';\r\n')	

def ss():
	cur = conn.cursor()
	cur.execute('''
		SELECT 		
			[MONEDA],	
			[SERVICIO],
			[SERVICIOS SUPLEMENTARIOS],
			[IMPORTE ADICIONAL]
		  FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO]
		  GROUP BY
		  	[MONEDA],
			[SERVICIO],
			[SERVICIOS SUPLEMENTARIOS],
			[IMPORTE ADICIONAL]			
	''')
	ss_id=0
	sql='INSERT INTO TMSERV_SUPL (CO_PROD,CO_MONE_FACT,NO_SERV_SUPL,IM_MONTO,ST_AFEC_DETR,ST_ELIM) VALUES '
	for reg in cur.fetchall():						
		moneda=idMoneda[reg[0].strip()]
		producto=idProducto[reg[1].strip()]
		glosa=reg[2].strip() if reg[2] else None
		monto=reg[3]
		if glosa!='.' and glosa!=None:
			ss_id+=1	
			sql="%s (%s,'%s','%s','%s',1,0)," % (sql,producto['id'],moneda,glosa,monto)
			producto['sss'][glosa+str(monto)]={'id':ss_id}
	file_todo.write(sql[:-1]+';\r\n')

def negocio_sucursal_ss():
	cur = conn.cursor()
	cur.execute('''
		SELECT 	
			[NEGOCIO ],
			[OIT INSTALACION],			
			[MONEDA],	
			[SERVICIO],
			[SERVICIOS SUPLEMENTARIOS],
			[IMPORTE ADICIONAL],
			[Fecha inicio OIT]
		  FROM [EXPORT_EXCEL_ABRIL_2016].[dbo].[CLIENTE_SERVICIO]
		  GROUP BY
			[NEGOCIO ],
			[OIT INSTALACION],		  
		  	[MONEDA],
			[SERVICIO],
			[SERVICIOS SUPLEMENTARIOS],
			[IMPORTE ADICIONAL],
			[Fecha inicio OIT]	
	''')
	sql='INSERT INTO TMNEGO_SUCU_SERV_SUPL (CO_NEGO_SUCU,ST_SOAR_INST,CO_OIT_INST,CO_SERV_SUPL,NO_SERV_SUPL,FE_INIC,FE_FIN,FE_ULTI_FACT,IM_MONTO,ST_AFEC_DETR,ST_ELIM) VALUES '
	van_idNego_Sucu_Serv_Supl=0
	for reg in cur.fetchall():	
		nego=(long)(reg[0])
		oit_inst=reg[1].strip().upper() if reg[1] else None
		moneda=idMoneda[reg[2].strip()]
		producto=idProducto[reg[3].strip()]
		glosa=reg[4].strip() if reg[4] else None
		monto=reg[5]
		fi=reg[6]
		if glosa!='.' and glosa!=None:
			regNego=idNego[nego]

			soar_oit_inst=False
			num_oit_inst=None
			if oit_inst:
				if oit_inst.find('S')==-1:
					num_oit_inst=(long)(oit_inst)
					if oit_inst.find('O')==0:
						soar_oit_inst=True
				else:
					num_oit_inst=(long)(oit_inst[1:])
			sucu=regNego['sucursales'][num_oit_inst]
			ss=producto['sss'][glosa+str(monto)]
			if regNego['tipo_fact']==idTipoFacturacion['Vencida']:
				uff='2016-03-31'
			else:
				uff='2016-04-30'
			sql="%s (%s,%s,%s,%s,'%s','%s',NULL,'%s',%f,1,0)," % (sql,sucu['id'], 1 if soar_oit_inst else 0,num_oit_inst,ss['id'],glosa,fi,uff,monto)
			van_idNego_Sucu_Serv_Supl+=1
			#Asocia los servicios suplementarios con su respectivo plan
			for key in idNego_Sucu_Plan:
				plan=idNego_Sucu_Plan[key]	
				if (plan['sucu_id']==sucu['id'] and plan['nego']==nego):
					plan['ss'][van_idNego_Sucu_Serv_Supl]={'fi':fi,'monto':monto,'moneda_id':moneda,'glosa':glosa}


	file_todo.write(sql[:-1]+';\r\n')		
def main():
	file_todo.write('USE GAF_FACTURACION;\r\n')
	ubigeo()
	cliente()
	cliente_contacto()
	cliente_sucursal()
	cliente_update()	
	negocio()
	negocio_sucursal()
	medio_plan()
	plan()
	negocio_sucursal_plan()
	ss()
	negocio_sucursal_ss()

def cierre(periodo_inicio=01,ano_inicio=2014,periodo_fin=04,ano_fin=2016):	
	#file_todo.write("INSERT INTO TMCIER (CO_PROD,NU_PERI,NU_ANO,NU_TIPO_CAMB,DE_RAIZ_RECI,DE_RAIZ_FACT,FE_EMIS,FE_VENC,ST_CIER) VALUES (%d)" % (idProducto[]))
	sql="INSERT INTO TMCIER_DATA_SERV_SUPL (CO_CIER_DATA_SUCU,CO_NEGO_SUCU_SERV_SUPL,FE_INIC,FE_FIN,IM_MONT,CO_MONE_FACT,DE_NOMB,ST_TIPO_COBR) VALUES "
	sql1="INSERT INTO TMCIER_DATA_PLAN (CO_CIER_DATA_SUCU,CO_NEGO_SUCU_PLAN,FE_INIC,FE_FIN,IM_MONT,CO_MONE_FACT,DE_NOMB,ST_TIPO_COBR) VALUES "
	sql2="INSERT INTO TMCIER_DATA_SUCU (CO_NEGO_SUCU,CO_CIER_DATA_NEGO,DE_DIRE_SUCU,DE_ORDE) VALUES "
	sql3="INSERT INTO TMCIER_DATA_NEGO (CO_NEGO) VALUES "
	van_insert=0
	van_insert_ss=0
	van_cier_sucu_id=0
	van_cier_nego_id=0

	while ano_inicio*100+periodo_inicio<=ano_fin*100+periodo_fin:
		primer_dia=datetime.datetime(ano_inicio,periodo_inicio,1)
		ultimo_dia=primer_dia+relativedelta(months=1,days=-1)		
		for key in idNego_Sucu_Plan:
			reg=idNego_Sucu_Plan[key]
			if not(reg['tipo_fact']==idTipoFacturacion['Vencida'] and periodo_fin==periodo_inicio and ano_fin==ano_inicio):		
				van_cier_sucu_id+=1
				van_cier_nego_id+=1
				sql3="%s(%s)," % (sql3,reg['nego'])
				sql2="%s(%s,%d,'aaa','bbb')," % (sql2,reg['sucu_id'],van_cier_nego_id)
				sql1="%s(%s,%d,'%s','%s',%f,'%s','%s',1)," % (sql1,van_cier_sucu_id,key,primer_dia,ultimo_dia,reg['monto'],reg['moneda_id'],reg['glosa'])
				van_insert+=1

				if van_insert>500:
					sql3="%s;" % sql3[:-1]
					sql2="%s;" % sql2[:-1]
					sql1="%s;" % sql1[:-1]
					sql1="%s\r\nINSERT INTO TMCIER_DATA_PLAN (CO_CIER_DATA_SUCU,CO_NEGO_SUCU_PLAN,FE_INIC,FE_FIN,IM_MONT,CO_MONE_FACT,DE_NOMB,ST_TIPO_COBR) VALUES " % sql1
					sql2="%s\r\nINSERT INTO TMCIER_DATA_SUCU (CO_NEGO_SUCU,CO_CIER_DATA_NEGO,DE_DIRE_SUCU,DE_ORDE) VALUES " % sql2
					sql3="%s\r\nINSERT INTO TMCIER_DATA_NEGO (CO_NEGO) VALUES " % sql3
					van_insert=0

				for key_ss in reg['ss']:
					ss=reg['ss'][key_ss]
					sql="%s(%s,%d,'%s','%s',%f,'%s','%s',1)," % (sql,van_cier_sucu_id,key_ss,primer_dia,ultimo_dia,ss['monto'],ss['moneda_id'],ss['glosa'])
					van_insert_ss+=1
					if van_insert_ss>500:
						sql="%s;" % sql[:-1]
						sql="%s\r\nINSERT INTO TMCIER_DATA_SERV_SUPL (CO_CIER_DATA_SUCU,CO_NEGO_SUCU_SERV_SUPL,FE_INIC,FE_FIN,IM_MONT,CO_MONE_FACT,DE_NOMB,ST_TIPO_COBR) VALUES " % sql
						van_insert_ss=0


		periodo_inicio+=1
		if periodo_inicio>12:
			ano_inicio+=1
			periodo_inicio=1


	file_todo.write(sql3[:-1]+';')
	file_todo.write(sql2[:-1]+';')
	file_todo.write(sql1[:-1]+';')
	file_todo.write(sql[:-1]+';')

main()
cierre()
file_todo.close()

