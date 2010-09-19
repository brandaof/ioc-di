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

import javax.servlet.ServletContext;
import org.brandao.ioc.ObjectFactory;
import org.brandao.ioc.Scope;
import org.brandao.ioc.ScopeType;

/**
 *
 * @author Afonso Brandao
 */
public class GlobalScope implements Scope{

    private ServletContext context;

    public GlobalScope( ServletContext context ){
        this.context = context;
    }

    public String getName(){
        return ScopeType.GLOBAL.toString();
    }

    public Object get(String beanName, ObjectFactory factory) {
        Object value = context.getAttribute(beanName);
        if( value == null ){
            value = factory.getObject();
            context.setAttribute(beanName, value);
        }
        return value;
    }

    public void remove(String name) {
        context.removeAttribute(name);
    }

}
