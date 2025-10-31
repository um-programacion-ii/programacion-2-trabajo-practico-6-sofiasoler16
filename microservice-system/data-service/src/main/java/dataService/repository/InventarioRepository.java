package dataService.repository;

import dataService.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    @Query(" SELECT i FROM Inventario i WHERE i.cantidad <= i.stockMinimo")
    List<Inventario> findConStockBajo();
    Optional<Inventario> findByProductoId(Long productoId);
}

