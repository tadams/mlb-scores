package com.tadams;

import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

public class MLBScoresApplication {

    private static final Logger logger = LoggerFactory.getLogger(MLBScoresApplication.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH = "/mlb-scores";
    private static final String CONFIG_LOCATION = "com.tadams.config";
    private static final String MAPPING_URL = "/";
    private static final String DEFAULT_PROFILE = "dev";
    private static final String WEBAPP_DIRECTORY = "webapp";

    public static void main(String[] args) throws Exception {
        new MLBScoresApplication().startJetty(getPortFromArgs(args));
    }

    private static int getPortFromArgs(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
            }
        }
        logger.debug("No server port configured, falling back to {}", DEFAULT_PORT);
        return DEFAULT_PORT;
    }

    private void startJetty(int port) throws Exception {
        logger.debug("Starting server at port {}", port);
        Server server = new Server(port);

        server.setHandler(getServletContextHandler());

        addRuntimeShutdownHook(server);

        server.start();
        logger.info("Server started at port {}", port);
        server.join();
    }

    private static ServletContextHandler getServletContextHandler() throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setErrorHandler(null);

        contextHandler.setResourceBase(new ClassPathResource(WEBAPP_DIRECTORY).getURI().toString());
        contextHandler.setContextPath(CONTEXT_PATH);

        // JSP
        contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
        contextHandler.addServlet(JspServlet.class, "*.jsp");

        // Spring
        WebApplicationContext webAppContext = getWebApplicationContext();
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webAppContext);
        ServletHolder springServletHolder = new ServletHolder("mvc-dispatcher", dispatcherServlet);
        contextHandler.addServlet(springServletHolder, MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(webAppContext));

        return contextHandler;
    }

    private static WebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
        return context;
    }

    private static void addRuntimeShutdownHook(final Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (server.isStarted()) {
                    server.setStopAtShutdown(true);
                    try {
                        server.stop();
                    } catch (Exception e) {
                        System.out.println("Error while stopping jetty server: " + e.getMessage());
                        logger.error("Error while stopping jetty server: " + e.getMessage(), e);
                    }
                }
            }
        }));
    }

}
