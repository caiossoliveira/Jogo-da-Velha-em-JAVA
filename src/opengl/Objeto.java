package opengl;


import com.jogamp.opengl.util.texture.Texture;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henrique
 */
public class Objeto {
    private Texture corpo;
    private int largura;
    private int altura;
    private int id;
    private float x;
    private float y;
    private float z;
    private boolean ativo;
    private boolean mudou_imagem;
    private String nova_imagem;
    private String padrao;
    private boolean andando;
    private int altura_r;
    
    private int largura_r;
    private int objeto_x;
    private int objeto_y;
    
    private float x_atual;
    private float y_atual;
    private boolean visivel;

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }
    

    public int getObjeto_x() {
        return objeto_x;
    }

    public void setObjeto_x(int objeto_x) {
        this.objeto_x = objeto_x;
    }

    public int getObjeto_y() {
        return objeto_y;
    }

    public void setObjeto_y(int objeto_y) {
        this.objeto_y = objeto_y;
    }

    public float getX_atual() {
        return x_atual;
    }

    public void setX_atual(float x_atual) {
        this.x_atual = x_atual;
    }

    public float getY_atual() {
        return y_atual;
    }

    public void setY_atual(float y_atual) {
        this.y_atual = y_atual;
    }
    
    
    public Texture getCorpo() {
        return corpo;
    }

    public void setCorpo(Texture corpo) {
        this.corpo = corpo;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isMudou_imagem() {
        return mudou_imagem;
    }

    public void setMudou_imagem(boolean mudou_imagem) {
        this.mudou_imagem = mudou_imagem;
    }

    public String getNova_imagem() {
        return nova_imagem;
    }

    public void setNova_imagem(String nova_imagem) {
        this.nova_imagem = nova_imagem;
    }

    public String getPadrao() {
        return padrao;
    }

    public void setPadrao(String padrao) {
        this.padrao = padrao;
    }
    public boolean isAndando() {
        return andando;
    }

    public void setAndando(boolean andando) {
        this.andando = andando;
    }

    public int getAltura_r() {
        return altura_r;
    }

    public void setAltura_r(int altura_r) {
        this.altura_r = altura_r;
    }

    public int getLargura_r() {
        return largura_r;
    }

    public void setLargura_r(int largura_r) {
        this.largura_r = largura_r;
    }
}
