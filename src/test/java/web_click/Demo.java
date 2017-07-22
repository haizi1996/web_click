package web_click;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class Demo {

	public String mmm(@Deprecated String a, @Deprecated Boolean b, Integer c, Boolean d, Boolean e,String[] f ,Demo g) {

		return null;
	}

	public static void main(String[] args) throws Exception {
		
		Class<Demo> clazz = Demo.class;
		
		System.out.println(clazz.getClass()+"==>"+clazz.getClassLoader());
		System.out.println(String.class +"===>"+ String.class.getClassLoader());
		ClassPool classPool = ClassPool.getDefault();
		CtClass ct = classPool.getCtClass(clazz.getName());
		Method[] methodss = clazz.getDeclaredMethods();
		CtMethod[] ctmethods = ct.getDeclaredMethods();
		Map<String, Method> methods = new HashMap<String, Method>();
		for (Method method : methodss) {
			methods.put(method.getName(), method);
		}
		for (CtMethod ctmethod : ctmethods) {
	//		System.out.println(ctmethod.getName());
			Method method = methods.get(ctmethod.getName());

			MethodInfo info = ctmethod.getMethodInfo();
			CodeAttribute codeAttribute = info.getCodeAttribute();

			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);

			int paramCount = method.getParameterCount();
			Parameter[] fields = method.getParameters();
			for (int i = 0; i < paramCount; i++) {
				System.out.println(attr.variableName(i + 1) +"===>"+ fields[i].getType().getSimpleName() + "==>" +Class.forName(fields[i].getType().getName().replaceAll("\\[L", "")).getClassLoader());
				
			}
			// System.out.println(method.getName());

			CtClass[] ctc = ctmethod.getParameterTypes();
			
		

		}

	}

}
