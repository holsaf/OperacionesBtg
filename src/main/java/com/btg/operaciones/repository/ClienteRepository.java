package com.btg.operaciones.repository;

import com.btg.operaciones.entities.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {

    double findSaldoById(String id);
}
