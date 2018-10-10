package fr.avalonlab.warp10.injection.guide;

import com.google.inject.AbstractModule;

import fr.avalonlab.warp10.injection.Warp10InjectableClient;
import fr.avalonlab.warp10.injection.Warp10Client;

public class Warp10ClientModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Warp10Client.class).to(Warp10InjectableClient.class);
  }
}
