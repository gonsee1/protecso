package com.americatel.facturacion.fncs;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.StringMemberValue;


public class CopyClass {
	private ClassPool pool=null;
	public CopyClass(){
            this.pool=ClassPool.getDefault();		
	}
	
	/*public CopyClass(ClassPool pool){
		this.pool=pool;
	}*/	
	
	public Object copyObject(String className,Object obj,List <CopyClass.Attribute> listaAttr){
		Map<String, Object[]> info=this.getInfoObject(obj);		
		Class <?> clazz=this.copyClass(className, obj,info,listaAttr);
		Object ret=null;
		try {			
			ret=clazz.newInstance();			
			Object[] reg=null;
			for (Entry <String, Object[]> entry : info.entrySet()) {
				reg=entry.getValue();
				clazz.getMethod("set"+entry.getKey(), (Class)reg[0]).invoke(ret, reg[1]);
			}
			for (CopyClass.Attribute attr:listaAttr){
				clazz.getMethod("set"+attr.getName(), attr.getClazz()).invoke(ret, attr.getValue());
			}
		} catch (Exception e) {}
		return ret;
	}
	
	public Class<?> copyClass(String className,Object obj,Map<String, Object[]> info,List <CopyClass.Attribute> listaAttr){
		Class <?> ret=this.getClass(className);
		if (ret==null){
			CtClass clase= this.pool.makeClass(className);
			this.createStructureClass(clase,obj,info);
			this.createStructureClass(clase, listaAttr);
			try {
				ret=clase.toClass();
			} catch (Exception e) {}			
		}
		return ret;
	}
	public Class<?> copyClass(String className,Object obj){
		return this.copyClass(className,obj,this.getInfoObject(obj),null);
	}
	
	
	public Class<?> getClass(String className){		
		try {	
			//pool.find(className);
			return Class.forName(className);			
		} catch (Exception e) {}		
		return null;
	}
	
	private Map<String, Object[]> getInfoObject(Object obj){
		Map<String, Object[]> ret=new HashMap<String, Object[]>();
		Class <?> cls=obj.getClass();
		Method metodos[]= cls.getDeclaredMethods();
		String nameMethod="";
		String namePar="";
		Class classRet=null;		
		for(Method metodo: metodos){
			nameMethod=metodo.getName();
			if (nameMethod.toLowerCase().indexOf("get")>-1){
				try {					
					Object value=metodo.invoke(obj,new Object[]{});
					if (value==null)						
						classRet=metodo.getReturnType();
					else
						classRet=value.getClass();
					namePar=nameMethod;
					namePar=namePar.replaceAll("\\_ForJSON$|^get","");
					ret.put(namePar, new Object[]{classRet,value,metodo});
				} catch (Exception e) {
				}			
			}			
		}
		return ret;
	}
	
	private void createStructureClass(CtClass clase,Object obj,Map<String, Object[]> info){
		for (Entry <String, Object[]> entry : info.entrySet()) {
			try {
				clase.addField(this.generateField((Class<?>)entry.getValue()[0], entry.getKey(), clase));
				clase.addMethod(this.generateMethodGetter(clase, entry.getKey(), entry.getValue()));
				clase.addMethod(this.generateMethodSetter(clase, entry.getKey(), (Class<?>)entry.getValue()[0]));
			} catch (Exception e) {}			
		}
	}

	private void createStructureClass(CtClass clase,List <CopyClass.Attribute> listaAttr){
		try {
			CtMethod metodo=null;
			for(CopyClass.Attribute attr : listaAttr){
				Object value=attr.getValue();
				String name=attr.getName();
				Class clazz=attr.getClazz();
				clase.addField(this.generateField(clazz, name, clase));
				metodo=this.generateMethodGetter(clase, name, clazz);
				clase.addMethod(metodo);
				this.copyAnnotations(attr.getAnnotationsGet(), metodo);
				clase.addMethod(this.generateMethodSetter(clase, name, clazz));			
			}
		} catch (Exception e) {e.printStackTrace();}			
	}
	
	private void createStructureClass(CtClass clase,Object obj){
		this.createStructureClass(clase,obj,this.getInfoObject(obj));
	}
	
    private CtClass toCtClass(Class <?>clazz){
    	this.pool.insertClassPath(new ClassClassPath(clazz));
    	try {
    		return  this.pool.getCtClass(clazz.getCanonicalName());
		} catch (Exception e) { 
			return null;
		}
    	
    } 
    private CtField generateField(Class type,String name,CtClass clase){
    	CtField ret=null;
    	try {
    		ret=new CtField(this.toCtClass(type),name,clase);
    	}catch(Exception e){}   
    	return ret;
    }
    private void copyAnnotations(Method de,CtMethod a){
    	Annotation anns[]=de.getDeclaredAnnotations(); 
    	MethodInfo info=a.getMethodInfo();
		ConstPool constpool=info.getConstPool();
		AnnotationsAttribute attr = new AnnotationsAttribute(constpool,AnnotationsAttribute.visibleTag );//AnnotationsAttribute.visibleTag
		for(Annotation ann : anns){
			javassist.bytecode.annotation.Annotation anotation=null; 
			try {					
				anotation=new javassist.bytecode.annotation.Annotation(constpool,this.toCtClass(ann.annotationType()));
				for (Method m : ann.annotationType().getMethods()){
			
					if (m.getName().equals("value") || m.getName().equals("required")){
						anotation.addMemberValue(m.getName(), createMemberValue(m.getReturnType(),m.invoke(ann, new Object[]{}) , constpool));
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			attr.addAnnotation(anotation);
			
		}
		info.addAttribute(attr);    	
    }
    private void copyAnnotations(List<Annotation> lista,CtMethod a){    	 
    	MethodInfo info=a.getMethodInfo();
		ConstPool constpool=info.getConstPool();
		AnnotationsAttribute attr = new AnnotationsAttribute(constpool,AnnotationsAttribute.visibleTag );//AnnotationsAttribute.visibleTag
		for(Annotation ann : lista){
			javassist.bytecode.annotation.Annotation anotation=null; 
			try {					
				anotation=new javassist.bytecode.annotation.Annotation(constpool,this.toCtClass(ann.annotationType()));
				for (Method m : ann.annotationType().getMethods()){
			
					if (m.getName().equals("value") || m.getName().equals("required")){
						anotation.addMemberValue(m.getName(), createMemberValue(m.getReturnType(),m.invoke(ann, new Object[]{}) , constpool));
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			attr.addAnnotation(anotation);
			
		}
		info.addAttribute(attr);    	
    }
    private CtMethod generateMethodGetter(CtClass declaringClass,String fieldName, Object[] data){
    	CtMethod ret=null;    	
    	try {
			Class fieldClass=(Class)data[0];			
        	String getterName="get"+fieldName;
			ret=CtMethod.make("public "+fieldClass.getName()+ " "+getterName+"(){return this."+fieldName+";}", declaringClass);
			if (data.length==3){
				Method metodo=(Method)data[2];
				this.copyAnnotations(metodo, ret);
			}			
 		} catch (Exception e) {}    	
    	return ret;
    }
    private CtMethod generateMethodGetter(CtClass declaringClass,String fieldName,Class<?> data){
    	return this.generateMethodGetter(declaringClass, fieldName, new Object[]{data});
    }
    
    private MemberValue createMemberValue(Class<?> type, Object val, ConstPool cp) {
        if (type == int.class) {
            return new IntegerMemberValue(cp, (Integer) val);
        } else if (type == short.class) {
            return new ShortMemberValue((Short) val, cp);
        } else if (type == long.class) {
            return new LongMemberValue((Long) val, cp);
        } else if (type == byte.class) {
            return new ByteMemberValue((Byte) val, cp);
        } else if (type == float.class) {
            return new FloatMemberValue((Float) val, cp);
        } else if (type == double.class) {
            return new DoubleMemberValue((Double) val, cp);
        } else if (type == char.class) {
            return new CharMemberValue((Character) val, cp);
        } else if (type == boolean.class) {
            return new BooleanMemberValue((Boolean) val, cp);
        } else if (type == String.class) {
            return new StringMemberValue((String) val, cp);
        } else if (type == Class.class) {
            return new ClassMemberValue(((Class<?>) val).getName(), cp);
        } else if (type.isEnum()) {
            EnumMemberValue e = new EnumMemberValue(cp);
            e.setType(type.getName());
            e.setValue(((Enum<?>) val).name());
            return e;
        } else if (type.isAnnotation()) {
            //return new AnnotationMemberValue(createJavassistAnnotation((java.lang.annotation.Annotation) val, cp), cp);
        } else if (type.isArray()) {
            //Class<?> arrayType = type.getComponentType();
            //int length = Array.getLength(val);
            //MemberValue arrayval = createEmptyMemberValue(arrayType, cp);
            //ArrayMemberValue ret = new ArrayMemberValue(arrayval, cp);
            //MemberValue[] vals = new MemberValue[length];
            //for (int i = 0; i < length; ++i) {
			//                vals[i] = createMemberValue(arrayType, Array.get(val, i), cp);
            //}
            //ret.setValue(vals);
            //return ret;
        }
        throw new RuntimeException("Invalid array type " + type + " value: " + val);

    }    
    
	private CtMethod generateMethodSetter(CtClass declaringClass, String fieldName, Class fieldClass){
		CtMethod ret=null;
		try {
			String str="public void set"+fieldName+"("+fieldClass.getName()+" "+fieldName+"){this."+fieldName+"="+fieldName+";}";
			ret=CtMethod.make(str, declaringClass);
		} catch (Exception e) {e.printStackTrace();}
		return ret;
	}     

	
	public void setValueAttribute(Object objeto, String nameMethod, Object value) {
		try {
			Method metodo=null;
			if (value!=null){
				metodo=objeto.getClass().getMethod("set"+nameMethod,value.getClass());
				metodo.invoke(objeto, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	

	
	public static class Attribute{
		private String name=null;
		private Object value=null;
		private Class clazz=null;
		List<Annotation> AnnotationsGet=new ArrayList<Annotation>();
		public Attribute(String name,Class clazz, Object value){
			this.name=name;
			this.value=value;
			this.clazz=clazz;
		}
		public Attribute(String name,Class clazz, Object value,Object AnnotationsGet[]){
			this.name=name;
			this.value=value;
			this.clazz=clazz;
			for(Object get:AnnotationsGet){
				Object a[]=(Object [])get;
				Class cls=(Class)a[0];
				Object params[]=(Object [])a[1];
				Map<String,Object> values=new TreeMap<String, Object>();
				for (Object param:params){
					Object regs[]=(Object [])param;
					values.put((String)regs[0], regs[1]);
				}				
				this.AnnotationsGet.add((Annotation)Proxy.newProxyInstance(cls.getClassLoader(), new Class[] { cls, Serializable.class }, new AnnotationProxy(cls, values)));
			}
		}		
		public String getName(){
			return this.name;
		}
		public Object getValue(){
			return this.value;
		}
		public Class getClazz(){
			return this.clazz;
		}		
		public List<Annotation> getAnnotationsGet(){
			return this.AnnotationsGet;
		}
		public void addAnnotationGet(Annotation ann){
			this.AnnotationsGet.add(ann);
		}
		/*
		public static List<Attribute> getAttributes (Object[][] data){
			List<Attribute> ret=new ArrayList<Attribute>();
			for(Object obj[]:data){
				ret.add(new CopyClass.Attribute((String)obj[0],obj[1]));
			}
			return ret;
		}	*/	
		
	}
	
	private static class AnnotationProxy<T extends Annotation> implements InvocationHandler {
		private final Class<T> _annotationClass;
		private final List<String> _elements = new ArrayList<String>();
		private final Map<String, Object> _values;
		private final Map<String, Method> _methods = new HashMap<String, Method>();
		
		public AnnotationProxy(Class<T> annotationClass, Map<String, Object> values) {
			_values = (values == null ? new HashMap<String, Object>() : new HashMap<String, Object>(values));
			_annotationClass = annotationClass;
			for (Method method : _annotationClass.getDeclaredMethods()) {
				if (Modifier.isPublic(method.getModifiers()) && Modifier.isAbstract(method.getModifiers()) && method.getParameterTypes().length == 0) {
					String elementName = method.getName();
					_elements.add(elementName);
					_methods.put(elementName, method);
					Object defaultValue = method.getDefaultValue();
					if (defaultValue != null && !_values.containsKey(elementName))
						_values.put(elementName, defaultValue);
				}
			}
		}
		
		private static boolean isEqualToAny(Object lhs, Object... rhs) {
			for (Object item : rhs) {
				if (lhs.equals(item))
					return true;
			}
			return false;
		}
		
		private int valueHashCode(Class<?> elementType, Object value) {
			if (elementType.isArray()) {
				return Arrays.hashCode((Object[])value);
			} else if (isEqualToAny(elementType,
					byte.class, Byte.class,
					char.class, Character.class,
					double.class, Double.class,
					float.class, Float.class,
					int.class, Integer.class,
					long.class, Long.class,
					short.class, Short.class,
					boolean.class, Boolean.class,
					String.class)
					|| elementType.isEnum()
					|| elementType.isAnnotation()) {
				return value.hashCode();
			}
			throw new RuntimeException("This shouldn't happen");
		}
		
		private int calculateHashCode() {
			// This is specified in java.lang.Annotation.
			int hashCode = 0;
			for (String element : _elements)
				hashCode += (127 * element.hashCode()) ^ valueHashCode(_methods.get(element).getReturnType(), _values.get(element));
			return hashCode;
		}
		
		private boolean checkEquals(Object other) throws Throwable {
			if (!_annotationClass.isAssignableFrom(other.getClass()))
				return false;
			for (String element : _elements) {
				Method method = _methods.get(element);
				method.setAccessible(true);
				Object value = _values.get(element);
				Object otherValue = method.invoke(other);
				if (!value.equals(otherValue))
					return false;
			}
			return true;
		}
		
		private String buildString() {
			StringBuilder builder = new StringBuilder("@").append(_annotationClass.getName());
			if (_elements.size() > 0) {
				builder.append("(");
				for (int x = 0; x < _elements.size(); ++x) {
					if (x > 0)
						builder.append(",");
					builder.append(_elements.get(x)).append("=").append(_values.get(_elements.get(x)));
				}
				builder.append(")");
			}
			return builder.toString();
		}
		
		// http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/Annotation.html
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();

			if ("annotationType".equals(methodName)) {
				return _annotationClass;
			} else if ("equals".equals(methodName)) {
				return checkEquals(args[0]);
			} else if ("hashCode".equals(methodName)) {
				return calculateHashCode();
			} else if ("toString".equals(methodName)) {
				return buildString();
			} else if (_elements.contains(methodName)) {
				return _values.get(methodName);
			}
			throw new RuntimeException();
		}
	}

	 	
}
