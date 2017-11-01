package com.rtcomps.data.base;

import java.lang.reflect.Method;


public class ClassUtil {
	
	public static Object runNoArgMethodIfAny(Object obj, String methodName) {
		 Method method = findMethod(obj.getClass(),methodName);
		 if (method == null) {
			 return null;
		 }
		 try {
			return method.invoke(obj, new Object[0]);
		 } catch (Exception e) {
			throw new RuntimeException("Failed to invoke method="+methodName +", on object="+obj,e);	
		 }
				
	}
	
	public static Method findMethod(@SuppressWarnings("rawtypes") Class klass,String methodName) {
		for(Method method: klass.getMethods()) {
			if(method.getName().equals(methodName))
				return method;
		}
		return null;
	}

}
