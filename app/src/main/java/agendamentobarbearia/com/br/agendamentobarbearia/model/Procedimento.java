package agendamentobarbearia.com.br.agendamentobarbearia.model;

/**
 * Created by Marcello on 25/05/2017.
 */

public enum Procedimento {

    CA("Cabelo"),
    BA("Barba"),
    BI("Bigode"),
    TO("Todos");

    private String procedimento;

    Procedimento(String procedimento){
        this.procedimento = procedimento;
    }

    @Override
    public String toString() {
        return procedimento;
    }
}
