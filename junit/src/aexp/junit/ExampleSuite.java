package aexp.junit;

import junit.framework.TestSuite;

public class ExampleSuite extends TestSuite {
	/**
	 * ��Ҫ���Ե���
	 */
	public ExampleSuite() {
		addTestSuite(MathTest.class);
		addTestSuite(ContactTest.class);
		addTestSuite(SomeTest.class);
	}
}
