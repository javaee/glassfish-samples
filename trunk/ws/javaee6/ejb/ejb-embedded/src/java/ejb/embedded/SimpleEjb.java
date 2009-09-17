package sample.ejb.embedded;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.annotation.security.PermitAll;
import javax.annotation.security.DenyAll;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import sample.ejb.embedded.persistence.SimpleEntity;

/**
 * @author mvatkina
 */
@Stateless
public class SimpleEjb {

    @PersistenceContext(unitName="embedded_test") EntityManager em;

    @PermitAll
    public int verify() {
        String result = null;
        Query q = em.createNamedQuery("SimpleEntity.findAll");
        Collection entities = q.getResultList();
        int s = entities.size();
        for (Object o : entities) {
            SimpleEntity se = (SimpleEntity)o;
            System.out.println("Found: " + se.getName());
        }

        return s;
   }

    @PermitAll
    public void insert(int num) {
        for (int i = 1; i <= num; i++) {
            System.out.println("Inserting # " + i);
            SimpleEntity e = new SimpleEntity(i);
            em.persist(e);
        }
    }
}
