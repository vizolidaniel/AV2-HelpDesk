package br.com.daniel.config;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.util.Optional;

public class ServerConfig {
    private static final Log log = LogFactory.getLog(ServerConfig.class);

    public static void initServer() throws LifecycleException, ServletException {
        final Tomcat server = new Tomcat();

        final int webPort = Optional
                .ofNullable(System.getenv("PORT"))
                .map(Integer::parseInt)
                .orElse(Configs.SERVER_DEFAULT_PORT);

        server.setPort(webPort);
        server.setBaseDir("./.tomcat");

        final String webappLocation = new File("src/main/webapp").getAbsolutePath();

        final StandardContext ctx = (StandardContext) server.addWebapp("", webappLocation);
        System.out.println("configuring app with basedir: " + webappLocation);

        final File additionWebInfClasses = new File("target/classes");
        final WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(
                resources,
                "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(),
                "/"
        ));
        ctx.setResources(resources);

        server.start();

        log.info(String.format("Servidor iniciado em http://localhost:%s", Configs.SERVER_DEFAULT_PORT));
        server.getServer().await();
    }
}
