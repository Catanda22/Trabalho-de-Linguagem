package model;

public enum StatusConsulta {
    AGENDADA("Agendada"),
    CONFIRMADA("Confirmada"),
    REALIZADA("Realizada"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusConsulta(String d) { this.descricao = d; }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return descricao; }
}
