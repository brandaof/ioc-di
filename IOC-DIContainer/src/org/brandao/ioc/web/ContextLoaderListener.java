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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.brandao.ioc.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.brandao.ioc.RootContainer;
import org.brandao.ioc.ScopeType;
import org.brandao.ioc.scope.GlobalScope;

/**
 *
 * @author Afonso Brandao
 */
public class ContextLoaderListener implements ServletContextListener{

    public void contextInitialized(ServletContextEvent arg0) {
        RootContainer container = RootContainer.getInstance();
        GlobalScope scope = new GlobalScope( arg0.getServletContext() );
        container.getScopeManager().register(ScopeType.GLOBAL.toString(), scope);
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        RootContainer container = RootContainer.getInstance();
        container.getScopeManager().remove(ScopeType.GLOBAL.toString());
    }

}
