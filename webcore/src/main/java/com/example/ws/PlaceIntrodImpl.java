package com.example.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;
import com.example.service.PlaceIntrod;

@WebServiceClient(name = "PlaceIntrodImpl", wsdlLocation = "http://127.0.0.1/kfserver?wsdl", targetNamespace = "http://impl.service.test.com/")
public class PlaceIntrodImpl extends Service {

	public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://impl.service.test.com/", "PlaceIntrodImpl");
    public final static QName PlaceIntrodImplPort = new QName("http://impl.service.test.com/", "PlaceIntrodImplPort");
    static {
        URL url = null;
        try {
            url = new URL("http://127.0.0.1/kfserver?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(PlaceIntrodImpl.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://127.0.0.1/kfserver?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public PlaceIntrodImpl(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PlaceIntrodImpl(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PlaceIntrodImpl() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     * 
     * @return
     *     returns PlaceIntrod
     */
    @WebEndpoint(name = "PlaceIntrodImplPort")
    public PlaceIntrod getPlaceIntrodImplPort() {
        return super.getPort(PlaceIntrodImplPort, PlaceIntrod.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PlaceIntrod
     */
    @WebEndpoint(name = "PlaceIntrodImplPort")
    public PlaceIntrod getPlaceIntrodImplPort(WebServiceFeature... features) {
        return super.getPort(PlaceIntrodImplPort, PlaceIntrod.class, features);
    }


}
