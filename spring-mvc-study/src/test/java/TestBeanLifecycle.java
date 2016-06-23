import org.junit.Test;

import base.UnitTestBase;

public class TestBeanLifecycle extends UnitTestBase {
	public TestBeanLifecycle () {
		super("classpath:spring-lifecycle.xml");
	}
	
	@Test
	public void test1() {
		super.getBean("beanLifeCycle");
	}
}
