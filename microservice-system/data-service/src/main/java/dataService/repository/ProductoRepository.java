package dataService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dataService.entity.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByCategoriaNombreIgnoreCase(String nombre);
}
