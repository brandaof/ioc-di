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
import org.brandao.ioc.ObjectFactory;
import org.brandao.ioc.Scope;
import org.brandao.ioc.ScopeType;

/**
 *
 * @author Afonso Brandao
 */
public class RequestScope implements Scope{

    private ThreadLocal<ServletRequest> requests;

    public RequestScope( ThreadLocal<ServletRequest> requests ){
        this.requests = requests;
    }

    public String getName(){
        return ScopeType.REQUEST.toString();
    }

    public Object get(String beanName, ObjectFactory factory) {
        ServletRequest request = requests.get();
        Object value = request.getAttribute(beanName);
        if( value == null ){
            value = factory.getObject();
            request.setAttribute(beanName, value);
        }
        return value;
    }

    public void remove(String name) {
        ServletRequest request = requests.get();
        request.removeAttribute(name);
    }

}
