package dataService.repository;

import dataService.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    @Query(" SELECT i FROM Inventario i WHERE i.cantidad <= i.stockMinimo")
    List<Inventario> findConStockBajo();
}
