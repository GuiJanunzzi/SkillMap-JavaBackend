package br.com.fiap.skillmap.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Busca uma mensagem no arquivo de properties correspondente
     * ao Locale (língua) da requisição atual.
     * @param code A chave da mensagem (ex: "error.notfound")
     * @return A mensagem traduzida
     */
    public String get(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    /**
     * Busca uma mensagem e injeta argumentos nela.
     * @param code A chave da mensagem (ex: "error.notfound.id")
     * @param args Os argumentos para injetar (ex: o ID)
     * @return A mensagem traduzida e formatada
     */
    public String get(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}