package core.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Serialize {

	public static String objectToXml(Object obj, Class<?> load) {
		JAXBContext context;
		StringWriter writer = new StringWriter();
		try {
			context = JAXBContext.newInstance(load);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			//marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);

			marshaller.marshal(obj, writer);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString();
	}
}
