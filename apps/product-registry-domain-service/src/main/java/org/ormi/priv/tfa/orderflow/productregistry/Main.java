package org.ormi.priv.tfa.orderflow.productregistry;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Point d'entrée principal de l'application du domaine du registre de produits.
 * <p>
 * Cette classe initialise et démarre Quarkus pour exécuter l'application,
 * en attendant que le processus soit arrêté. Elle définit également la classe interne
 * {@link ProductRegistryDomainApplication} qui implémente {@link QuarkusApplication}
 * et contient la logique principale d'exécution.
 */


@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(
            ProductRegistryDomainApplication.class,
            (exitCode, exception) -> {},
            args);
    }

    public static class ProductRegistryDomainApplication implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
