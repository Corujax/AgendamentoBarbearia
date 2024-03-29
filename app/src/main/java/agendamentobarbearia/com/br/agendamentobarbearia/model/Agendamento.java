package agendamentobarbearia.com.br.agendamentobarbearia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Calendar;

import agendamentobarbearia.com.br.agendamentobarbearia.util.ImageUtil;

/**
 * Created by Marcello on 25/05/2017.
 */

public class Agendamento implements Serializable {

    private long id;
    private String chave;
    private String fotoAntes;
    private String fotoDepois;
    private String nome;
    private String telefone;
    private Calendar dataHora;
    private Procedimento procedimento;
    private String imageAntes;
    private String imageDepois;
    @JsonIgnore
    private boolean novo;
    @JsonIgnore
    private boolean importing;

    public Agendamento(){
        novo = false;
        importing = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getFotoAntes() {
        return fotoAntes;
    }

    public void setFotoAntes(String fotoAntes) {
        this.fotoAntes = fotoAntes;
    }

    public String getFotoDepois() {
        return fotoDepois;
    }

    public void setFotoDepois(String fotoDepois) {
        this.fotoDepois = fotoDepois;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Calendar getDataHora() {
        return dataHora;
    }

    public void setDataHora(Calendar dataHora) {
        this.dataHora = dataHora;
    }

    public Procedimento getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
    }

    public boolean isNovo() {
        return novo;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
    }

    public boolean isImporting() {
        return importing;
    }

    public void setImporting(boolean importing) {
        this.importing = importing;
    }

    public String getImageAntes(){
        if(!importing && this.fotoAntes != null){
            imageAntes = ImageUtil.bitmapToBase64(fotoAntes);
        }
        return imageAntes;
    }

    public void setImageAntes(String imageAntes){
        this.imageAntes = imageAntes;
    }

    public String getImageDepois(){
        if(!importing && this.fotoDepois != null){
            imageDepois = ImageUtil.bitmapToBase64(fotoDepois);
        }
        return imageDepois;
    }

    public void setImageDepois(String imageDepois){
        this.imageDepois = imageDepois;
    }

    @Override
    public String toString() {
        return nome + " " + procedimento.toString();
    }
}
