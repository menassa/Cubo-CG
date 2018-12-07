package projetocg;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.*;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.media.opengl.awt.GLJPanel;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_L;
import java.awt.event.KeyListener;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;

public class ProjetoCG_M2 extends GLJPanel implements GLEventListener, KeyListener {

	public static void main(String[] args) {
            JFrame window = new JFrame("CUBO 3D");
            GLCapabilities caps = new GLCapabilities(null);
            ProjetoCG_M2 panel = new ProjetoCG_M2(caps);
            window.setContentPane(panel);
            window.pack();
            window.setResizable(false);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);
            panel.requestFocusInWindow();
	}

	private float girarX, girarY, girarZ;   // variáveis para rotação
        GLUT GLUT = new GLUT();                 //instancia GLUT
        
        //Luz
        private static boolean luz;

	public ProjetoCG_M2(GLCapabilities capabilities) {
            super(capabilities);
            setPreferredSize(new Dimension(500,500));   //seta o tamanho da proporção janela-cubo
            addGLEventListener(this);                   //adiciona eventos do GL
            addKeyListener(this);                       //adiciona eventos de teclado
            girarX = 45;                                //seta 15 como grau inicial em x
            girarY = 45;                                //seta 15 como grau inicial em y
            girarZ = 0;                                 //seta 0 como grau inicial em z
	}
	
        @Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    girarY -= 5;
                    break;
                case KeyEvent.VK_RIGHT:
                    girarY += 5;
                    break;
                case KeyEvent.VK_DOWN:
                    girarX += 5;
                    break;
                case KeyEvent.VK_UP:
                    girarX -= 5;
                    break;
                case KeyEvent.VK_PAGE_UP:
                    girarZ += 5;
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    girarZ -= 5;
                    break;
                case VK_L:
                    luz = !luz;
                    break;
                case KeyEvent.VK_HOME:
                    girarX = girarY = girarZ = 0;
                    break;
                default:
                    break;
            }
		repaint();
	}

        @Override
	public void keyReleased(KeyEvent e) { 
	}

        @Override
	public void keyTyped(KeyEvent e) { 
	}
	
	private void textura(GL2 gl, float r, float g, float b) {
            gl.glColor3f(r,g,b);                //cor do quadrado
            gl.glTranslatef(0,0,0.5f);          //move a figura pra frente
            gl.glNormal3f(0,0,1);               //vetor normal do quadrado(pré-definido)

            gl.glBegin(GL2.GL_QUADS);           //----------------------------------------------------
                gl.glVertex2f(-0.5f,-0.5f);     //Desenha quadrados coloridos como textura para 
                gl.glVertex2f(0.5f,-0.5f);      //preencher toda a area do cubo(antes que a translação
                gl.glVertex2f(0.5f,0.5f);       //seja aplicada) no plano x,y
                gl.glVertex2f(-0.5f,0.5f);      //com seu valor em (0,0,0)
            gl.glEnd();                         //---------------------------------------------------
	}

	private void desenhaCubo(GL2 gl) {
            gl.glPushMatrix();          //salva as transformações atuais na pilha
            gl.glScaled(1,1,1);         //promove uma escala de 1-1-1 para o cubo
            GLUT.glutWireCube(1);       //instancia um cubo a partir do GLUT
            gl.glPopMatrix();           //restaura as transformações anteriores
            
            gl.glPushMatrix();          //salva as transformações atuais na pilha
            textura(gl,1,0,0);          //face frontal vermelha
            gl.glPopMatrix();           //restaura as transformações anteriores

            gl.glPushMatrix();          //salva as transformações atuais na pilha
            gl.glRotatef(180,0,1,0);    //rotaciona o quadrado para a parte de tras
            textura(gl,0,1,1);          //face trazeira cyan
            gl.glPopMatrix();           //restaura as transformações anteriores

            gl.glPushMatrix();          //salva as transformações atuais na pilha
            gl.glRotatef(-90,0,1,0);    //rotaciona o quadrado para a parte da esquerda
            textura(gl,0,1,0);          //face esquerda verde
            gl.glPopMatrix();           //restaura as transformações anteriores

            gl.glPushMatrix();          //salva as transformações atuais na pilha
            gl.glRotatef(90,0,1,0);     //rotaciona o quadrado para a parte da direita
            textura(gl,1,0,1);          //face direita magenta
            gl.glPopMatrix();           //restaura as transformações anteriores

            gl.glPushMatrix();          //salva as transformações atuais na pilha
            gl.glRotatef(-90,1,0,0);    //rotaciona o quadrado para a parte de cima
            textura(gl,0,0,1);          //face de cima azul
            gl.glPopMatrix();           //restaura as transformações anteriores

            gl.glPushMatrix();          //salva as transformações atuais na pilha
            gl.glRotatef(90,1,0,0);     //rotaciona o quadrado para a parte de baixo
            textura(gl,1,1,0);          //face de baixo amarela
            gl.glPopMatrix();           //restaura as transformações anteriores
	}
	
        @Override
	public void display(GLAutoDrawable drawable) {
            // chamado quando o painel precisa ser desenhado
            GL2 gl = drawable.getGL().getGL2();
            gl.glClearColor(0,0,0,0);                                       //Especifica que a cor de fundo da janela será preta
            gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );  //Limpa a janela de visualização com a cor de fundo especificada 

            gl.glMatrixMode(GL2.GL_PROJECTION); //configura a projeção
            gl.glLoadIdentity();                //reinicializa as transformações
            gl.glOrtho(-1,1,-1,1,-2,2);         //projeção ortográfica das faces
            gl.glMatrixMode(GL2.GL_MODELVIEW);  //configura a modelview

            gl.glLoadIdentity();            //reinicializa as transformações
            gl.glRotatef(girarZ,0,0,1);     //rotação em graus (15) no eixo ligado (1)
            gl.glRotatef(girarY,0,1,0);     //rotação em graus (15) no eixo ligado (1)
            gl.glRotatef(girarX,1,0,0);     //rotação em graus (15) no eixo ligado (1)
            
            if (luz) {
                gl.glEnable(GL_LIGHTING);
            } else {
                gl.glDisable(GL_LIGHTING);
            }

            desenhaCubo(gl);
	}

        @Override
	public void init(GLAutoDrawable drawable) {
            // chamado quando o painel é criado
            GL2 gl = drawable.getGL().getGL2();
            gl.glClearColor(0f,0f,0f,0f);           // Especifica que a cor de fundo da janela será preta
            gl.glEnable(GL.GL_DEPTH_TEST);          //controla as comparações de profundidade e atualiza o depth buffer
            gl.glEnable(GL2.GL_COLOR_MATERIAL);     // Habilita a definição da cor do material a partir da cor corrente
            
            // seta LIGHTING para LIGHT-1
            // A luz ambiente não vem de uma direção específica. Precisa de alguma luz ambiente
            // para iluminar a cena. Valor do ambiente no RGBA.
            float[] LuzAmbiente = {0.5f, 0.5f, 0.5f, 1.0f};
            // A luz difusa vem de um determinado local. O valor do difuso no RGBA
            float[] LuzDifusa = {1.0f, 1.0f, 1.0f, 1.0f};
            // localização da luz difusa xyz (na frente da tela).
            float PosicaoLuzDifusa[] = {5.0f, 0.0f, 2.0f, 1.0f};

            gl.glLightfv(GL_LIGHT1, GL_AMBIENT, LuzAmbiente, 0);
            gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, LuzDifusa, 0);
            gl.glLightfv(GL_LIGHT1, GL_POSITION, PosicaoLuzDifusa, 0);
            gl.glEnable(GL_LIGHT1);         // Ativa Light-1
            gl.glDisable(GL_LIGHTING);      // Mas desativa LIGHTING
            luz = false;
	}

        @Override
	public void dispose(GLAutoDrawable drawable) {
            // chamado quando a janela é fechada
	}

        @Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            // chamado quando a janela é redimensionada
	}
}