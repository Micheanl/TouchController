package top.fifthlight.mergetools.merger.plugin.expectactual;

import top.fifthlight.mergetools.merger.api.MergeEntry;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ServiceLoaderRegistrationEntry implements MergeEntry {
    private final String implementationClass;

    public ServiceLoaderRegistrationEntry(String implementationClass) {
        this.implementationClass = implementationClass;
    }

    @Override
    public void write(OutputStream output) {
        var writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        writer.println(implementationClass);
        writer.flush();
    }
}
