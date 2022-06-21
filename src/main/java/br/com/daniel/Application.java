package br.com.daniel;

import org.glassfish.embeddable.*;

import java.io.File;

public class Application {
    public static void main(String[] args) throws GlassFishException {
        GlassFishProperties props = new GlassFishProperties();
        props.setPort("http-listener", 8080);

        GlassFish glassfish = GlassFishRuntime.bootstrap().newGlassFish(props);
        glassfish.start();

        Deployer deployer = glassfish.getDeployer();
        deployer.deploy(new File("target/HelpDesk.war"), "--name=HelpDesk", "--contextroot=/", "--force=true");
    }
}
