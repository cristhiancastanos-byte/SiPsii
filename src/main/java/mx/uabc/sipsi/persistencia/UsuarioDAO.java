package mx.uabc.sipsi.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import mx.uabc.sipsi.entidad.Usuario;

@ApplicationScoped
public class UsuarioDAO {

    @Inject
    EntityManager em;

    public Usuario buscarPorUsuarioOCorreo(String userOrMail) {
        String u = userOrMail == null ? "" : userOrMail.trim();
        if (u.isEmpty()) return null;

        TypedQuery<Usuario> q = em.createQuery(
                "SELECT u FROM Usuario u WHERE lower(u.usuario)=:x OR lower(u.correo)=:x",
                Usuario.class);
        q.setParameter("x", u.toLowerCase());

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
