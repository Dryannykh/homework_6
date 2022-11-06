package ru.digitalhabits.homework_6;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;

public class Certificates {

    private KeyStore loadKeyStore() {
        String PATH_TO_CACERTS = "/lib/security/cacerts";
        String cacertsPath = PATH_TO_CACERTS.replace("/", File.separator);
        String fullFilePath = System.getProperty("java.home") + cacertsPath;
        try (FileInputStream inputStream = new FileInputStream(fullFilePath)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            String password = "changeit";
            keyStore.load(inputStream, password.toCharArray());
            return keyStore;
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<X509Certificate> getCertificates() throws KeyStoreException {
        KeyStore keyStore = loadKeyStore();
        List<X509Certificate> certificates = new ArrayList<>();
        assert keyStore != null;
        List<String> aliases = getAliases(keyStore);
        for(String alias : aliases) {
            certificates.add((X509Certificate)keyStore.getCertificate(alias));
        }
        return certificates;
    }

    private List<String> getAliases(KeyStore keyStore) throws KeyStoreException {
        List<String> aliasesList = new ArrayList<>();
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            aliasesList.add(aliases.nextElement());
        }
        return aliasesList;
    }

    public List<Long> getDuration(List<X509Certificate> certificates) {
        List<Long> duration = new ArrayList<>();
        for(X509Certificate certificate : certificates) {
            Date from = certificate.getNotBefore();
            Date to = certificate.getNotAfter();
            duration.add(Duration.between(from.toInstant(), to.toInstant()).toDays());
        }
        return duration;
    }

    public List<String> getName(List<X509Certificate> certificates) throws InvalidNameException {
        List<String> names = new ArrayList<>();
        Optional<Rdn> rdnCN;
        Optional<Rdn> rdnOU;
        String name = "";
        for(X509Certificate certificate : certificates) {
            String fullName = certificate.getSubjectX500Principal().getName();
            LdapName ldapName = new LdapName(fullName);
            List<Rdn> rdnList = ldapName.getRdns();
            rdnCN = rdnList.stream().filter(r -> r.getType().equalsIgnoreCase("CN")).findFirst();
            if(rdnCN.isPresent())
                name = rdnCN.get().getValue().toString();
            else {
                rdnOU = rdnList.stream().filter(r -> r.getType().equalsIgnoreCase("OU")).findFirst();
                if(rdnOU.isPresent())
                    name = rdnOU.get().getValue().toString();
                else if(!rdnCN.isPresent() && !rdnOU.isPresent())
                    name = fullName;
            }
            names.add(name);
        }
        return names;
    }
}
