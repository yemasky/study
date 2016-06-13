/** 2016年5月13日  TestStudents.java   **/
package entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

/**
 * @author YEMASKY
 *
 */
public class TestStudents {
	@Test
	public void testSchemaExport(){
		Configuration config = new Configuration().configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
		@SuppressWarnings("unused")
		Session session = sessionFactory.getCurrentSession();
		SchemaExport export = new SchemaExport(config);
		export.create(true, true);
		
	}
	
	@Test
	public void testSchema() {
		
		/*
		Configuration config = new Configuration().configure();
		//ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata(); 
		SchemaExport schemaExport = new SchemaExport(); 
		schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);*/


		
	}
}
