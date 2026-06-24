package model;

public enum Perfil {
    ADMIN("Administrador"),
    MEDICO("Médico"),
    RECEPCIONISTA("Recepcionista");

    private final String descricao;

    Perfil(String descricao) { this.descricao = descricao; }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return descricao; }
}
