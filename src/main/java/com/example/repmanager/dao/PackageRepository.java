package com.example.repmanager.dao;

import com.example.repmanager.entities.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Bu arayüz (interface), PackageEntity nesnesi için veri tabanı işlemlerini yönetir.
 * Spring Data JPA sayesinde CRUD (Create, Read, Update, Delete) işlemleri otomatik olarak sağlanır.
 */
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

    /**
     * Paket ismine ve versiyonuna göre veri tabanında bir PackageEntity nesnesi arar.
     *
     * @param name Aranacak paketin adını belirtir.
     * @param version Aranacak paketin versiyonunu belirtir.
     * @return Bulunursa PackageEntity nesnesini içeren bir Optional nesnesi döner.
     */
    Optional<PackageEntity> findByNameAndVersion(String name, String version);
}
