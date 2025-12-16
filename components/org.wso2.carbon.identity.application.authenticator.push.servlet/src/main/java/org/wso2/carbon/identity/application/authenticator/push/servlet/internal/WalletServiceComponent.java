package org.wso2.carbon.identity.application.authenticator.push.servlet.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.http.HttpService;
import org.wso2.carbon.identity.application.authenticator.push.servlet.servlet.WalletResponseServlet;

import java.util.Dictionary;
import java.util.Hashtable;

@Component(
        name = "org.wso2.carbon.identity.application.authenticator.push.servlet.internal.WalletServiceComponent",
        immediate = true
)
public class WalletServiceComponent {

    private static final Log log = LogFactory.getLog(WalletServiceComponent.class);
    private HttpService httpService;

    @Activate
    protected void activate(ComponentContext context) {
        try {
            // Register the Servlet at this context path
            String contextPath = "/wallet/response";
            
            // You can pass init params here if needed
            Dictionary<String, String> servletParams = new Hashtable<>();
            
            httpService.registerServlet(contextPath, new WalletResponseServlet(), servletParams, null);
            
            if (log.isDebugEnabled()) {
                log.debug("Wallet Response Servlet activated at: " + contextPath);
            }
        } catch (Exception e) {
            log.error("Error while registering Wallet Servlet", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        if (httpService != null) {
            // Clean up by unregistering the path
            httpService.unregister("/wallet/response");
        }
    }

    @Reference(
            name = "osgi.http.service",
            service = HttpService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetHttpService"
    )
    protected void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    protected void unsetHttpService(HttpService httpService) {
        this.httpService = null;
    }
}