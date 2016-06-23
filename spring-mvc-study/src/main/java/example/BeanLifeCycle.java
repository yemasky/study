package example;

//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;

public class BeanLifeCycle {// implements InitializingBean, DisposableBean {
	public void start() {
		System.out.println("Bean start");
	}

	public void stop() {
		System.out.println("Bean stop");
	}
	
	/*public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Bean destroy");
		
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Bean afterPropertiesSet");
	}*/
	
}
