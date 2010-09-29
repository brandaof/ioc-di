/*
 * IOC-DI Container http://ioc-di.sourceforge.net/
 * Copyright (C) 2010 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.brandao.ioc.web;

import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.brandao.ioc.DestructionCallBackSupport;
import org.brandao.ioc.RootContainer;
import org.brandao.ioc.ScopeType;
import org.brandao.ioc.scope.RequestScope;
import org.brandao.ioc.scope.SessionScope;

/**
 *
 * @author Afonso Brandao
 */
public class RequestContextListener implements ServletRequestListener, HttpSessionListener{

    private ThreadLocal<ServletRequest> requests;

    public RequestContextListener(){
        this.requests = new ThreadLocal<ServletRequest>();
        RootContainer container = RootContainer.getInstance();
        RequestScope requestScope = new RequestScope( requests );
        SessionScope sessionScope = new SessionScope( requests );
        container.getScopeManager().register(ScopeType.REQUEST.toString(), requestScope);
        container.getScopeManager().register(ScopeType.SESSION.toString(), sessionScope);
    }
    
    public void requestDestroyed(ServletRequestEvent arg0) {
        try{
            DestructionCallBackSupport currentDequestDestruction =
                    RequestDestructionCallBackSupport.get();
            currentDequestDestruction.destroyAll();
        }
        finally{
            requests.remove();
            RequestDestructionCallBackSupport.remove();
        }

    }

    public void requestInitialized(ServletRequestEvent arg0) {
        requests.set(arg0.getServletRequest());
        RequestDestructionCallBackSupport.set();
    }

    public void sessionCreated(HttpSessionEvent arg0) {
        SessionDestructionCallBackSupport.set(arg0.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent arg0) {
        try{
            DestructionCallBackSupport currentDequestDestruction =
                    SessionDestructionCallBackSupport.get(arg0.getSession());
            currentDequestDestruction.destroyAll();
        }
        finally{
            SessionDestructionCallBackSupport.remove( arg0.getSession() );
        }
    }

}
