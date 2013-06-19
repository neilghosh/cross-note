package com.neil;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;

/**
 * This is going to be invoked when container is brought up
 */
public class MyGuiceServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                //Keep sending Guice the modules
                new SitebricksModule() {
                    @Override
                    protected void configureSitebricks() {
                        scan(NotebookService.class.getPackage());
                        //Should change this to logger, this is just to proove that
                        //sitebrick scans the classes for annotations like @At etc.
                        System.out.println("****** Scan complete ******");
                    }
                }
                , new ServletModule() {
                    @Override
                    protected void configureServlets() {
                        //Servlet classes have to be singleton to be consistent with servlet specification
                        //In tranditional cases web.xml config tell the container to do so I guess.
                        bind(com.neil.NotebookServlet.class).in(Singleton.class);
                        //Analogous to typcial servlet URL mappings
                        serve("/servlet").with(com.neil.NotebookServlet.class);
                    }
                }
        );
    }
}
