package com.apg.clientes.infraestructura.persistencia;

import com.apg.clientes.dominio.modelo.Cliente;
import com.apg.clientes.dominio.repositorio.ClienteRepositorio;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ClienteRepositorioImpl implements ClienteRepositorio {

    private final EntityManager entityManager;

    @Inject
    public ClienteRepositorioImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Cliente crear(Cliente cliente) {
        entityManager.persist(cliente);
        return cliente;
    }

    @Override
    @Transactional
    public Cliente actualizar(Cliente cliente) {
        return entityManager.merge(cliente);
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return entityManager.find(Cliente.class, id);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return entityManager.createQuery("FROM Cliente c ORDER BY c.id", Cliente.class)
                .getResultList();
    }

    @Override
    public List<Cliente> buscarPorPais(String codigoPais) {
        return entityManager.createQuery(
                        "FROM Cliente c WHERE UPPER(c.pais) = :pais ORDER BY c.id", Cliente.class)
                .setParameter("pais", codigoPais.toUpperCase())
                .getResultList();
    }

    @Override
    public boolean existeCorreo(String correo){
        Long count = entityManager.createQuery(
                        "SELECT COUNT(c) FROM Cliente c WHERE LOWER(c.correo) = :correo", Long.class)
                .setParameter("correo", correo.toLowerCase())
                .getSingleResult();
        return count > 0;
    };

    @Override
    @Transactional
    public boolean eliminarPorId(Long id) {
        Cliente cliente = buscarPorId(id);
        if (cliente != null) {
            entityManager.remove(cliente);
            return true;
        }
        return false;
    }
}