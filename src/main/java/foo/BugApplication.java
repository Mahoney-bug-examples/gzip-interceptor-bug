package foo;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class BugApplication extends Application<BugConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BugApplication().run(args);
    }

    @Override
    public String getName() {
        return "gzip-interceptor-bug";
    }

    @Override
    public void initialize(final Bootstrap<BugConfiguration> bootstrap) {
    }

    @Override
    public void run(final BugConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new TestResource());
    }
}
