package com.yashu.demo;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(MyResource.class);
        register(AlienResource.class);
    }
}
