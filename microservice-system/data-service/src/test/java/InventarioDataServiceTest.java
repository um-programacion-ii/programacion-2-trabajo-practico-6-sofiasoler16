
import dataService.entity.Inventario;
import dataService.entity.Producto;
import dataService.exception.ProductoNoEncontradoException;
import dataService.repository.InventarioRepository;
import dataService.repository.ProductoRepository;
import dataService.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioDataServiceTest {

    @Mock
    private InventarioRepository inventarioRepository; 
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Producto producto;
    private Inventario inv;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        inv = new Inventario();
        inv.setProducto(producto);
        inv.setCantidad(2);
        inv.setStockMinimo(5);
        inv.setFechaActualizacion(LocalDateTime.now());
    }

}