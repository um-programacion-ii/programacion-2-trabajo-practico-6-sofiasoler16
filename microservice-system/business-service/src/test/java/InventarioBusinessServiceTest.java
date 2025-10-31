package businessService.service;

import businessService.client.DataServiceClient;
import businessService.dto.InventarioDTO;
import businessService.dto.ProductoDTO;
import businessService.exception.ResourceNotFoundException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private InventarioBusinessService inventarioBusinessService;

    @Test
    void obtenerProductosConStockBajo_ok() {
        // Armamos un ProductoDTO
        ProductoDTO prod = new ProductoDTO();
        prod.setId(1L);
        // si tu ProductoDTO requiere más campos, setéalos acá

        // Armamos el InventarioDTO con ese producto
        InventarioDTO dto = new InventarioDTO();
        dto.setProducto(prod);
        dto.setCantidad(2);
        dto.setStockMinimo(5);

        when(dataServiceClient.obtenerProductosConStockBajo()).thenReturn(List.of(dto));

        var res = inventarioBusinessService.obtenerProductosConStockBajo();
        assertEquals(1, res.size());
        assertNotNull(res.get(0).getProducto());
        assertEquals(1L, res.get(0).getProducto().getId());
    }

    @Test
    void obtenerProductosConStockBajo_mapeaError() {
        Request req = Request.create(
                Request.HttpMethod.GET,
                "/data/inventario/stock-bajo",
                java.util.Map.of(),
                null,
                StandardCharsets.UTF_8,
                new RequestTemplate()
        );
        FeignException fe = new FeignException.InternalServerError("boom", req, null, null);

        when(dataServiceClient.obtenerProductosConStockBajo()).thenThrow(fe);

        assertThrows(ResourceNotFoundException.class,
                () -> inventarioBusinessService.obtenerProductosConStockBajo());
    }
}
