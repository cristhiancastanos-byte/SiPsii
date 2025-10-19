package mx.uabc.sipsi.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("sipsiPU");

    @Produces
    public EntityManager produceEntityManager() {
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em != null && em.isOpen()) em.close();
    }
}
