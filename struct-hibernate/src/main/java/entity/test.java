/** 2016年5月13日  test.java   **/
package entity;

import java.math.BigDecimal;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * @author YEMASKY
 *
 */
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigDecimal b1=new BigDecimal(Double.toString(7));  
        BigDecimal b2=new BigDecimal(Double.toString(6));  
        double sss = b1.divide(b2,10,BigDecimal.ROUND_HALF_UP).doubleValue();  
		double last_page = Math.ceil(7/6);
		System.out.println(sss);
		/*
		// TODO Auto-generated method stub
		Configuration config = new Configuration().configure();
		//ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.getCurrentSession();
		SchemaExport export = new SchemaExport(config);
		export.create(true, true);
		*/
	}

}
