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
package org.brandao.ioc.scope;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.brandao.ioc.ObjectFactory;
import org.brandao.ioc.Scope;
import org.brandao.ioc.ScopeType;

/**
 *
 * @author Afonso Brandao
 */
public class SessionScope implements Scope{

    private ThreadLocal<ServletRequest> requests;

    public SessionScope( ThreadLocal<ServletRequest> requests ){
        this.requests = requests;
    }
    
    public String getName(){
        return ScopeType.SESSION.toString();
    }

    public Object get(String beanName, ObjectFactory factory) {
        HttpSession session = ((HttpServletRequest)requests.get()).getSession();
        Object value = session.getAttribute(beanName);
        if( value == null ){
            value = factory.getObject();
            session.setAttribute(beanName, value);
        }
        return value;
    }

    public void remove(String name) {
        HttpSession session = ((HttpServletRequest)requests.get()).getSession();
        session.removeAttribute(name);
    }

}
