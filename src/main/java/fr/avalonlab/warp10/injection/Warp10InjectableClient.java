package fr.avalonlab.warp10.injection;

import fr.avalonlab.warp10.Warp10;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;

public class Warp10InjectableClient implements Warp10Client {

    private final String endPointUri;
    private final String writeToken;
    private final String readToken;
    private final String timeout;

     /**
     * @param endPointUri : URI of your Warp10 server
     * @param writeToken : Your Warp10 write token
     * @param readToken : Your Warp10 read token
     * @param timeout : (Nullable) a global timeout (in milliseconds) to used for fetch and exec
     */
    @Inject
    public Warp10InjectableClient(@Named("warp10.endpoint") String endPointUri, @Named("warp10.token.write") String writeToken, @Named("warp10.token.read") String readToken,
                                  @Named("warp10.timeout.millis") String timeout) {
        this.endPointUri = endPointUri;
        this.readToken = readToken;
        this.writeToken = writeToken;
        this.timeout = timeout;
    }

    @Override
    public Warp10 builder() {
        Duration globalTimeout = null;

        if (timeout != null && !timeout.isBlank()) {
            var longTimeout = Long.parseLong(timeout);
            globalTimeout = Duration.ofMillis(longTimeout);
        }

        return new Warp10(endPointUri, writeToken, readToken, globalTimeout);
    }
}
