#!/usr/bin/python
# -*- coding: utf-8 -*-
import pymssql
import codecs
conn = pymssql.connect(host='10.17.20.15', user='sa', password='123456', database='export_excel', charset='UTF-8')
idDepa={}
idClie={}
idPlan={}
idMedioPlan={}
idMoneda={'USD':'2','SOLES':'1'}
idSS={}
orden=0
ordenTodo=0
def getFile(name,raiz=False):
	global orden
	orden=orden+1
	if raiz:
		return codecs.open(str(orden)+" - "+name, mode='wb', encoding="utf-8", errors='strict', buffering=1)
	return codecs.open("sqls/"+str(orden)+" - "+name, mode='wb', encoding="utf-8", errors='strict', buffering=1)
	#return codecs.open("sqls/"+name, mode='wb', encoding="utf-8", errors='strict', buffering=1)

def medio_plan():
	cur = conn.cursor()
	cur.execute('SELECT UPPER(MEDIO) FROM PLANES$ GROUP BY MEDIO')
	f=getFile("medio_plan.sql")	
	sql="INSERT INTO [GAF_FACTURACION].[dbo].[TMPLAN_MEDI_INST] (NO_PLAN_MEDI_INST) VALUES "
	generate=0
	for x in cur.fetchall():
		sql=sql+u"("+ ('NULL' if x[0]==None else "'"+x[0]+"'")+"),\r\n"
		generate=generate+1	
		idMedioPlan[x[0]]=generate
	f.write(sql[:-3]+";")
	f.close()
	cur.close()		

def planes():
	'''
	CO_PROD TINYINT NOT NULL,
	NO_PLAN VARCHAR(100) NOT NULL,
	IM_MONTO DECIMAL(6,2) NOT NULL,	
	'''

	cur = conn.cursor()
	cur.execute('SELECT CODIGO, SERVICIO +\' \'+ GLOSA,IMPORTE,UPPER(MEDIO),"VELOCIDAD BAJADA","VELOCIDAD SUBIDA",MONEDA FROM PLANES$')
	f=getFile("planes.sql")
	sql="INSERT INTO [GAF_FACTURACION].[dbo].[TMPLAN] (CO_PROD,NO_PLAN,IM_MONTO,CO_PLAN_MEDI_INST,DE_VELO_SUBI,DE_VELO_BAJA,CO_MONE_FACT) VALUES "
	generate=0
	for x in cur.fetchall():
		c=unicode(x[0]).upper().strip()
		n=unicode(x[1]).upper().strip()
		i=unicode(x[2]).upper().strip()		
		vs=unicode(x[4]).upper().strip()		
		vb=unicode(x[5]).upper().strip()
		smo=unicode(x[6]).upper().strip()
		if n!='NONE':
			sql=sql+u"(1,'"+n+u"','"+i+"','"+str(idMedioPlan[x[3]])+"','"+vs+"','"+vb+"',"+idMoneda[smo]+"),\r\n"
			generate=generate+1
			idPlan[c]={'codigo':generate,'nombre':n,'vs':vs,'vb':vb,'monto':i,'vs':vs,'vb':vb}
	f.write(sql[:-3]+";")
	f.close()
	cur.close()

def suplementarios():
	'''
	CO_SERV_SUPL INT NOT NULL PRIMARY KEY IDENTITY (1,1),
	CO_PROD TINYINT NOT NULL,
	NO_SERV_SUPL VARCHAR(100) NOT NULL,
	IM_MONTO DECIMAL(6,2) NOT NULL,
	ST_AFEC_DETR BIT NOT NULL DEFAULT 0,-- AFECTO A DETRACCION
	ST_ADIC BIT NOT NULL DEFAULT 0,-- SERVICIO ADICIONAL
	'''

	cur = conn.cursor()
	cur.execute('SELECT CODIGO,GLOSA, [IMPORTE ADICIONAL]  FROM SS$')
	f=getFile("suplementarios.sql")
	sql="INSERT INTO [GAF_FACTURACION].[dbo].[TMSERV_SUPL] (CO_PROD,CO_MONE_FACT,NO_SERV_SUPL,IM_MONTO,ST_AFEC_DETR) VALUES "
	generate=0
	for x in cur.fetchall():
		c=unicode(x[0]).upper().strip()
		g=unicode(x[1]).upper().strip()
		i=unicode(x[2]).upper().strip()
		sql=sql+u"(1,2,'"+g+u"','"+i+"',1),\r\n"#para el producto 1 (Internet)
		sql=sql+u"(2,2,'"+g+u"','"+i+"',1),\r\n"#para el producto 2 (Datos)
		generate=generate+1
		idSS[c+"-INTERNET"]={'codigo':generate,'nombre':g,'monto':i}
		generate=generate+1
		idSS[c+"-DATOS"]={'codigo':generate,'nombre':g,'monto':i}
	f.write(sql[:-3]+";")
	f.close()
	cur.close()		

def departamento():
	cur = conn.cursor()
	cur.execute('SELECT departamento FROM CLIENTE_SERVICIO$ UNION SELECT departamento FROM CLIENTE$ GROUP BY departamento;')
	f=getFile("departamentos.sql")
	sql=u"INSERT INTO [GAF_FACTURACION].[dbo].[TMDEPA] (NO_DEPA) VALUES "
	generate=0
	for x in cur.fetchall():
		d=unicode(x[0]).upper().strip()
		sql=sql+u"('"+d+u"'),\r\n"
		generate=generate+1
		idDepa[d]={'codigo':generate,'lst':{}}
	f.write(sql[:-3]+";")
	f.close()
	cur.close()
def provincia():
	cur = conn.cursor()
	cur.execute('SELECT provincia,departamento FROM CLIENTE_SERVICIO$ UNION SELECT provincia,departamento FROM CLIENTE$ GROUP BY provincia,departamento;')
	f=getFile("provincia.sql")
	sql=u"INSERT INTO [GAF_FACTURACION].[dbo].[TMPROV] (NO_PROV,CO_DEPA) VALUES "
	generate=0
	for x in cur.fetchall():
		#print x
		a=unicode(x[0]).upper().strip()
		b=unicode(x[1]).upper().strip()
		c=idDepa[b]
		sql=sql+u"('"+a+u"','"+unicode(c['codigo'])+u"'),\r\n"
		generate=generate+1
		c['lst'][a]={'codigo':generate,'lst':{}}
	f.write(sql[:-3]+";")
	f.close()
	cur.close()
def distrito():
	cur = conn.cursor()
	cur.execute('SELECT distrito,provincia,departamento FROM CLIENTE_SERVICIO$ UNION SELECT distrito,provincia,departamento FROM CLIENTE$ GROUP BY provincia,departamento,distrito;')
	f=getFile("distrito.sql")
	sql=u"INSERT INTO [GAF_FACTURACION].[dbo].[TMDIST] (NO_DIST,CO_PROV) VALUES "
	generate=0
	for x in cur.fetchall():
		#print x
		a=unicode(x[0]).upper().strip()
		b=unicode(x[1]).upper().strip()
		c=unicode(x[2]).upper().strip()
		d=idDepa[c]['lst'][b]
		sql=sql+u"('"+a+u"','"+unicode(d['codigo'])+u"'),\r\n"
		generate=generate+1
		d['lst'][a]={'codigo':generate}
	f.write(sql[:-3]+";")
	f.close()
	cur.close()		
def cliente():
	cur=conn.cursor()
	cur.execute('SELECT [COD CLI],[CLIENTE ],[RUC] FROM [export_excel].[dbo].[CLIENTE$] GROUP BY [COD CLI],[CLIENTE ],[RUC]')
	f=getFile("cliente.sql")
	sql=u"INSERT INTO [GAF_FACTURACION].[dbo].[TMCLIE] (NO_RAZO,NO_CLIE,AP_CLIE,CO_TIPO_DOCU,DE_NUME_DOCU) VALUES "
	generate=1
	for x in cur.fetchall():
		a=x[0]
		ruc=x[2]
		if ruc:
			ruc=unicode(int(float(ruc)))
			if a!=None:
				a=a.upper().strip()
			sql=sql+u"('"+unicode(x[1] if x[1] else '')+"',NULL,NULL,1,'"+ruc+"'),\r\n"
			idClie[a]={'codigo':generate,'sucursales':{},'negocios':{}}
			generate=generate+1
	f.write(sql[:-3]+";")
	f.close()
	cur.close()	
def sucursal():
	cur=conn.cursor()
	cur.execute('SELECT * FROM (SELECT [COD CLI],UBICACION,DIRECCION,DISTRITO,PROVINCIA,DEPARTAMENTO FROM CLIENTE_SERVICIO$ UNION SELECT [COD CLI], [DIRECCION FISCAL] as UBICACION,[DIRECCION CORRESPONDENCA] as DIRECCION,DISTRITO,PROVINCIA,DEPARTAMENTO FROM CLIENTE$) AS W  GROUP BY [COD CLI],UBICACION,DIRECCION,PROVINCIA,DISTRITO,DEPARTAMENTO')
	generate=1
	f=getFile("sucursal.sql")
	sql="INSERT INTO [GAF_FACTURACION].[dbo].[TMSUCU] (DE_DIRE,CO_DIST,CO_CLIE) VALUES "
	for x in cur.fetchall():
		a=unicode(x[0]).upper().strip()
		b=unicode(x[1]).upper().strip()
		if a!='NONE':
			c=idClie[a]
			c['sucursales'][b]=generate
			generate=generate+1

			de=unicode(x[5]).upper().strip()
			pr=unicode(x[4]).upper().strip()
			di=unicode(x[3]).upper().strip()
			dire=unicode(x[2]).upper().strip().replace('\'','\'\'')
			if dire==u'0':
				dire=b

			cod_dis=idDepa[de]['lst'][pr]['lst'][di]['codigo']
			sql=sql+"('"+dire+"','"+unicode(cod_dis)+"','"+unicode(c['codigo'])+"'),\r\n"
	f.write(sql[:-3]+";")
	f.close()
	cur.close()
def negocio():
	cur=conn.cursor()
	cur.execute('SELECT [DIRECCION FISCAL],negocio,[COD CLI] FROM CLIENTE$ GROUP BY [DIRECCION FISCAL],negocio,[COD CLI]')	
	f=getFile("negocio.sql")
	sql="INSERT INTO [GAF_FACTURACION].[dbo].[TMNEGO] ([ST_FACT_DEBU],[CO_NEGO],[CO_SUCU_CORR],[CO_CLIE],[CO_PROD],[CO_MONE_FACT],[CO_PERI_FACT],[CO_TIPO_FACT]) VALUES "
	generate=1
	for x in cur.fetchall():
		c=unicode(x[2]).upper().strip()#cliente
		n=unicode(x[1]).upper().strip()#negocio
		cs=unicode(x[0]).upper().strip()#direccion
		if(c!='NONE') and n!='NONE':
			n=unicode(int(float(n)))
			cliente=idClie[c]
			codigo_sucursal=cliente['sucursales'][cs]
			sql=sql+"(1,'"+n+"','"+unicode(codigo_sucursal)+"','"+unicode(cliente['codigo'])+"',1,2,1,2),\r\n"
			cliente['negocios'][n]={'codigo':n,'sucursales':{},'producto':''}			
			generate=generate+1
	f.write(sql[:-3]+';')
	f.close()
	cur.close()
def negocio_sucursal():
	cur=conn.cursor()
	cur.execute('SELECT [COD CLI],[NEGOCIO ],[OIT INSTALACION],[UBICACION],MIN([Fecha inicio OIT]),MAX([Fecha de desact]) FROM CLIENTE_SERVICIO$ GROUP BY [COD CLI],[NEGOCIO ],[OIT INSTALACION],[UBICACION],[Fecha inicio OIT],[Fecha de desact]')	
	f=getFile("negocio_sucursal.sql")
	sql=u"INSERT INTO [GAF_FACTURACION].[dbo].[TMNEGO_SUCU] (CO_NEGO,ST_SOAR_INST,CO_OIT_INST,CO_CIRC,CO_SUCU,FE_INIC,FE_FIN) VALUES "
	generate=1
	for x in cur.fetchall():
		c=unicode(x[0]).upper().strip()#cliente
		n=unicode(x[1]).upper().strip()#negocio
		o=unicode(x[2]).upper().strip()[1:]#oit
		u=unicode(x[3]).upper().strip()#ubicacion
		inicio=unicode(x[4]).upper().strip()#ubicacion
		fin=unicode(x[5]).upper().strip()#ubicacion
		if(c!='NONE') and n!='NONE':
			n=unicode(int(float(n)))
			cliente=idClie[c]
			codigo_sucursal=cliente['sucursales'][u]
			#print cliente['negocios']
			negocio=cliente['negocios'][n]
			#print negocio
			codigo_negocio=negocio['codigo']
			if (fin=='NONE'):
				fin='NULL'
			else:
				fin="'"+fin+"'"
			sql=sql+"('"+unicode(negocio['codigo'])+"',0,'"+o+"',NULL,'"+unicode(codigo_sucursal)+"','"+inicio+"',"+fin+"),\r\n"
			negocio['sucursales'][o]={'codigo':generate}
			generate=generate+1
	f.write(sql[:-3]+';')
	f.close()
	cur.close()	

def sucursal_plan():
	'''
	CO_NEGO_SUCU_PLAN INT NOT NULL PRIMARY KEY IDENTITY (1,1),
	CO_NEGO_SUCU INT NOT NULL,
	ST_SOAR BIT NULL DEFAULT 0,
	CO_OIT INT NULL,
	CO_PLAN INT NOT NULL,
	NO_PLAN VARCHAR(100),
	FE_INIC DATE NOT NULL,
	FE_FIN DATE NULL,
	FE_ULTI_FACT DATE NULL, -- ULTIMA FACTURACION
	IM_MONTO DECIMAL(6,2) NOT NULL,	
	'''
	cur = conn.cursor()
	cur.execute('SELECT [COD CLI],[NEGOCIO],[COD SERV],[OIT INSTALACION],[Fecha inicio OIT],[Fecha de desact] FROM CLIENTE_SERVICIO$ GROUP BY [COD CLI],[NEGOCIO],[COD SERV],[OIT INSTALACION],[Fecha inicio OIT],[Fecha de desact];')
	f=getFile("sucursal_plan.sql")
	sql="INSERT INTO [GAF_FACTURACION].[dbo].[TMNEGO_SUCU_PLAN] (CO_NEGO_SUCU,ST_SOAR_INST,CO_OIT_INST,CO_PLAN,NO_PLAN,DE_VELO_SUBI,DE_VELO_BAJA,IM_MONTO,FE_INIC,FE_FIN) VALUES "
	#generate=0
	for x in cur.fetchall():
		c=unicode(x[0]).upper().strip()
		n=unicode(x[1]).upper().strip()
		p=unicode(x[2]).upper().strip()
		o=unicode(x[3]).upper().strip()[1:]
		ini=unicode(x[4]).upper().strip()
		fin=unicode(x[5]).upper().strip()
		plan=idPlan[p]
		if (fin=='NONE'):
			fin='NULL'
		else:
			fin="'"+fin+"'"
		if (n!='NONE'):			
			n=unicode(int(float(n)))
			negocio=idClie[c]['negocios'][n]
			if (plan['nombre'].find('INTERNET INTERNET')==0):
				negocio['producto']='INTERNET'
			else:
				negocio['producto']='DATOS'
			codigio_sucursal=unicode(negocio['sucursales'][o]['codigo'])
			sql=sql+u"('"+codigio_sucursal+"',0,'"+o+"','"+unicode(plan['codigo'])+"','"+plan['nombre']+"','"+plan['vs']+"','"+plan['vb']+"','"+plan['monto']+"','"+ini+"',"+fin+"),\r\n"
		#generate=generate+1
		#idSS[c]=generate
	f.write(sql[:-3]+";")
	f.close()
	cur.close()			


def sucursal_suplementario():
	'''
	CO_NEGO_SUCU_SERV_SUPL INT NOT NULL PRIMARY KEY IDENTITY (1,1),
	CO_NEGO_SUCU INT NOT NULL,
	ST_SOAR BIT NULL DEFAULT 0,
	CO_OIT INT NULL,
	CO_SERV_SUPL INT NOT NULL,
	NO_SERV_SUPL VARCHAR(100),
	FE_INIC DATE NOT NULL,
	FE_FIN DATE NULL,
	IM_MONTO DECIMAL(6,2) NOT NULL,
	ST_AFEC_DETR BIT NOT NULL DEFAULT 0,-- AFECTO A DETRACCION
	ST_ADIC BIT NOT NULL DEFAULT 0,-- SERVICIO ADICIONAL
	'''
	cur = conn.cursor()
	cur.execute("SELECT [COD CLI],[NEGOCIO],[S#SUPLEMENTARIO],[OIT INSTALACION],[Fecha inicio OIT],[Fecha de desact] FROM CLIENTE_SERVICIO$ WHERE [S#SUPLEMENTARIO]!=\'-\' GROUP BY [COD CLI],[NEGOCIO],[S#SUPLEMENTARIO],[OIT INSTALACION],[Fecha inicio OIT],[Fecha de desact];")
	f=getFile("sucursal_suplementario.sql")
	sql="INSERT INTO [GAF_FACTURACION].[dbo].[TMNEGO_SUCU_SERV_SUPL] (CO_NEGO_SUCU,ST_SOAR_INST,CO_OIT_INST,CO_SERV_SUPL,NO_SERV_SUPL,IM_MONTO,ST_AFEC_DETR,FE_INIC,FE_FIN) VALUES "
	#generate=0
	for x in cur.fetchall():
		c=unicode(x[0]).upper().strip()
		n=unicode(x[1]).upper().strip()
		p=unicode(x[2]).upper().strip()
		o=unicode(x[3]).upper().strip()[1:]
		ini=unicode(x[4]).upper().strip()
		fin=unicode(x[5]).upper().strip()
		ps=p.split('-')
		if (n!='NONE'):
			n=unicode(int(float(n)))
			negocio=idClie[c]['negocios'][n]
			codigio_sucursal=unicode(negocio['sucursales'][o]['codigo'])
			for p in ps:
				p=p.strip()
				ss=idSS[p+"-"+negocio['producto']]
				if (fin=='NONE' or fin=='NULL'):
					fin='NULL'
				else:
					fin="'"+fin+"'"

				sql=sql+u"('"+codigio_sucursal+"',0,'"+o+"','"+unicode(ss['codigo'])+"','"+ss['nombre']+"','"+ss['monto']+"',1,'"+ini+"',"+fin+"),\r\n"
		#generate=generate+1
		#idSS[c]=generate
	f.write(sql[:-3]+";")
	f.close()
	cur.close()		

def agregar_file(f,name):
	global ordenTodo
	ordenTodo=ordenTodo+1	
	fr=codecs.open("sqls/"+str(ordenTodo)+' - '+name, mode='rb', encoding="utf-8", errors='strict', buffering=1)
	for line in fr.readlines():
		f.write(line)
	f.write("\r\n-- -- -- * * * -- -- --\r\n")
	fr.close()
	

def unir():
	#productos internet 1, datos 2
	f=getFile("todo.sql",raiz=False)

	f.write('USE GAF_FACTURACION;')
	f.write('\r\n')
	agregar_file(f,"medio_plan.sql")
	agregar_file(f,"planes.sql")
	agregar_file(f,"suplementarios.sql")
	agregar_file(f,"departamentos.sql")
	agregar_file(f,"provincia.sql")
	agregar_file(f,"distrito.sql")
	agregar_file(f,"cliente.sql")
	agregar_file(f,"sucursal.sql")
	agregar_file(f,"negocio.sql")
	agregar_file(f,"negocio_sucursal.sql")
	agregar_file(f,"sucursal_plan.sql")
	agregar_file(f,"sucursal_suplementario.sql")
	f.write('\r\n')
	f.write('-- *******************\r\n')
	f.write('UPDATE n\r\n')
	f.write('SET n.CO_PROD=2\r\n')
	f.write('FROM TMNEGO n\r\n')
	f.write('inner join TMNEGO_SUCU ns on n.CO_NEGO=ns.CO_NEGO\r\n')
	f.write('inner join TMNEGO_SUCU_PLAN nsp on  ns.CO_NEGO_SUCU=nsp.CO_NEGO_SUCU\r\n')
	f.write('''where nsp.NO_PLAN like '%enlace de datos%';\r\n''')
	f.write('''UPDATE TMPLAN  SET CO_PROD=1 where NO_PLAN like '%INTERNET INTERNET%';\r\n''')
	f.write('''UPDATE TMPLAN  SET CO_PROD=2 where NO_PLAN like '%enlace de datos enlace de datos%';\r\n''')
	f.write('''UPDATE TMPLAN  SET NO_PLAN=right(NO_PLAN,DATALENGTH(NO_PLAN)-DATALENGTH('enlace de datos ')) where NO_PLAN like '%enlace de datos enlace de datos%';\r\n''')
	f.write('''UPDATE TMNEGO_SUCU_PLAN  SET NO_PLAN=right(NO_PLAN,DATALENGTH(NO_PLAN)-DATALENGTH('enlace de datos ')) where NO_PLAN like '%enlace de datos enlace de datos%';\r\n''')
	f.write('''UPDATE TMPLAN  SET NO_PLAN=right(NO_PLAN,DATALENGTH(NO_PLAN)-DATALENGTH('INTERNET ')) where NO_PLAN like '%INTERNET INTERNET%';\r\n''')
	f.write('''UPDATE TMNEGO_SUCU_PLAN  SET NO_PLAN=right(NO_PLAN,DATALENGTH(NO_PLAN)-DATALENGTH('INTERNET ')) where NO_PLAN like '%INTERNET INTERNET%';\r\n''')
	f.write('UPDATE TMNEGO_SUCU_PLAN SET FE_ULTI_FACT=FE_FIN WHERE FE_FIN is not null;\r\n')
	f.write('UPDATE TMNEGO_SUCU_SERV_SUPL SET FE_ULTI_FACT=FE_FIN WHERE FE_FIN is not null;\r\n')
	f.write('''UPDATE TMNEGO_SUCU_PLAN SET FE_ULTI_FACT='2015-01-31' WHERE FE_ULTI_FACT is null;\r\n''')
	f.write('''UPDATE TMNEGO_SUCU_SERV_SUPL SET FE_ULTI_FACT='2015-01-31' WHERE FE_ULTI_FACT is null;\r\n''')
	f.write('''UPDATE n SET n.CO_MONE_FACT=1 FROM TMNEGO n WHERE EXISTS(select * from TMNEGO_SUCU ns inner join TMNEGO_SUCU_PLAN nsp on nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU inner join TMPLAN p ON p.CO_PLAN=nsp.CO_PLAN WHERE p.CO_MONE_FACT=1 AND ns.CO_NEGO=n.CO_NEGO);''')#asignamos los tipo moneda a aca negocio
	f.close()

def main():
	medio_plan()
	planes()
	suplementarios()
	departamento()
	provincia()
	distrito()
	cliente()
	sucursal()
	negocio()
	negocio_sucursal()
	sucursal_plan()
	sucursal_suplementario()
	unir()
main()
