package ru.serce.jmh;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class ReflectionMethodStaticAccess {
	
	private static final Class<TestedClass> clazz = TestedClass.class;
	
	Method simpleMethod;
	Method methodAccessible;
	MethodHandle methodHandle;
	FastMethod fastMethod;
	
	@Setup
	public void init() {
		try {
			simpleMethod = clazz.getMethod("getAStatic", null);

			Method method = clazz.getMethod("getBStatic", null);
			method.setAccessible(true);
			methodAccessible = method;
		
			fastMethod = FastClass.create(clazz).getMethod("getCStatic", null);
			
			methodHandle = MethodHandles.lookup().findStatic(clazz, "getDStatic", MethodType.methodType(Integer.class));
		} catch (Exception e) {
			// do nothing
		}
	}

	@GenerateMicroBenchmark
	public Object testFastMethod() throws Throwable {
		return fastMethod.invoke(null, null);
	}

	@GenerateMicroBenchmark
	public Object testMethodAccessible() throws Throwable {
		return methodAccessible.invoke(null, null);
	} 

	@GenerateMicroBenchmark
	public Object testMethodNotAccessible() throws Throwable {
		return simpleMethod.invoke(null, null);
	}
	
	@GenerateMicroBenchmark
	public Object testMethodHandleExact() throws Throwable {
		return (Integer)methodHandle.invokeExact();
	}
	
	@GenerateMicroBenchmark
	public Object testMethodHandle() throws Throwable {
		return (Integer)methodHandle.invoke();
	}

	@GenerateMicroBenchmark
	public Object testMethodDirect() throws Throwable {
		return TestedClass.getAStatic();
	}
}
