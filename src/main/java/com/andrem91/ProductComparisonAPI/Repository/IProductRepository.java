package com.andrem91.ProductComparisonAPI.Repository;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface para repositório de produtos.
 * Define contrato para acesso a dados.
 */
public interface IProductRepository {
    
    /** Recupera todos os produtos. */
    List<ProductEntity> findAll();
    
    /** Encontra produto por ID. */
    Optional<ProductEntity> findById(Long id);
    
    /** Recupera produtos por IDs. */
    List<ProductEntity> findByIds(List<Long> ids);
    
    /** Conta total de produtos. */
    long count();
}
