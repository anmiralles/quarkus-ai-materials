package me.amiralles.materials.client;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.util.List;
import java.util.function.Function;

@QuarkusMain
public class McpClientServerApplication {

    public static void main(String[] args) {
        main(args, (remainingArgs) -> null);
    }

    public static void main(String[] args, Function<List<String>, Void> onArgsProcessed) {
        List<String> remainingArgs = McpCliConfigSource.setupConfigSource(args);
        onArgsProcessed.apply(remainingArgs);

        Quarkus.run(null,
            (exitCode, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                }
                System.exit(exitCode);
            },
            args);
    }
}
