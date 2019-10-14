/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.SUSP;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.springframework.util.comparator.ComparableComparator;

/**
 *
 * @author crodas
 */
public class FechaHora {
    public static Date FECHA_MAXIMA_SI_NO_TIENE=FechaHora.getDate(2050, 11, 31);//31 de diciembre 2050
    public static Date addDays(Date f,int days){
        Calendar c = Calendar.getInstance();
        c.setTime(f);
        c.add(Calendar.DATE, days);
        return c.getTime();
    } 
    public static Date addMonths(Date f,int months){
        Calendar c = Calendar.getInstance();
        c.setTime(f);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }
    public static Date addYears(Date f,int years){
        Calendar c = Calendar.getInstance();
        c.setTime(f);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    } 
    public static String getStringDate(Date d){
        return FechaHora.getStringDate(d, "-");
    }
    public static String getStringDate(Date d,String separador){
        if (d==null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("dd"+separador+"MM"+separador+"yyyy");//MMM nombre
        return format.format(d);
    }
    public static String getStringDateTime(Date d,String separadorDate){
        if (d==null)
            return null;        
        SimpleDateFormat format = new SimpleDateFormat("dd"+separadorDate+"MM"+separadorDate+"yyyy HH:mm:ss");//MMM nombre
        return format.format(d);
    }    
    public static Date getDate(Integer year,Integer month,Integer day){
        //month is 0 a 11        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day,0,0,0);        
        return cal.getTime();
    }
    
    public static Integer getYear(Date d){
        if (d==null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);        
        return calendar.get(Calendar.YEAR);  
    }

    public static Integer getMonth(Date d) {
        //retorna de 0 a 11
        if (d==null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);        
        return calendar.get(Calendar.MONTH);  
    }
            
    public static Integer getDay(Date d) {
        if (d==null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);        
        return calendar.get(Calendar.DAY_OF_MONTH);  
    }  
    
    public static Integer getNumDaysOfMonth(Date f){//1 to 31
        return FechaHora.getNumDaysOfMonth(FechaHora.getYear(f),FechaHora.getMonth(f));
    }
    
    public static Integer getNumDaysOfMonth(int year,int month){//month 0 a 11
        Date inicio=FechaHora.getDate(year, month, 1);
        Date fin=FechaHora.addDays(FechaHora.addMonths(inicio, 1),-1);
        return FechaHora.getDiffDays(inicio, fin);
    } 
    
    public static Integer getDiffDays(Date inicio,Date fin){
        DateTime startDate = new DateTime(inicio);
        DateTime endDate = new DateTime(fin);
        int ret=Days.daysBetween(startDate, endDate).getDays();
        if (ret!=0){
            if (ret>0)
                ret++;
            else
                ret--;
        }else{
            ret=1;
        }
        return ret;
    }
    public static Integer getDiffSeconds(Date inicio,Date fin){
        DateTime startDate = new DateTime(inicio);
        DateTime endDate = new DateTime(fin);
        return Seconds.secondsBetween(startDate, endDate).getSeconds();         
    }
    public static Integer getDiffMonths(Date inicio,Date fin){
        DateTime startDate = new DateTime(inicio);
        DateTime endDate = new DateTime(fin);
        return Months.monthsBetween(startDate, endDate).getMonths();         
    }    
    
    public static String getStringDateShortStringMonth(Date d) {
        if (d!=null){
            int month=FechaHora.getMonth(d);
            int day=FechaHora.getDay(d);
            int year=FechaHora.getYear(d);
            switch(month){
                case 0:return day+" ENE "+year;
                case 1:return day+" FEB "+year;
                case 2:return day+" MAR "+year;
                case 3:return day+" ABR "+year;
                case 4:return day+" MAY "+year;
                case 5:return day+" JUN "+year;
                case 6:return day+" JUL "+year;
                case 7:return day+" AGO "+year;
                case 8:return day+" SEP "+year;
                case 9:return day+" OCT "+year;
                case 10:return day+" NOV "+year;
                case 11:return day+" DIC "+year;
            }
                    
        }
        return null;        
    }
    
    public static Boolean isBetween(Date inicio,Date fin,Date compare){
        if (inicio!=null && fin!=null && compare!=null){
            long long_inicio=inicio.getTime();
            long long_fin=fin.getTime();
            long long_compare=compare.getTime();
            return (long_inicio<=long_compare) && (long_compare<=long_fin);
        }
        return false;
    }

    public static Date getUltimaFechaMes(Date tmpPI) {
        return  FechaHora.addDays(FechaHora.addMonths( FechaHora.getPrimeraFechaMes(tmpPI),1),-1);
    }
    public static Date getPrimeraFechaMes(Date tmpPI) {
        return  FechaHora.getDate(FechaHora.getYear(tmpPI) , FechaHora.getMonth(tmpPI), 1);
    }
    
    public static Boolean esMismoAnioPeriodo(Date a,Date b){
        return FechaHora.getYear(a).equals(FechaHora.getYear(b)) && FechaHora.getMonth(a).equals(FechaHora.getMonth(b));
    }
    
    public static Date[] getFechaIntersecta(Date Fa[],Date Fb[]){// [pos 0 es inicio, pos 1 es fin]
        //retorna cuantos dias se intersectan Fa on Fb
        if (Fa!=null && Fb!=null){
            if (Fa.length==2 && Fb.length==2){
                //1
                Long La[]=new Long[]{Fa[0].getTime(),Fa[1].getTime()};
                Long Lb[]=new Long[]{Fb[0].getTime(),Fb[1].getTime()};                
                Date inicio=null,fin=null;
                if (Lb[0]<=La[0] && Lb[1]>=La[0]){                    
                    inicio=Fa[0];
                    if (Lb[1]>La[1]){  
                        fin=Fa[1];
                    }else{
                        fin=Fb[1];
                    }
                }else if (Lb[0]<=La[1] && Lb[1]>=La[1]){
                    fin=Fa[1];
                    if (Lb[0]>La[0]){
                        inicio=Fb[0];
                    }else{
                        inicio=Fa[0];
                    }                    
                }
                if (inicio!=null && fin!=null){
                    return new Date[]{inicio, fin};
                }
                //2 evalucamos alrevez     
                Date t[]=new Date[]{Fa[0],Fa[1]};
                Fa=new Date[]{Fb[0],Fb[1]};
                Fb=t;
                La=new Long[]{Fa[0].getTime(),Fa[1].getTime()};
                Lb=new Long[]{Fb[0].getTime(),Fb[1].getTime()};                
                inicio=null;
                fin=null;
                if (Lb[0]<=La[0] && Lb[1]>=La[0]){                    
                    inicio=Fa[0];
                    if (Lb[1]>La[1]){  
                        fin=Fa[1];
                    }else{
                        fin=Fb[1];
                    }
                }else if (Lb[0]<=La[1] && Lb[1]>=La[1]){
                    fin=Fa[1];
                    if (Lb[0]>La[0]){
                        inicio=Fb[0];
                    }else{
                        inicio=Fa[0];
                    }                    
                }
                if (inicio!=null && fin!=null){
                    return new Date[]{inicio, fin};
                }                
            }
        }
        return null;
    }
    
    public static Date[] getFechaUnion(Date Fa[], Date Fb[]){// [pos 0 es inicio, pos 1 es fin]
        //Retorna la union de dos fechas intersectadas
        if (getFechaIntersecta(Fa, Fb)!=null || getFechaIntersecta(Fb, Fa)!=null){
            Long La[]=new Long[]{Fa[0].getTime(),Fa[1].getTime()};
            Long Lb[]=new Long[]{Fb[0].getTime(),Fb[1].getTime()}; 
            Date inicio=null,fin=null;
            if (La[0]>Lb[0]){
                inicio=Fb[0];
            }else{
                inicio=Fa[0];
            }
            
            if (La[1]>Lb[1]){
                fin=Fa[1];
            }else{
                fin=Fb[1];
                
            }
            return new Date[]{inicio, fin};
            /*               
            Date inicio=null,fin=null;
            if(Lb[0]<La[0] && Lb[1]>La[1]){
                inicio=Fb[0];
                fin=Fb[1];
            } else if(La[0]<Lb[0] && La[1]>Lb[1]){
                inicio=Fa[0];
                fin=Fa[1];
            } else if (La[1]>Lb[0] || La[1].equals(Lb[0])){
                inicio=Fa[0];
                fin=Fb[1];
            } else if (Lb[1]>La[0] || Lb[1].equals(La[0])){
                inicio=Fb[0];
                fin=Fa[1];
            }
            if (inicio!=null && fin!=null){
                return new Date[]{inicio, fin};
            }*/
        }
        
        return null;
    }
    public static List<Date[]> getUnionElementoJuntosLista(List<Date[]> as){
        //De una lista de Dates une los elementos que se intersectan
        as=FechaHora.getListaPeriodosOrdenadoPorColumna(as, 0);
        List<Date[]> retornar=new ArrayList<Date[]>();
        if (as.size()>0){
            retornar.add(as.get(0));
            for(int i=1;i<as.size();i++){
                boolean encontro=false;
                for(int j=0;j<retornar.size() && !encontro;j++){
                    Date union[]=FechaHora.getFechaUnion(as.get(i),retornar.get(j));
                    if (union!=null){
                        retornar.get(j)[0]=union[0];
                        retornar.get(j)[1]=union[1];
                        encontro=true;
                    }
                }
                if (!encontro){
                    retornar.add(as.get(i));
                }
            }
        }
        return retornar;
    }
    
    public static Date getFechaInicial(List<Date[]> date )
    {
    	Date fechaInicial = null;
    	
    	for(Date[] date_temp : date)
    	{
    		if(fechaInicial == null) fechaInicial = date_temp[0];
    		else {
    			if(date_temp[0].getTime()< fechaInicial.getTime())
    			{
    				fechaInicial = date_temp[0];
    			}
    		}
    	}
    	
    	return fechaInicial; 
    	
    }
    
    public static List<Date[]> getPeriodosUnionCortesSuspensiones(List<CORT> cortes,List<SUSP> suspensiones){
        List<Date[]> retornar=new ArrayList<Date[]>();
        List<Date[]> as=new ArrayList<Date[]>();
        Date fecha_muy_mayor=FechaHora.FECHA_MAXIMA_SI_NO_TIENE;
        //List<Date[]> bs=new ArrayList<Date[]>();
        for(CORT corte : cortes){
            Date t[]=new Date[]{corte.getFE_INIC_ForJSON(),corte.getFE_FIN_ForJSON()==null ? fecha_muy_mayor : corte.getFE_FIN_ForJSON()};
            if (t[0].getTime()!=t[1].getTime())
                as.add(t);
            //bs.add(new Date[]{corte.getFE_INIC_ForJSON(),corte.getFE_FIN_ForJSON()});
        }
        for(SUSP suspension : suspensiones){
            Date t[]=new Date[]{suspension.getFE_INIC_ForJSON(),suspension.getFE_FIN_ForJSON() == null ? fecha_muy_mayor : suspension.getFE_FIN_ForJSON() };
            if (t[0].getTime()!=t[1].getTime())
                as.add(t);            
            //bs.add(new Date[]{suspension.getFE_INIC_ForJSON(),suspension.getFE_FIN_ForJSON()});
        }
        for(int i=0;i<as.size();i++){
            Date a[]=as.get(i);
            List<Integer> no_considerar=new ArrayList <Integer>();
            for(int j=0;j<as.size();j++){
                Date b[]=as.get(j);
                if (i!=j && ! no_considerar.contains(j)){
                    Date union[]=FechaHora.getFechaUnion(a, b);
                    if (union!=null){
                        a=union;
                        no_considerar.add(j);
                        j=0;
                    }
                }
            } 
            //No insertamos si ya existe
            boolean existe=false;
            for(Date t[]:retornar){
                if (t[0].getTime()==a[0].getTime() && t[1].getTime()==a[1].getTime()) existe=true;
            }
            if (!existe)
                retornar.add(a);
        }   
        return retornar;
    }

    public static List<Date[]> getQuitar(Date a[],Date b[]){//a inicio, b quitar , posiciones 0 son las inicio, 1 son las fin
        List<Date[]> retornar=new ArrayList<Date[]>();
        if (b[0].getTime()>a[1].getTime()){
            //Lo que se quitara esta fuera del rando a, por tanto no se quita nada
            retornar.add(a);
        }else if (b[1].getTime()<a[0].getTime()){
            //Lo que se quitara esta fuera del rando a, por tanto no se quita nada
            retornar.add(a);            
        }else if (b[0].getTime()<=a[0].getTime() && b[1].getTime()>=a[1].getTime()){
            //"b" le quita todo a "a"
            //no agrega nada
        }else{
            if (b[0].getTime()<=a[0].getTime()){
                retornar.add(new Date[]{FechaHora.addDays(b[1],1),a[1]});
            }else if (b[1].getTime()>=a[1].getTime()){
                retornar.add(new Date[]{a[0],FechaHora.addDays(b[0],-1)});
            }else{
                retornar.add(new Date[]{a[0],FechaHora.addDays(b[0],-1)});
                retornar.add(new Date[]{FechaHora.addDays(b[1],1),a[1]});
            }
        }
        return retornar;
    }
    
    public static List<Date[]> getQuitar(Date a[],List<Date[]> bs){
        List<Date[]> cs=FechaHora.getUnionElementoJuntosLista(bs);
        List<Date[]> retornar=new ArrayList<Date[]>();
        List<Date[]> quedas=new ArrayList<Date[]>();
        retornar.add(a);
        quedas.add(a);
        for(Date c[]:cs){
            retornar=new ArrayList<Date[]>();
            for(Date queda[] : quedas){
                List<Date[]>ts=FechaHora.getQuitar(queda, new Date[]{c[0],c[1]});
                for(Date t[]:ts){
                    retornar.add(t);
                }
            }
            quedas=retornar;
            /*for(int j=0;j<retornar.size();j++){
                List<Date[]> queda=FechaHora.getQuitar(retornar.get(j), c);
                if(queda.size()==0)
            }*/
        }
        return retornar;
    }
    public static List<Date[]> getQuitarCortesSuspensionesNoConsiderarReconexion(Date a[],List<Date[]> periodoCORSUP){
        //El tema es qui no quitar las reconexiones ya que estas si se deben considerar
        //solucion restarle un dia a los f[1]
        //ejemplo UFF 8 enero, FD 25 febrero, Susp (8 enero a 14 enero) Corte (15, sin reconexion) =>> si debe facturar el 14 por eso no se debe 
        //considerar la reconexion
        List<Date[]> lista=new ArrayList<Date[]>();
        for(Date[] d : periodoCORSUP){
            if (d[1]!=null){
                if (d[0].getTime()<d[1].getTime()){
                    lista.add(new Date[]{d[0],FechaHora.addDays(d[1], -1)});
                    //lista.add(d);
                }
            }else{
                lista.add(d);
            }
        }
        return FechaHora.getQuitar(a, lista);
    }
    
    public static boolean esIgual(Date a, Date b) {
        if (a!=null && b!=null){
            if (a.getTime()==b.getTime())
                return true;
        }
        return false;
    }

    public static Date setDay(Date CierreRealFin, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CierreRealFin);
        calendar.set(Calendar.DATE, day);        
        return calendar.getTime();
    }
    
    
    public static Date getDateFromString(String fecha,String formato){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formato);
            return formatter.parse(fecha);            
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String gF(Date d){
        return FechaHora.getStringDate(d, "/");
    }
    
    public static List<Date[]> getListaPeriodosOrdenadoPorColumna(List<Date[]> l, int columna){
        Map<Long,List<Integer>> ordenado=new HashMap<Long,List<Integer>>();
        List<Long> orden=new ArrayList<Long>();
        for(int i=0;i<l.size();i++){
            Date [] columnas=l.get(i);
            Long key=columnas[columna].getTime();
            if (!ordenado.containsKey(key))
                ordenado.put(key, new ArrayList<Integer>());
            ordenado.get(key).add(i);
            orden.add(key);
        }
        Collections.sort(orden, new Comparator(){
            @Override
            public int compare(Object oa, Object ob) {
                Long a=(Long)oa;
                Long b=(Long)ob;
                return a.compareTo(b);
            }            
        });
        List<Date[]> retornar=new ArrayList<Date[]>();
        for(Long key : orden){
            for(Integer pos : ordenado.get(key)){
                retornar.add(l.get(pos));
            }
        }
        return retornar;
    }
    
    //Une los periodos continuos y forma uno solo (por ejm: 12-16 y 16-20=12-20)
    public static List<Date[]> getListaPeriodosContinuosUnidos(List<Date[]> lista){
        List<Date[]> retornar=new ArrayList<Date[]>();
        boolean addUltimo=false;
        for(int i=0;i<lista.size();i++){
            if (i>0){
                long f_anterior=lista.get(i-1)[1].getTime();
                long f_despues=addDays(lista.get(i)[0], -1).getTime();
                if (f_anterior==f_despues){
                    retornar.add(new Date[]{lista.get(i-1)[0],lista.get(i)[1]});
                }else{
                    if (lista.size()-1==i){
                        addUltimo=true;
                    }
                    retornar.add(lista.get(i-1));
                }
            }
        }
        if ((lista.size()>0 && addUltimo) || lista.size()==1){
            retornar.add(lista.get(lista.size()-1));
        }
        
        return retornar;
    }
}
