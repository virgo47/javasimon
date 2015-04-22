package xxx;

import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class Main {

	public static void main(String[] args) {
		ScriptEngineManager seManager = new ScriptEngineManager();
		List<ScriptEngineFactory> engineFactories = seManager.getEngineFactories();
		System.out.println("engineFactories = " + engineFactories);
	}
}
