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
