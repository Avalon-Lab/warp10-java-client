package fr.avalonlab.warp10.injection;

import fr.avalonlab.warp10.Warp10;

import javax.inject.Inject;
import javax.inject.Named;

public class Warp10InjectableClient implements Warp10Client {

  private final String endPointUri;
  private final String writeToken;
  private final String readToken;

  @Inject
  public Warp10InjectableClient(@Named("warp10.endpoint") String endPointUri, @Named("warp10.token.write") String writeToken, @Named("warp10.token.read") String readToken) {
    this.endPointUri = endPointUri;
    this.readToken = readToken;
    this.writeToken = writeToken;
  }

  @Override
  public Warp10 builder() {
    return new Warp10(endPointUri, writeToken, readToken);
  }
}
