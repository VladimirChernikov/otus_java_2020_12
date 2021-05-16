package ru.otus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import ru.otus.domain.Client;

public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {

    List<Client> findAll();

    Page<Client> findAll(Pageable pageble);

    Optional<Client> findById(long id);

    List<Client> findByName(String name);

}
