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

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import junit.framework.TestCase;
import org.brandao.ioc.TestHelper.CustomScope;
import org.brandao.ioc.TestHelper.MyBean;
import org.brandao.ioc.TestHelper.MyBeanByContructor;
import org.brandao.ioc.TestHelper.MyEnum;
import org.brandao.ioc.TestHelper.MyFactory;
import org.brandao.ioc.TestHelper.MySimpleBean;
import org.brandao.ioc.web.ContextLoaderListener;
import org.brandao.ioc.web.RequestContextListener;

/**
 *
 * @author Afonso Brandao
 */
public class IOCContainerTest extends TestCase{

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

    public void testConstructorInjectMultipleArgs(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer.addBean(String.class)
                .addConstructiorArg( "Texto..." );

        iocContainer.addBean(Integer.class)
                .addConstructiorArg( 100 );

        iocContainer.addBean("myBean", MySimpleBean.class, ScopeType.SINGLETON );

        iocContainer
            .addBean(MyBeanByContructor.class)
                .addConstructiorArg()
                .addConstructiorArg()
                .addConstructiorArg();

        MyBeanByContructor instance = (MyBeanByContructor) iocContainer.getBean(MyBeanByContructor.class);
        assertNotNull( instance );
        assertEquals( "Texto...", instance.getStringValue() );
        assertEquals( 100, instance.getIntValue() );
        assertEquals( iocContainer.getBean(MySimpleBean.class), instance.getBean() );
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

    public void testGlobalScope(){
        MockServletContext servletContext = new MockServletContext();
        ServletContextEvent sce = new ServletContextEvent( servletContext );
        ContextLoaderListener listener = new ContextLoaderListener();

        try{
            listener.contextInitialized(sce);
            IOCContainer iocContainer = new IOCContainer();
            iocContainer
                .addBean("myBean", TestHelper.MySimpleBean.class, ScopeType.GLOBAL);

            Object instance = iocContainer.getBean("myBean");
            Object instance2 = iocContainer.getBean("myBean");
            assertTrue( instance == instance2 );
            assertNotNull( servletContext.getAttribute("myBean") );
        }
        finally{
            listener.contextDestroyed(sce);
        }

    }

    public void testSingletonScope(){
        IOCContainer iocContainer = new IOCContainer();
        iocContainer
            .addBean("myBean", TestHelper.MySimpleBean.class, ScopeType.SINGLETON);

        Object instance = iocContainer.getBean("myBean");
        Object instance2 = iocContainer.getBean("myBean");
        assertTrue( instance == instance2 );
    }

    public void testPrototypeScope(){
        IOCContainer iocContainer = new IOCContainer();
        iocContainer
            .addBean("myBean", TestHelper.MySimpleBean.class, ScopeType.PROTOTYPE);

        Object instance = iocContainer.getBean("myBean");
        Object instance2 = iocContainer.getBean("myBean");
        assertTrue( instance != instance2 );
    }

    public void testRequestScope(){
        MockServletContext servletContext = new MockServletContext();
        ServletContextEvent sce = new ServletContextEvent( servletContext );
        ContextLoaderListener listener = new ContextLoaderListener();
        try{
            listener.contextInitialized(sce);
            RequestContextListener requestListener = new RequestContextListener();
            MockHttpServletRequest request = new MockHttpServletRequest();
            ServletRequestEvent sre = new ServletRequestEvent(servletContext, request);

            try{
                requestListener.requestInitialized(sre);
                IOCContainer iocContainer = new IOCContainer();
                iocContainer
                    .addBean("myBean", TestHelper.MySimpleBean.class, ScopeType.REQUEST);

                Object instance = iocContainer.getBean("myBean");
                Object instance2 = iocContainer.getBean("myBean");
                assertTrue( instance == instance2 );
                assertNotNull( request.getAttribute("myBean") );
            }
            finally{
                requestListener.requestDestroyed(sre);
            }
        }
        finally{
            listener.contextDestroyed(sce);
        }

    }

    public void testSessionScope(){
        MockServletContext servletContext = new MockServletContext();
        ServletContextEvent sce = new ServletContextEvent( servletContext );
        ContextLoaderListener listener = new ContextLoaderListener();
        try{
            listener.contextInitialized(sce);
            RequestContextListener requestListener = new RequestContextListener();
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpSession session = new MockHttpSession();
            ServletRequestEvent sre = new ServletRequestEvent(servletContext, request);
            request.setSession(session);

            try{
                requestListener.requestInitialized(sre);
                IOCContainer iocContainer = new IOCContainer();
                iocContainer
                .addBean("myBean", TestHelper.MySimpleBean.class, ScopeType.SESSION);

                Object instance = iocContainer.getBean("myBean");
                Object instance2 = iocContainer.getBean("myBean");
                assertTrue( instance == instance2 );
                assertNotNull( request.getSession().getAttribute("myBean") );
            }
            finally{
                requestListener.requestDestroyed(sre);
            }
        }
        finally{
            listener.contextDestroyed(sce);
        }

    }

    public void testRootContainer(){
        RootContainer rootContainer = RootContainer.getInstance();

        rootContainer.addBean(TestHelper.MySimpleBean.class);

        IOCContainer iocContainer = new IOCContainer();
        
        assertNotNull( iocContainer.getBean(TestHelper.MySimpleBean.class) );
    }

    public void testCustomScope(){
        CustomScope customScope = new CustomScope();
        IOCContainer iocContainer = new IOCContainer();
        iocContainer.getScopeManager()
                .register(customScope.getName(), customScope);

        iocContainer
            .addBean( "myBean", TestHelper.MySimpleBean.class,
                ScopeType.valueOf( customScope.getName() ) );

        Object instance = iocContainer.getBean("myBean");

        assertNotNull( instance );
    }

    public void testPropertyAutoInject(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer
            .addBean("bean",MySimpleBean.class);

        iocContainer
            .addBean(MyBean.class)
                .addProperty("bean");

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testPropertyAutoInjectByType(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer
            .addBean(MySimpleBean.class);

        iocContainer
            .addBean(MyBean.class)
                .addProperty("bean");

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testConstructorArgAutoInject(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer
            .addBean("bean",MySimpleBean.class);

        iocContainer
            .addBean(MyBean.class).addConstructiorArg();

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testConstructorArgAutoInjectWithFactory(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer.addBean("myEnumId", String.class)
                .addConstructiorArg("VALUE2");
        
        iocContainer
            .addBean(MyEnum.class)
                .addConstructiorArg()
                .setFactoryMethod("valueOf");

        MyEnum instance = (MyEnum) iocContainer.getBean(MyEnum.class);
        assertEquals( MyEnum.VALUE2, instance );
    }

}
