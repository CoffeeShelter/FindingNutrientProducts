package detection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class DetectionMark {
	private PythonInterpreter interperter;

	public static void main(String[] args) throws Exception {
		ProcessBuilder pb2 = new ProcessBuilder(
				"C:\\Users\\USER\\anaconda3\\envs\\certificationMark_detection\\python.exe",
				"src/detection/DetectionMark.py");
		Process p4 = pb2.start();
		InputStream is2 = p4.getInputStream();
		BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
		for (String str; (str = br2.readLine()) != null;) {
			System.out.println(str);
		}
		System.out.println("end");
	}

	public DetectionMark() {
		System.setProperty("python.import.site", "false");
		this.interperter = new PythonInterpreter();
		interperter.execfile("src/detection/Test.py");

		PyFunction pyFunction = interperter.get("testFunc", PyFunction.class);
		
		int a = 100;
		int b = 30;

		PyObject pyobj = pyFunction.__call__(new PyInteger(a), new PyInteger(b));
		String result = pyobj.toString();

		System.out.println(result);
	}
}
