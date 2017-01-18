/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl;

/**
 *
 * @author Henrique
 */
public class Cerquilha {
    private Objeto objeto;
    private int maximoFrames;
    private int frameAtual;
    private int contadorFrames;
    private int tempoTrocaFrames;

    public int getContadorFrames() {
        return contadorFrames;
    }

    public void setContadorFrames(int contadorFrames) {
        this.contadorFrames = contadorFrames;
    }

    public int getTempoTrocaFrames() {
        return tempoTrocaFrames;
    }

    public void setTempoTrocaFrames(int tempoTrocaFrames) {
        this.tempoTrocaFrames = tempoTrocaFrames;
    }

    
    
    public int getFrameAtual() {
        return frameAtual;
    }

    public void setFrameAtual(int frameAtual) {
        this.frameAtual = frameAtual;
    }
    
    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public int getMaximoFrames() {
        return maximoFrames;
    }

    public void setMaximoFrames(int maximoFrames) {
        this.maximoFrames = maximoFrames;
    }
    
}
