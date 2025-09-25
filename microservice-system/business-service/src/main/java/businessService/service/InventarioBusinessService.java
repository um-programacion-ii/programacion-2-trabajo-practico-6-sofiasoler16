package businessService.service;

import businessService.client.DataServiceClient;
import businessService.dto.CategoriaDTO;
import businessService.dto.InventarioDTO;
import businessService.exception.MicroserviceCommunicationException;
import businessService.exception.ResourceNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InventarioBusinessService {
    private final DataServiceClient dataServiceClient;

    public InventarioBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    public List<InventarioDTO> obtenerProductosConStockBajo() {
        try {
            return dataServiceClient.obtenerProductosConStockBajo();
        } catch (FeignException e) {
            log.error("Error al obtener Inventario productos", e);
            throw new ResourceNotFoundException("Error al encontrar productos con stock bajo");
        }
    }

}
