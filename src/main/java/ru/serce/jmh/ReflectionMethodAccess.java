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
public class ReflectionMethodAccess {
	
	private static final Class<TestedClass> clazz = TestedClass.class;
	
	private TestedClass testedObject;
	
	Method simpleMethod;
	Method methodAccessible;
	FastMethod fastMethod;
	MethodHandle methodHandle;
	
	@Setup
	public void init() {
		try {
			testedObject = new TestedClass();
			
			simpleMethod = clazz.getMethod("getA", null);

			Method method = clazz.getMethod("getB", null);
			method.setAccessible(true);
			methodAccessible = method;
			
			fastMethod = FastClass.create(clazz).getMethod("getC", null);
			
			methodHandle = MethodHandles.lookup().findVirtual(clazz, "getD", MethodType.methodType(Integer.class));
		} catch (Exception e) {
			// do nothing
		}
	}

	@GenerateMicroBenchmark
	public Object testFastMethod() throws Throwable {
		return fastMethod.invoke(testedObject, null);
	}

	@GenerateMicroBenchmark
	public Object testMethodAccessible() throws Throwable {
		return methodAccessible.invoke(testedObject, null);
	}

	@GenerateMicroBenchmark
	public Object testMethodNotAccessible() throws Throwable {
		return simpleMethod.invoke(testedObject, null);
	}
	
	@GenerateMicroBenchmark
	public Object testMethodHandleExact() throws Throwable {
		return (Integer)methodHandle.invokeExact(testedObject);
	}
	
	@GenerateMicroBenchmark
	public Object testMethodHandle() throws Throwable {
		return (Integer)methodHandle.invoke(testedObject);
	}


	@GenerateMicroBenchmark
	public Object testMethodDirect() throws Throwable {
		return testedObject.getA();
	}

}
