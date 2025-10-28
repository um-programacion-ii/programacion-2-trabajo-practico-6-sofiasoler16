package dataService.service;

import dataService.entity.Inventario;
import dataService.exception.ProductoNoEncontradoException;
import dataService.repository.InventarioRepository;
import dataService.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventarioService {

    private final InventarioRepository inventarioRepo;
    private final ProductoRepository productoRepo;

    public InventarioService(InventarioRepository inventarioRepo, ProductoRepository productoRepo) {
        this.inventarioRepo = inventarioRepo;
        this.productoRepo = productoRepo;
    }

    public Inventario actualizarStock(Long productoId, int nuevaCantidad) {
        var producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado: " + productoId));

        var inv = producto.getInventario();
        inv.setCantidad(nuevaCantidad);
        inv.setFechaActualizacion(LocalDateTime.now());
        return inventarioRepo.save(inv);
    }

    public List<Inventario> obtenerProductosConStockBajo() {
        return inventarioRepo.findConStockBajo();
    }
}