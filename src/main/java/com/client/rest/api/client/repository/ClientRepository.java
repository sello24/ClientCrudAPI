package com.client.rest.api.client.repository;

import com.client.api.entitiy.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByMobileNumber(String mobileNumber);
    Optional<Client> findByIdNumber(String idNumber);

    @Query("SELECT c FROM Client c " +
            "WHERE LOWER(c.firstName) like LOWER(concat('%', :searchTerm, '%')) " +
            "OR LOWER(c.idNumber) like LOWER(concat('%', :searchTerm, '%')) " +
            "OR LOWER(c.mobileNumber) like LOWER(concat('%', :searchTerm, '%')) "
    )
    List<Client> searchClient(@Param("searchTerm") String searchTerm);
}
