package br.com.daniel;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.ServletException;
import java.io.File;
import java.util.Optional;

public class Application {

    public static void main(String[] args) throws LifecycleException, ServletException {
        final Tomcat server = new Tomcat();

        final int webPort = Optional
                .ofNullable(System.getenv("PORT"))
                .map(Integer::parseInt)
                .orElse(8090);

        server.setPort(webPort);

        final String webappLocation = new File("src/main/webapp").getAbsolutePath();

        StandardContext ctx = (StandardContext) server.addWebapp("", webappLocation);
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

        server.getServer().await();
    }
}
