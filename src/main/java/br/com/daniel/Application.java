package br.com.daniel;

import org.apache.catalina.LifecycleException;

import javax.servlet.ServletException;

import static br.com.daniel.config.ServerConfig.initServer;

public class Application {

    public static void main(String[] args) throws LifecycleException, ServletException {
        initServer();
    }
}
