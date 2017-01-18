package opengl;

import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_NICEST;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import com.jogamp.opengl.GL2;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static javax.media.opengl.GL.GL_SRC_ALPHA;

public class Jogo extends GLJPanel implements GLEventListener, KeyListener, MouseListener{
    private float angulo = 0.0f;
    private FPSAnimator animator;
    List<Objeto> objetos;
    Cerquilha cerquilha;
    private Objeto p1;
    private Objeto p2;
    private GLU glu;
    float x = 0.01f;
    private static int MAX_FRAMES = 60;
    Objeto fundoPrincipal;
    int largura;
    int altura;
    boolean[][] jogadaPosicao;
    Objeto[][] objetosCogumelos;
    Objeto[][] objetosFlores;
    boolean doisJogadores;
    boolean iniciouJogo;
    boolean vezJogadorPrincipal;
    boolean terminouJogo = false;
    int vitoria;
    public javax.swing.JLabel txtResultado;
    
    public Jogo() {
        //INICIALIZAÇÃO DO JOGO
        super(new GLCapabilities(GLProfile.getDefault()));
        this.addGLEventListener(this);
        this.addKeyListener(this);
        this.addMouseListener(this);
        doisJogadores = false;
        iniciouJogo = false;
        vezJogadorPrincipal = true;
        jogadaPosicao = new boolean[3][3];
        objetosCogumelos = new Objeto[3][3];
        objetosFlores = new Objeto[3][3];
        txtResultado = null;
        vitoria = 0;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        //INICIA O OPEN GL
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        animator = new FPSAnimator(this,MAX_FRAMES);
        animator.start();
        
        glu = new GLU();
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
        objetos = criarObjetos();
        
        
    }
    
    //CRIAÇÃO DE OBJETO
    private List<Objeto> criarObjetos()
    {
        List<Objeto> retornar = new ArrayList<>();
        //FUNDO
        Objeto fundo = criarObjeto("/background/tabuleiro.png", "fundo", 0, 0, 0, 100, 100, 1,  true );
        fundoPrincipal = fundo;
        retornar.add(fundo);
        //Cerquilha
        Objeto img_cerquilha = criarObjeto("/bloco/interrogacao01.png", "/bloco/interrogacao0", 0, 0, 0, 100,100, 1, true);
        criarCerquilha(img_cerquilha);
        retornar.add(img_cerquilha);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //Adicionando Flores na Matriz
                Objeto flor = criarObjeto("/objetos/fl"+i+j+".png", "fl", 0, 0, 0, 100, 100, 1, false);
                objetosFlores[j][i] = flor;
                //Adicionando Cogumelos na Matriz
                Objeto cogumelo = criarObjeto("/objetos/cg"+i+j+".png", "fl", 0, 0, 0, 100, 100, 1, false );
                objetosCogumelos[j][i] = cogumelo;
                retornar.add(flor);
                retornar.add(cogumelo);
            }
        }
        
        return retornar;
    }

    private void criarCerquilha(Objeto img_cerquilha) {
        cerquilha = new Cerquilha();
        cerquilha.setObjeto(img_cerquilha);
        cerquilha.setMaximoFrames(4);
        cerquilha.setFrameAtual(0);
        cerquilha.setContadorFrames(0);
        cerquilha.setTempoTrocaFrames(90);
    }

    private Objeto criarObjeto(String caminho, String padrao, int x, int y, int z, int alt, int larg, int id, boolean visivel) {
        Objeto objeto = new Objeto();
        objeto.setCorpo(carregarTextura(caminho));
        objeto.setX(x);
        objeto.setY(y);
        objeto.setZ(z);
        objeto.setAltura(alt);
        objeto.setLargura(larg);
        objeto.setObjeto_x(0);
        objeto.setObjeto_y(0);
        objeto.setAndando(false);
        objeto.setAtivo(true);
        objeto.setId(id);
        objeto.setMudou_imagem(false);
        objeto.setPadrao(padrao);
        objeto.setVisivel(visivel);
        return objeto;
    }
    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        if (animator != null) {
            if (animator.isAnimating()) {
                animator.stop();
            }
            
        }
       // JOptionPane.showMessageDialog(this, "Fim de Jogo!");
    };
    @Override
    public void display(GLAutoDrawable drawable) {
        int larg = drawable.getSurfaceWidth();
        int alt = drawable.getSurfaceHeight();
        largura = larg;
        altura = alt;
        GL2 gl = configuracaoDisplayOpenGl(drawable);
        //PARTE LÓGICA DO PROJETO
        atualizacaoLogicaDasImagens();
        //PARTE FÍSICA DO PROJETO
        atualizacaoFisicaDasImagens(gl, alt, larg);        
       
       
    }
    private void atualizacaoLogicaDasImagens()
    {
        verificarFimJogo();
        if (terminouJogo) {
            exibirResultado();
        }else{
            atualizarFundo();
            
        }
        inteligenciaDoJogo();
    }
    private void exibirResultado()
    {
        if (vitoria == 0) {
            txtResultado.setText("Fim de Jogo: Empate!");
        }else if (vitoria == 1){
            txtResultado.setText("Fim de Jogo: Vitória Jogador 1!");
            
        }else{
            
            txtResultado.setText("Fim de Jogo: Vitória Jogador 2!");
        }
        cerquilha.getObjeto().setMudou_imagem(true);
        cerquilha.getObjeto().setPadrao("/bloco/desabilitado");
        cerquilha.getObjeto().setNova_imagem("");
    }
    private void verificarFimJogo(){
        terminouJogo = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!jogadaPosicao[j][i]) {
                    terminouJogo = false;
                }
            }
            
        }
        for (int i = 0; i < 3; i++) {
            if (jogadaPosicao[i][0] && jogadaPosicao[i][1] && jogadaPosicao[i][2]) {
                if (objetosCogumelos[i][0].isVisivel() && objetosCogumelos[i][1].isVisivel() && objetosCogumelos[i][2].isVisivel()) {
                    terminouJogo = true;
                    vitoria = 1;
                }
                if (objetosFlores[i][0].isVisivel() && objetosFlores[i][1].isVisivel() && objetosFlores[i][2].isVisivel()) {
                    terminouJogo = true;
                    vitoria = 2;
                }
            }
            if (jogadaPosicao[0][i] && jogadaPosicao[1][i] && jogadaPosicao[2][i]) {
                if (objetosCogumelos[0][i].isVisivel() && objetosCogumelos[1][i].isVisivel() && objetosCogumelos[2][i].isVisivel()) {
                    terminouJogo = true;
                    vitoria = 1;
                }
                if (objetosFlores[0][i].isVisivel() && objetosFlores[1][i].isVisivel() && objetosFlores[2][i].isVisivel()) {
                    terminouJogo = true;
                    vitoria = 2;
                }
            }
        }
        if (jogadaPosicao[0][0] && jogadaPosicao[1][1] && jogadaPosicao[2][2]) {
                if (objetosCogumelos[0][0].isVisivel() && objetosCogumelos[1][1].isVisivel() && objetosCogumelos[2][2].isVisivel()) {
                    terminouJogo = true;
                    vitoria = 1;
                }
                if (objetosFlores[0][0].isVisivel() && objetosFlores[1][1].isVisivel() && objetosFlores[2][1].isVisivel()) {
                    terminouJogo = true;
                    vitoria = 2;
                }
        }
        if (jogadaPosicao[2][0] && jogadaPosicao[1][1] && jogadaPosicao[0][2]) {
                if (objetosCogumelos[2][0].isVisivel() && objetosCogumelos[1][1].isVisivel() && objetosCogumelos[0][2].isVisivel()) {
                    terminouJogo = true;
                    vitoria = 1;
                }
                if (objetosFlores[2][0].isVisivel() && objetosFlores[1][1].isVisivel() && objetosFlores[0][2].isVisivel()) {
                    terminouJogo = true;
                    vitoria = 2;
                }
        }
    }
    private void inteligenciaDoJogo()
    {
        if (doisJogadores || vezJogadorPrincipal || terminouJogo) {
            return;
        }
        int posicaoEscX = -1;
        int posicaoEscY = -1;
        boolean jogou = false;
        while (!jogou) {
            posicaoEscX = (int)(Math.random() * (3));
            posicaoEscY = (int)(Math.random() * (3));
            if (!jogadaPosicao[posicaoEscX][posicaoEscY]) {
                adicionarObjeto(posicaoEscX, posicaoEscY);
                jogou = true;
            }
        }
    }
    private void atualizarFundo()
    {
        cerquilha.setContadorFrames(cerquilha.getContadorFrames()+1);
        int tempo = cerquilha.getTempoTrocaFrames()*MAX_FRAMES / 1000;
        if (cerquilha.getContadorFrames() >= tempo) {
            cerquilha.setContadorFrames(0);
            cerquilha.setFrameAtual((cerquilha.getFrameAtual() +1) % cerquilha.getMaximoFrames());
            cerquilha.getObjeto().setMudou_imagem(true);
            cerquilha.getObjeto().setNova_imagem(String.valueOf((cerquilha.getFrameAtual()+1)));
        }
    }

    private void atualizacaoFisicaDasImagens(GL2 gl, int alt, int larg) {
        for (Objeto objeto : objetos) {
            
            atualizar_objeto(objeto, gl, alt, larg);
            
        }
    }

    private GL2 configuracaoDisplayOpenGl(GLAutoDrawable drawable) throws GLException {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        // gl.glColor3f(1.0f, 1.0f, 0.0f);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        return gl;
    }
  
    private void atualizar_objeto(Objeto objeto, GL2 gl, int alt, int larg)
    {
        calculoDaPosicaoDoObjeto(objeto, larg, alt);
        if (objeto.isAtivo()) {
            verificarMudancaImagemObjeto(objeto);
            
            configuracaoDeAtualizarImagemOpenGL(gl, objeto);
                
            verificarMovimentacaoObjeto(objeto, gl, larg, alt);
        
            objeto.getCorpo().disable(gl);
        }
    }

    private void verificarMudancaImagemObjeto(Objeto objeto) {
        if (objeto.isMudou_imagem()) {
            objeto.setMudou_imagem(false);
            objeto.setCorpo(carregarTextura(
                    objeto.getPadrao()+objeto.getNova_imagem()+".png"));
        }
    }

    private void calculoDaPosicaoDoObjeto(Objeto objeto, int larg, int alt) {
        //resetar_posicao(objeto, alt, larg);
        float xd = (float)Math.round(objeto.getX_atual())*100/larg;
        objeto.setX(objeto.getX() + xd);
        objeto.setX_atual(0);
        float yd = (float)Math.round(objeto.getY_atual())*100/alt;
        objeto.setY(objeto.getY() + yd);
        objeto.setY_atual(0);
        objeto.setAltura_r(Math.round((objeto.getAltura()*alt)/100));
        objeto.setLargura_r(Math.round((objeto.getLargura()*larg)/100));
        objeto.setObjeto_x(Math.round((objeto.getX()*larg)/100));
        objeto.setObjeto_y(Math.round((objeto.getY()*alt)/100));
    }

    private void verificarMovimentacaoObjeto(Objeto objeto, GL2 gl, int larg, int alt) {
        //

          desenhar_objeto(objeto, gl);
    }

    private void configuracaoDeAtualizarImagemOpenGL(GL2 gl, Objeto objeto) throws GLException {
        // set the texture parameters to allow for properly displaying
        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        objeto.getCorpo().enable(gl);
        objeto.getCorpo().bind(gl);
        // gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);
    }
    private void desenhar_objeto(Objeto objeto, GL2 gl)
    {
        if (!objeto.isVisivel()) {
            return;
        }
        gl.glBegin(GL2.GL_QUADS);
        // Face frontal
        gl. glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(objeto.getObjeto_x(), objeto.getObjeto_y(), objeto.getZ());
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(objeto.getObjeto_x()+objeto.getLargura_r(),
                objeto.getObjeto_y(), objeto.getZ());
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(objeto.getObjeto_x()+objeto.getLargura_r(),
                objeto.getObjeto_y()+objeto.getAltura_r(), objeto.getZ());
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(objeto.getObjeto_x(),
                objeto.getObjeto_y()+objeto.getAltura_r(), objeto.getZ());
        gl.glEnd();
    }
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
         GL2 gl = drawable.getGL().getGL2();
//
//        if (height == 0) {
//            height = 1;
//        }
//        float aspect = (float) width / height;

        // Configurar área de visualização para o tamanho da janela
        gl.glViewport(0, 0, width, height);

        // Definir projeção em perspectiva 
        gl.glMatrixMode(GL_PROJECTION);  // Matriz de projeção 
        gl.glLoadIdentity();             // Resetar projection matrix
        glu.gluOrtho2D(0.0f, width, 0.0f, height);
        //glu.gluPerspective(90.0, aspect, 0.01, 100.0); // fovy, aspect, zNear, zFar

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    public Texture carregarTextura(String PATH) {
        try {
            InputStream stream = getClass().getResourceAsStream(PATH);
            TextureData data
                    = TextureIO.newTextureData(
                            GLProfile.getDefault(), stream,
                            false, TextureIO.PNG);
            return TextureIO.newTexture(data);
        } catch (Exception e) {
            System.out.println("ERRO carregar textura: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
    }

   
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!iniciouJogo || (!doisJogadores && !vezJogadorPrincipal) || terminouJogo) {
            return;
        }
        int xJogo = Math.round(e.getX() * 100 / largura);
        int yJogo = Math.round(e.getY() * 100 / altura);
        
        int posicaoX = atualizarPosicaoMouseEmRelacaoAsCerquilhas(xJogo);
        int posicaoY = atualizarPosicaoMouseEmRelacaoAsCerquilhas(yJogo);
        adicionarObjeto(posicaoX, posicaoY);
        
    }
    private void adicionarObjeto(int posicaoX, int posicaoY)
    {
        if (jogadaPosicao[posicaoX][posicaoY]) {
            return;
        }
        jogadaPosicao[posicaoX][posicaoY] = true;
        if (vezJogadorPrincipal) {
            
            objetosCogumelos[posicaoX][posicaoY].setVisivel(true);
        }else{
            objetosFlores[posicaoX][posicaoY].setVisivel(true);
            
        }
        vezJogadorPrincipal = !vezJogadorPrincipal;
    }
    private int atualizarPosicaoMouseEmRelacaoAsCerquilhas(int posicaoMouseJogo)
    {
        if (posicaoMouseJogo <= 33) {
            return 0;
        }else if (posicaoMouseJogo <= 66)
        {
            return 1;
        }
        return 2;
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void DoisJogadores(boolean doisJogadores)
    {
        this.doisJogadores = doisJogadores;
        iniciouJogo = true;
    }
   
}
