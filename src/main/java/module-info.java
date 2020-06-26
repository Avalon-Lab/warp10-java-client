open module avalon.lab.warp10client {

    requires java.base;
    requires java.net.http;
    requires javax.inject;

    exports fr.avalonlab.warp10;

    exports fr.avalonlab.warp10.exception;

    exports fr.avalonlab.warp10.dsl;
    exports fr.avalonlab.warp10.dsl.framework;

    exports fr.avalonlab.warp10.injection;
}
