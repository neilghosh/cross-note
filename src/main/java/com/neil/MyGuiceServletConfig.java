package com.neil;

import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;

public class MyGuiceServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new SitebricksModule() {
                    @Override
                    protected void configureSitebricks() {
                        scan(NotebookService.class.getPackage());
                        System.out.println("****** Scan complete ******");
                    }
                }
                , new ServletModule() {
                    @Override
                    protected void configureServlets() {
                        bind(com.neil.NotebookServlet.class).in(Singleton.class);
                        serve("/servlet").with(com.neil.NotebookServlet.class);
                    }
                }
        );
    }
}
