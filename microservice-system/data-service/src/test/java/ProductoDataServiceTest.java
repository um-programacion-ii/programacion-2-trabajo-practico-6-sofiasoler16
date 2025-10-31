import dataService.dto.ProductoDTO;
import dataService.dto.ProductoRequest;
import dataService.entity.Categoria;
import dataService.entity.Inventario;
import dataService.entity.Producto;
import dataService.exception.CategoriaNoEncontradaException;
import dataService.exception.ProductoNoEncontradoException;
import dataService.repository.CategoriaRepository;
import dataService.repository.ProductoRepository;
import dataService.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoDataServiceTest {

    @Mock private ProductoRepository productoRepository;
    @Mock private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Categoria categoria;
    private Producto producto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNombre("Cat Test");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Prod");
        producto.setDescripcion("Desc");
        producto.setPrecio(BigDecimal.valueOf(123));
        producto.setCategoria(categoria);

        Inventario inv = new Inventario();
        inv.setProducto(producto);
        inv.setCantidad(5);
        inv.setStockMinimo(2);
        inv.setFechaActualizacion(LocalDateTime.now());
        producto.setInventario(inv);
    }

    @Test
    void crear_ok_conCategoriaExistente_persisteYDevuelveDTO() {
        ProductoRequest req = new ProductoRequest();
        req.setNombre("Nuevo");
        req.setDescripcion("Nuevo desc");
        req.setPrecio(BigDecimal.valueOf(100));
        req.setCategoriaId(10L);
        req.setStock(8);
        req.setStockMinimo(3);

        when(categoriaRepository.findById(10L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> {
            Producto p = i.getArgument(0);
            p.setId(99L);
            return p;
        });

        ProductoDTO dto = productoService.crear(req);

        assertNotNull(dto);
        assertEquals(99L, dto.getId());
        assertEquals("Nuevo", dto.getNombre());
        assertEquals("Cat Test", dto.getCategoriaNombre());
        assertEquals(8, dto.getStock());
        assertFalse(dto.getStockBajo());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void crear_falla_siCategoriaNoExiste() {
        ProductoRequest req = new ProductoRequest();
        req.setNombre("X");
        req.setPrecio(BigDecimal.TEN);
        req.setCategoriaId(404L);
        req.setStock(1);
        req.setStockMinimo(1);

        when(categoriaRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontradaException.class, () -> productoService.crear(req));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void actualizar_ok_actualizaCamposEInventario() {
        ProductoRequest req = new ProductoRequest();
        req.setNombre("Editado");
        req.setDescripcion("Edit desc");
        req.setPrecio(BigDecimal.valueOf(77));
        req.setCategoriaId(10L);
        req.setStock(2);
        req.setStockMinimo(2);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(categoriaRepository.findById(10L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        ProductoDTO dto = productoService.actualizar(1L, req);

        assertEquals("Editado", dto.getNombre());
        assertEquals(2, dto.getStock());
        assertTrue(dto.getStockBajo());
    }

    @Test
    void eliminar_falla_siNoExiste() {
        when(productoRepository.existsById(999L)).thenReturn(false);
        assertThrows(ProductoNoEncontradoException.class, () -> productoService.eliminar(999L));
    }

    @Test
    void porCategoriaNombre_listaOk_siExisteCategoria() {
        when(categoriaRepository.findByNombre("Cat Test")).thenReturn(Optional.of(categoria));
        when(productoRepository.findByCategoriaId(10L)).thenReturn(List.of(producto));

        var lista = productoService.porCategoriaNombre("Cat Test");
        assertEquals(1, lista.size());
        assertEquals("Prod", lista.get(0).getNombre());
    }

    @Test
    void porId_ok_cuandoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        var dto = productoService.porId(1L);
        assertEquals("Prod", dto.getNombre());
    }

    @Test
    void porId_falla_cuandoNoExiste() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ProductoNoEncontradoException.class, () -> productoService.porId(999L));
    }
}
