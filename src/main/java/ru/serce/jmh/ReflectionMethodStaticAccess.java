package ru.serce.jmh;

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
	FastMethod fastMethod;
	
	@Setup
	public void init() {
		try {
			simpleMethod = clazz.getMethod("getAStatic", null);

			Method method = clazz.getMethod("getBStatic", null);
			method.setAccessible(true);
			methodAccessible = method;

			fastMethod = FastClass.create(clazz).getMethod("getCStatic", null);
		} catch (Exception e) {
			// do nothing
		}
	}

	@GenerateMicroBenchmark
	public Object testFastMethodSave() throws Exception {
		return fastMethod.invoke(null, null);
	}

	@GenerateMicroBenchmark
	public Object testMethodSaveAccessible() throws Exception {
		return methodAccessible.invoke(null, null);
	}

	@GenerateMicroBenchmark
	public Object testMethodSaveNotAccessible() throws Exception {
		return simpleMethod.invoke(null, null);
	}

	@GenerateMicroBenchmark
	public Object testMethodStraighforward() throws Exception {
		return TestedClass.getAStatic();
	}
}
