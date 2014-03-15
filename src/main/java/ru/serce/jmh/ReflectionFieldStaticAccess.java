package ru.serce.jmh;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

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
public class ReflectionFieldStaticAccess {
	
	private static final Class<TestedClass> clazz = TestedClass.class;
	
	Field simpleField;
	Field fieldAccessible;
	
	@Setup
	public void init() {
		try {
			simpleField = clazz.getField("aStat");

			Field Field = clazz.getField("bStat");
			Field.setAccessible(true);
			fieldAccessible = Field;
		} catch (Exception e) {
			// do nothing
		}
	}

	@GenerateMicroBenchmark
	public Object testFieldSaveAccessible() throws Exception {
		return fieldAccessible.get(null);
	}

	@GenerateMicroBenchmark
	public Object testFieldSaveNotAccessible() throws Exception {
		return simpleField.get(null);
	}

	@GenerateMicroBenchmark
	public Object testFieldStraighforward() throws Exception {
		return TestedClass.cStat;
	}
}
