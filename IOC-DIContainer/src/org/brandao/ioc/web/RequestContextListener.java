/*
 * IOC-DI Container http://ioc-di.sourceforge.net/
 * Copyright (C) 2009 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * This library is free software. You can redistribute it
 * and/or modify it under the terms of the GNU General Public
 * License (GPL) version 3.0 or (at your option) any later
 * version.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Distributed WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 */

package org.brandao.ioc.web;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import org.brandao.ioc.RootContainer;
import org.brandao.ioc.ScopeType;
import org.brandao.ioc.scope.RequestScope;
import org.brandao.ioc.scope.SessionScope;

/**
 *
 * @author Afonso Brandao
 */
public class RequestContextListener implements ServletRequestListener{

    private ThreadLocal<ServletRequest> requests;

    public RequestContextListener(){
        this.requests = new ThreadLocal<ServletRequest>();
        RootContainer container = RootContainer.getInstance();
        RequestScope scope = new RequestScope( requests );
        SessionScope scope2 = new SessionScope( requests );
        container.getScopeManager().register(ScopeType.REQUEST.toString(), scope);
        container.getScopeManager().register(ScopeType.SESSION.toString(), scope2);
    }
    
    public void requestDestroyed(ServletRequestEvent arg0) {
        requests.remove();
    }

    public void requestInitialized(ServletRequestEvent arg0) {
        requests.set(arg0.getServletRequest());
    }

}
