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

package org.brandao.ioc;

import junit.framework.TestCase;
import org.brandao.ioc.TestHelper.MyBean;
import org.brandao.ioc.TestHelper.MyEnum;
import org.brandao.ioc.TestHelper.MyFactory;
import org.brandao.ioc.TestHelper.MySimpleBean;

/**
 *
 * @author Afonso Brandao
 */
public class TestIOCContainer extends TestCase{

    public void testSimpleBean(){
        IOCContainer iocContainer = new IOCContainer();
        iocContainer.addBean(TestHelper.MySimpleBean.class);
        assertNotNull( iocContainer.getBean(TestHelper.MySimpleBean.class) );
    }

    public void testConstructorInject(){
        IOCContainer iocContainer = new IOCContainer();
        iocContainer
            .addBean(MyBean.class)
                .addConstructiorArg(MySimpleBean.class);

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testConstructorInjectRef(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer
            .addBean(MySimpleBean.class);

        iocContainer
            .addBean(MyBean.class)
                .addConstructiorRefArg(MySimpleBean.class.getName());

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testPropertyInject(){
        IOCContainer iocContainer = new IOCContainer();
        iocContainer
            .addBean(MyBean.class)
                .addProperty("bean",MySimpleBean.class);

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testPropertyInjectRef(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer
            .addBean(MySimpleBean.class);

        iocContainer
            .addBean(MyBean.class)
                .addPropertyRef("bean",MySimpleBean.class.getName());

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testFactoryStaticMethod(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer
            .addBean(MyEnum.class)
                .addConstructiorArg("VALUE2")
                .setFactoryMethod("valueOf");

        MyEnum instance = (MyEnum) iocContainer.getBean(MyEnum.class);
        assertEquals( MyEnum.VALUE2, instance );
    }

    public void testFactory(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer
            .addBean(MyFactory.class);

        BeanBuilder beanBuilder = iocContainer.addBean(MySimpleBean.class);
        beanBuilder.setFactoryMethod("getInstance");
        beanBuilder.setFactory(MyFactory.class.getName());
        
        MySimpleBean instance = (MySimpleBean)iocContainer
                    .getBean(MySimpleBean.class);

        assertNotNull( instance );
    }

}
