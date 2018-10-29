module avalon.lab.warp10client {

    requires java.base;
    requires java.net.http;

    exports fr.avalonlab.warp10;

    exports fr.avalonlab.warp10.exception;

    exports fr.avalonlab.warp10.DSL;
    exports fr.avalonlab.warp10.DSL.framework;

    exports fr.avalonlab.warp10.injection;
}
