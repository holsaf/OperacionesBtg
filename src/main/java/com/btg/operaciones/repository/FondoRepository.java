package com.btg.operaciones.repository;

import com.btg.operaciones.entities.Fondo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FondoRepository extends MongoRepository<Fondo, String> {
}
