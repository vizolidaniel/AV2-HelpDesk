package br.com.daniel.domain;

public enum ServiceRequestStatus {
    OPEN("Aberto"),
    CLOSED("Fechado"),
    IN_ANALYSIS("Em Análise");

    private final String translation;

    ServiceRequestStatus(final String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return this.translation;
    }
}
