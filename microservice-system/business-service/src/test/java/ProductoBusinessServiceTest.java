import businessService.client.DataServiceClient;
import businessService.dto.ProductoDTO;
import businessService.dto.ProductoRequest;
import businessService.exception.ValidacionNegocioException;
import businessService.service.ProductoBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private ProductoBusinessService productoBusinessService;

    @Test
    void cuandoObtenerTodosLosProductos_entoncesRetornaLista() {
        // Arrange
        List<ProductoDTO> productosEsperados = Arrays.asList(
                new ProductoDTO(1L, "Producto 1", "Descripción 1", BigDecimal.valueOf(100), "Categoría 1", 10, false),
                new ProductoDTO(2L, "Producto 2", "Descripción 2", BigDecimal.valueOf(200), "Categoría 2", 5, true)
        );

        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(productosEsperados);

        // Act
        List<ProductoDTO> resultado = productoBusinessService.obtenerTodosLosProductos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Producto 1", resultado.get(0).getNombre());
        verify(dataServiceClient).obtenerTodosLosProductos();
    }

    @Test
    void cuandoCrearProductoConPrecioInvalido_entoncesLanzaExcepcion() {
        // Arrange
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto Test");
        request.setPrecio(BigDecimal.valueOf(-10));
        request.setStock(5);

        // Act & Assert
        assertThrows(ValidacionNegocioException.class, () -> {
            productoBusinessService.crearProducto(request);
        });

        verify(dataServiceClient, never()).crearProducto(any());
    }
}
