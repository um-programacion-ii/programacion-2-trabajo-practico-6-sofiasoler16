import businessService.client.DataServiceClient;
import businessService.dto.CategoriaDTO;
import businessService.exception.MicroserviceCommunicationException; // ⚠️ ajustá si usás otra
import businessService.service.CategoriaBusinessService;
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
class CategoriaBusinessServiceTest {

    @Mock private DataServiceClient dataServiceClient;

    @InjectMocks
    private CategoriaBusinessService categoriaBusinessService; // ⚠️ asegurate que existe

    @Test
    void obtenerTodas_ok() {
        var c1 = new CategoriaDTO(); c1.setId(1L); c1.setNombre("A");
        var c2 = new CategoriaDTO(); c2.setId(2L); c2.setNombre("B");

        when(dataServiceClient.obtenerTodasLasCategorias()).thenReturn(List.of(c1, c2));

        var lista = categoriaBusinessService.obtenerTodasLasCategorias(); // ⚠️ firma exacta
        assertEquals(2, lista.size());
        assertEquals("A", lista.get(0).getNombre());
    }

    @Test
    void obtenerTodas_mapeaErrorFeign() {
        Request req = Request.create(Request.HttpMethod.GET, "/data/categorias",
                java.util.Map.of(), null, StandardCharsets.UTF_8, new RequestTemplate());
        FeignException fe = new FeignException.InternalServerError("x", req, null, null);

        when(dataServiceClient.obtenerTodasLasCategorias()).thenThrow(fe);

        assertThrows(MicroserviceCommunicationException.class, () -> categoriaBusinessService.obtenerTodasLasCategorias());
    }
}
