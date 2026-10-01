package servicio;

import dto.AlertaDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AlertaSseService {

    private final List<SseEmitter> emisores = new CopyOnWriteArrayList<>();

    public SseEmitter suscribir() {
        // Timeout de 30 minutos (1,800,000 ms)
        SseEmitter emisor = new SseEmitter(1800000L);

        emisores.add(emisor);

        emisor.onCompletion(() -> emisores.remove(emisor));
        emisor.onTimeout(() -> emisores.remove(emisor));
        emisor.onError((e) -> emisores.remove(emisor));

        try {
            // Evento de confirmación de conexión para el cliente Angular
            emisor.send(SseEmitter.event()
                    .name("CONEXION_ESTABLECIDA")
                    .data(Map.of(
                            "mensaje", "Canal de telemetría y alertas en tiempo real conectado",
                            "emisoresActivos", emisores.size()
                    )));
        } catch (IOException e) {
            emisores.remove(emisor);
        }

        return emisor;
    }

    public void notificarNuevaAlerta(AlertaDTO alerta) {
        List<SseEmitter> muertos = new ArrayList<>();

        for (SseEmitter emisor : emisores) {
            try {
                emisor.send(SseEmitter.event()
                        .name("NUEVA_ALERTA")
                        .data(alerta));
            } catch (Exception e) {
                muertos.add(emisor);
            }
        }

        emisores.removeAll(muertos);
    }

    public void notificarCambioEstado(int idAlerta, String nuevoEstado) {
        List<SseEmitter> muertos = new ArrayList<>();

        for (SseEmitter emisor : emisores) {
            try {
                emisor.send(SseEmitter.event()
                        .name("CAMBIO_ESTADO")
                        .data(Map.of(
                                "idAlerta", idAlerta,
                                "nuevoEstado", nuevoEstado
                        )));
            } catch (Exception e) {
                muertos.add(emisor);
            }
        }

        emisores.removeAll(muertos);
    }
}
