/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.beans;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;

import com.americatel.facturacion.controllers.Cier;

/**
 *
 * @author crodas
 */
@Configuration 
@EnableAspectJAutoProxy
@MapperScan(basePackages = {"com.americatel.facturacion.mappers","com.americatel.facturacion.models.historico.mappers"}) 
public class ConfigApp {

	private static Logger log = Logger.getLogger(ConfigApp.class.getName());
	
    public static String SERVER[] = new String[]{ 
    	//"192.168.62.160/AMTEL_FSATELITAL_CONFIGURADO_TI_DEV_TEST_REGLAS","venus","div3o390",
    	"192.168.62.160/AMTEL_FSATELITAL_VF","venus","div3o390",
        //"192.168.62.160/AMTEL_FSATELITAL_ALEX","venus","div3o390",
        //"aglmarte.americatelperu.red/AMTEL_FSATELITAL","facturacion.satelita","f4cT5r4C10n.s4T-4mT3l.1977",
    };    
   
//    @Bean(name = "dataSource")
//    public DriverManagerDataSource dataSource() {
//        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();        
//        driverManagerDataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
//        driverManagerDataSource.setUrl("jdbc:jtds:sqlserver://"+ConfigApp.SERVER[0]+";useNTLMv2=true;domain=AMERICATELPERU");
//        driverManagerDataSource.setUsername(ConfigApp.SERVER[1]);
//        driverManagerDataSource.setPassword(ConfigApp.SERVER[2]);   
//        return driverManagerDataSource;
//    }
    /**
	 * Metodo de conexion a la base de datos consumiendo el recurso JNDI del
	 * Servidor Web (Tomcat).
	 * 
	 * @return
	 */
    @Bean(name = "dataSource")
	public DataSource dataSource() {
		//log.info(":: ConfigApp dataSource :: Starting execution...");
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		dsLookup.setResourceRef(true);
		DataSource dataSource = dsLookup.getDataSource("jdbc/facturacionDS");
		//log.info(":: ConfigApp dataSource :: Execution finish."+dataSource);
		return dataSource;
	}

  
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory() {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource());
        return sqlSessionFactory;
    }   
    
 
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {        
        return new DataSourceTransactionManager(dataSource());
    }   
     
    
}
