package com.btg.operaciones.repository;

import com.btg.operaciones.entities.Transaccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends MongoRepository<Transaccion, String> {

    List<Transaccion> findAllById(String Id);

    List<Transaccion> findAllByIdCliente(String idCliente);

    Page<Transaccion> findAllByIdCliente(String idCliente, Pageable page);

}
