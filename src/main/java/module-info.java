module application.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
   // requires jsapi;
    requires freetts;
    requires com.google.common;
    requires voicerss.tts;
    requires java.desktop;
    requires javafx.media;

    opens application.app to javafx.fxml;
    exports application.app;
}