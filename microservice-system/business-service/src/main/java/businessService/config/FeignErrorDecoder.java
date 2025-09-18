package businessService.config;

import businessService.exception.BusinessConflictException;
import businessService.exception.ResourceNotFoundException;
import businessService.exception.UpstreamServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        return switch (status) {
            case 400 -> new IllegalArgumentException("Solicitud inválida al data-service (" + methodKey + ")");
            case 404 -> new ResourceNotFoundException("Recurso no encontrado en data-service (" + methodKey + ")");
            case 409 -> new BusinessConflictException("Conflicto de negocio (" + methodKey + ")");
            case 500, 502, 503, 504 -> new UpstreamServiceException("data-service no disponible (HTTP " + status + ")");
            default -> defaultDecoder.decode(methodKey, response);
        };
    }

}
