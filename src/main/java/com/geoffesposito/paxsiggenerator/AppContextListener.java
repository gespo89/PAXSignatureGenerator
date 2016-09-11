package com.geoffesposito.paxsiggenerator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Geoff on 9/9/2016.
 */
@WebListener
public class AppContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        context.setAttribute("paxen", Arrays.asList(PAX.values()));
        context.setAttribute("badgetypes", Stream.of(BadgeType.values()).map(BadgeType::name).collect(Collectors.toList()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
