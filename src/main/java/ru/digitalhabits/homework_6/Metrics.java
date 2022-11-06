package ru.digitalhabits.homework_6;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.stereotype.Component;

import javax.naming.InvalidNameException;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.List;

@Component
public class Metrics {

    public Metrics(MeterRegistry registry) throws KeyStoreException, InvalidNameException {
        Certificates certificates = new Certificates();
        List<X509Certificate> certificatesList = certificates.getCertificates();
        List<String> names = certificates.getName(certificatesList);
        List<Long> duration = certificates.getDuration(certificatesList);
        for(int i = 0; i < names.size(); i++) {
            registry.gauge(names.get(i), Tags.of("Validity", String.valueOf(duration.get(i))),i);
        }
    }
}
