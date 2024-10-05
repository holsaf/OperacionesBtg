package com.btg.operaciones.services.notificador;

import com.btg.operaciones.dto.NotificacionDto;
import com.btg.operaciones.enums.TipoNotificacionEnum;
import com.btg.operaciones.enums.TipoTransaccionEnum;
import com.btg.operaciones.handlers.CustomExceptions.ServidorException;
import com.btg.operaciones.services.utils.EmailService;
import com.btg.operaciones.services.utils.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificadorServiceImpl implements NotificadorService {

    @Value("${constantes.mensaje.apertura.fondo}")
    private String msgAperturaFondo;

    @Value("${constantes.mensaje.cancelacion.fondo}")
    private String msgCancelacionFondo;

    @Value("${constantes.mensaje.asunto.apertura.fondo}")
    private String asuntoAperturaFondo;

    @Value("${constantes.mensaje.asunto.cancelacion.fondo}")
    private String asuntoCancelacionFondo;

    @Value("${constantes.mensaje.error.notificacion}")
    private String errorNotificacion;
    private final SmsService smsService;
    private final EmailService emailService;


    public NotificadorServiceImpl(SmsService smsService, EmailService emailService) {
        this.smsService = smsService;
        this.emailService = emailService;
    }

    @Override
    public void notificar(NotificacionDto notificacionDto) {
        try{
            if(notificacionDto.getTipoTransaccion().equals(TipoTransaccionEnum.APERTURAR.name())) {
                if (notificacionDto.getTipoNotificacion().equals(TipoNotificacionEnum.EMAIL.name())) {
                    emailService.sendEmail(notificacionDto.getCliente().getCorreoElectronico(), asuntoAperturaFondo, msgAperturaFondo + notificacionDto.getNombreFondo());
                } else if (notificacionDto.getTipoNotificacion().equals(TipoNotificacionEnum.SMS.name())) {
                    smsService.sendSms(notificacionDto.getCliente().getNumeroCelular(), msgAperturaFondo + notificacionDto.getNombreFondo());
                }
            } else if (notificacionDto.getTipoTransaccion().equals(TipoTransaccionEnum.CANCELAR.name())) {
                if (notificacionDto.getTipoNotificacion().equals(TipoNotificacionEnum.EMAIL.name())) {
                    emailService.sendEmail(notificacionDto.getCliente().getCorreoElectronico(), asuntoCancelacionFondo, msgCancelacionFondo + notificacionDto.getNombreFondo());
                } else if (notificacionDto.getTipoNotificacion().equals(TipoNotificacionEnum.SMS.name())) {
                    smsService.sendSms(notificacionDto.getCliente().getNumeroCelular(), msgCancelacionFondo + notificacionDto.getNombreFondo());

                }
            }
        }catch (Exception e){
            throw new ServidorException(errorNotificacion);
        }

    }
}
