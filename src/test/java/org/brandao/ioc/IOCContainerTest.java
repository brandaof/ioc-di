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

package org.brandao.ioc;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;
import java.text.DateFormatSymbols;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import junit.framework.TestCase;
import org.brandao.ioc.TestHelper.CustomScope;
import org.brandao.ioc.TestHelper.MyBean;
import org.brandao.ioc.TestHelper.MyBeanByContructor;
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
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();
        iocContainer.addBean(TestHelper.MySimpleBean.class);
        assertNotNull( iocContainer.getBean(TestHelper.MySimpleBean.class) );
    }

    public void testConstructorInject(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();
        iocContainer
            .addBean(MyBean.class)
                .addConstructorArg(MySimpleBean.class);

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testConstructorInjectMultipleArgs(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();

        iocContainer.addBean(String.class)
                .addConstructorArg( "Texto..." );

        iocContainer.addBean(Integer.class)
                .addConstructorArg( 100 );

        iocContainer.addBean("myBean", MySimpleBean.class, ScopeType.SINGLETON );

        iocContainer
            .addBean(MyBeanByContructor.class)
                .addConstructorArg()
                .addConstructorArg()
                .addConstructorArg();

        MyBeanByContructor instance = (MyBeanByContructor) iocContainer.getBean(MyBeanByContructor.class);
        assertNotNull( instance );
        assertEquals( "Texto...", instance.getStringValue() );
        assertEquals( 100, instance.getIntValue() );
        assertEquals( iocContainer.getBean(MySimpleBean.class), instance.getBean() );
    }

    public void testConstructorInjectRef(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();

        iocContainer
            .addBean(MySimpleBean.class);

        iocContainer
            .addBean(MyBean.class)
                .addConstructorRefArg(MySimpleBean.class.getName());

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testPropertyInject(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();
        iocContainer
            .addBean(MyBean.class)
                .addProperty("bean",MySimpleBean.class);

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    public void testPropertyInjectRef(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();

        iocContainer
            .addBean(MySimpleBean.class);

        iocContainer
            .addBean(MyBean.class)
                .addPropertyRef("bean",MySimpleBean.class.getName());

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    /*
    public void testFactoryStaticMethod(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer
            .addBean(MyEnum.class)
                .addConstructorArg("VALUE2")
                .setFactoryMethod("valueOf");

        MyEnum instance = (MyEnum) iocContainer.getBean(MyEnum.class);
        assertEquals( MyEnum.VALUE2, instance );
    }
    */
    public void testFactory(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();

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
            BuilderIOCContainer iocContainer = new BuilderIOCContainer();
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
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();
        iocContainer
            .addBean("myBean", TestHelper.MySimpleBean.class, ScopeType.SINGLETON);

        Object instance = iocContainer.getBean("myBean");
        Object instance2 = iocContainer.getBean("myBean");
        assertTrue( instance == instance2 );
    }

    public void testPrototypeScope(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();
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
                BuilderIOCContainer iocContainer = new BuilderIOCContainer();
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
            HttpSessionEvent sessionEvent = new HttpSessionEvent( (HttpSession)session );

            request.setSession(session);

            try{
                requestListener.requestInitialized(sre);
                requestListener.sessionCreated(sessionEvent);
                BuilderIOCContainer iocContainer = new BuilderIOCContainer();
                iocContainer
                .addBean("myBean", TestHelper.MySimpleBean.class, ScopeType.SESSION);

                Object instance = iocContainer.getBean("myBean");
                Object instance2 = iocContainer.getBean("myBean");
                assertTrue( instance == instance2 );
                assertNotNull( request.getSession().getAttribute("myBean") );
            }
            finally{
                requestListener.requestDestroyed(sre);
                requestListener.sessionDestroyed(sessionEvent);
            }
        }
        finally{
            listener.contextDestroyed(sce);
        }

    }

    public void testCustomScope(){
        CustomScope customScope = new CustomScope();
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();
        iocContainer.getScopeManager()
                .register(customScope.getName(), customScope);

        iocContainer
            .addBean( "myBean", TestHelper.MySimpleBean.class,
                ScopeType.valueOf( customScope.getName() ) );

        Object instance = iocContainer.getBean("myBean");

        assertNotNull( instance );
    }

    public void testPropertyAutoInject(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();

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
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();

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
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();

        iocContainer
            .addBean("bean",MySimpleBean.class);

        iocContainer
            .addBean(MyBean.class).addConstructorArg();

        MyBean instance = (MyBean) iocContainer.getBean(MyBean.class);
        assertNotNull( instance );
        assertNotNull( instance.getBean() );
    }

    /*
    public void testConstructorArgAutoInjectWithFactory(){
        IOCContainer iocContainer = new IOCContainer();

        iocContainer.addBean("myEnumId", String.class)
                .addConstructorArg("VALUE2");
        
        iocContainer
            .addBean(MyEnum.class)
                .addConstructorArg()
                .setFactoryMethod("valueOf");

        MyEnum instance = (MyEnum) iocContainer.getBean(MyEnum.class);
        assertEquals( MyEnum.VALUE2, instance );
    }
    */
    
    public void testAutoDefinition(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();

        iocContainer.addBean(String.class)
                .addConstructorArg( "Texto..." );

        iocContainer.addBean(Integer.class)
                .addConstructorArg( 100 );

        iocContainer.setAutoDefinitionProperty(true);
        iocContainer.setAutoDefinitionConstructor(true);
        MyBeanByContructor instance = (MyBeanByContructor) iocContainer.getBean(MyBeanByContructor.class);
        assertNotNull( instance );
        assertEquals( "Texto...", instance.getStringValue() );
        assertEquals( 100, instance.getIntValue() );
        assertNotNull( instance.getBean() );
    }

    public void testCoCConstructor(){
        BuilderIOCContainer iocContainer = new BuilderIOCContainer();
        iocContainer.addBean(String.class)
                .addConstructorArg("dd/MM/yyy");
        
        iocContainer.addBean(DateFormatSymbols.class);

        iocContainer.setAutoDefinitionConstructor(true);
        Object o = iocContainer.getBean(java.text.SimpleDateFormat.class);
    }
}
