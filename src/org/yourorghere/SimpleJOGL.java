package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
import java.awt.event.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


/**
 * SimpleJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL implements GLEventListener {
    
    static Koparka koparka;
    static Scena scena;

    static BufferedImage image1 = null,image2 = null, image3=null;
    static Texture t1 = null, t2 = null, t3 = null;
    
    public static float koparka_ram1 = 45.0f;
    public static float koparka_ram2 = -45.0f;
    public static float koparka_lyzka = -45.0f;
    public static float obrot = -45.0f;
    //warto?ci sk�adowe o�wietlenia i koordynaty �r�d�a �wiat�a
    static float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };//swiat�o otaczaj�ce
    static float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };//�wiat�o rozproszone
    static float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f}; //�wiat�o odbite
    static float lightPos[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja �wiat�a
    static float lightPos1[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja �wiat�a
        
    //statyczne pola okre�laj�ce rotacj� wok� osi X i Y
    private static float xrot = 0.0f, yrot = 0.0f, kx, kkat;

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new SimpleJOGL());
        frame.add(canvas);
        frame.setSize(800, 600);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        
         //Obs�uga klawiszy strza�ek
         frame.addKeyListener(new KeyListener() {
             public void keyPressed(KeyEvent e) {
                 if(e.getKeyCode() == KeyEvent.VK_8)
                         scena.przesun(2.0f);
                 if(e.getKeyCode() == KeyEvent.VK_2)
                         scena.przesun(-2.0f);
                 if(e.getKeyCode() == KeyEvent.VK_4)
                         scena.kat -= 2.0f;
                 if(e.getKeyCode() == KeyEvent.VK_6)
                         scena.kat += 2.0f;
                 if(e.getKeyCode() == KeyEvent.VK_A)
                         lightPos[3] = 10.0f;
                 if(e.getKeyCode() == KeyEvent.VK_S)
                         lightPos[3] = -10.0f;
                 if(e.getKeyCode() == KeyEvent.VK_Z)
                         lightPos[0] += 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_X)
                         lightPos[0] -= 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_M)
                         lightPos1[0] += 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_N)
                         lightPos1[0] -= 1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_C)
//                         lightPos[1] += 1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_V)
//                         lightPos[1] -= 1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_D)
//                     for (int i=0; i<=2; i++)
//                         specular[i] += 0.1f;
                 if(e.getKeyCode() == KeyEvent.VK_F)
                     for (int i=0; i<=2; i++)
                         specular[i] -= 0.1f;
                 if(e.getKeyCode() == KeyEvent.VK_Q)
                     for (int i=0; i<=2; i++)
                         ambientLight[i] += 0.1f;
                 if(e.getKeyCode() == KeyEvent.VK_W)
                     for (int i=0; i<=2; i++)
                         ambientLight[i] -= 0.1f;
                 if(e.getKeyCode() == KeyEvent.VK_E)
                     for (int i=0; i<=2; i++)
                         diffuseLight[i] += 0.1f;
                 if(e.getKeyCode() == KeyEvent.VK_R)
                     for (int i=0; i<=2; i++)
                         diffuseLight[i] -= 0.1f;
//                 if(e.getKeyCode() == KeyEvent.VK_UP)
//                     xrot -= 1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_DOWN)
//                     xrot +=1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_RIGHT)
//                     yrot += 1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_LEFT)
//                     yrot -=1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_UP)
                     kx -= 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_DOWN)
                     kx +=1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                     kkat += 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_LEFT)
                     kkat -=1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_V)
                     if (koparka_ram1 < 60.0f)
                         koparka_ram1+=1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_B)
                     if (koparka_ram1 > -10.0f)
                         koparka_ram1-=1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_N)
                     if (koparka_ram2 < 0.0f)
                         koparka_ram2+=1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_M)
                     if (koparka_ram2 > -90.0f)
                         koparka_ram2-=1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_5)
//                     if (koparka_lyzka < 0.0f)
//                         koparka_lyzka+=1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_6)
//                     if (koparka_lyzka > -90.0f)
//                         koparka_lyzka-=1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_7)
//                     obrot+=1.0f;
//                 if(e.getKeyCode() == KeyEvent.VK_8)
//                     obrot+=1.0f;
             }
         
             public void keyReleased(KeyEvent e){}
             public void keyTyped(KeyEvent e){}
         
         });

        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        //(czwarty parametr okre�la odleg�o�� �r�d�a:
        //0.0f-niesko�czona; 1.0f-okre�lona przez pozosta�e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o�wietlenia
        //ustawienie parametr�w ?r�d�a ?wiat�a nr. 0
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat�o otaczaj�ce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //�wiat�o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //�wiat�o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat�a
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie �r�d�a �wiat�a nr. 0
            lightPos1[3] = -10.0f;
        gl.glLightfv(GL.GL_LIGHT1,GL.GL_AMBIENT,ambientLight,0); //swiat�o otaczaj�ce
        gl.glLightfv(GL.GL_LIGHT1,GL.GL_DIFFUSE,diffuseLight,0); //�wiat�o rozproszone
        gl.glLightfv(GL.GL_LIGHT1,GL.GL_SPECULAR,specular,0); //�wiat�o odbite

        gl.glLightfv(GL.GL_LIGHT1,GL.GL_POSITION,lightPos1,0); //pozycja ?wiat�a
        gl.glEnable(GL.GL_LIGHT1); //uaktywnienie �r�d�a �wiat�a nr. 0
        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie �ledzenia kolor�w
        //kolory b�d� ustalane za pomoc� glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno�ci i odblaskowo?ci obiekt�w
        float specref[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR,specref,0);
        
        gl.glMateriali(GL.GL_FRONT,GL.GL_SHININESS,128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        
        // Setup the drawing area and shading mode
        // gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
   
         //wy��czenie wewn�trzych stron prymityw�w
         // gl.glEnable(GL.GL_CULL_FACE);
        
        try {
            image1 = ImageIO.read(getClass().getResourceAsStream("/bok.jpg"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/trawa.jpg"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/niebo.jpg"));
        }
            catch(Exception exc) {
            JOptionPane.showMessageDialog(null, exc.toString());
        return;
        }

         t1 = TextureIO.newTexture(image1, false);
         t2 = TextureIO.newTexture(image2, false);
         t3 = TextureIO.newTexture(image3, false);

         gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_BLEND | GL.GL_MODULATE);
         gl.glEnable(GL.GL_TEXTURE_2D);
         gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
         gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
         gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
         gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        
        koparka = new Koparka();
         scena = new Scena();
        
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!
        
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        
        glu.gluPerspective(90.0f, h, 0.1f, 300.0f);
        
//        float ilor;
//        if (width<=height) {
//            ilor = height/width;
//            gl.glOrtho(-20.0f,20.0f,-20.0f*ilor,20.0f*ilor,-20.0f,20.0f);
//        }
//        else {
//            ilor = width/height;
//            gl.glOrtho(-20.0f*ilor,20.0f*ilor,-20.0f,20.0f,-20.0f,20.0f);
//        }
//        
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        
        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat�o otaczaj�ce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //�wiat�o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //�wiat�o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat�a
        gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni�cie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wok� osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wok� osi Y
        gl.glRotatef(scena.kat, 0.0f, 1.0f, 0.0f);
        // gl.glScalef(3.0f, 3.0f, 3.0f);
        gl.glTranslatef(0.0f,99.0f,0.0f);

        
//        gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
//        gl.glColor3f(1.0f,0.0f,0.0f);
//        gl.glBegin(GL.GL_TRIANGLES);
//            //siana 1
//            float[] scianka1 = { 1.0f,-1.0f,1.0f,
//                               -1.0f,-1.0f,1.0f,
//                                0.0f,0.0f,0.0f };
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(-1.0f, 0.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.0f,0.0f,0.0f);
//            float[] normalna1 = WyznaczNormalna(scianka1,0,3,6);
//            gl.glNormal3fv(normalna1,0);
//            //siana 3
//            float[] scianka3 = { -1.0f,-1.0f,-1.0f,
//                                 1.0f,-1.0f,-1.0f,
//                                 0.0f,0.0f,0.0f };
//            gl.glTexCoord2f(0.0f, 1.0f);gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(-1.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.0f,0.0f,0.0f);
//            float[] normalna3 = WyznaczNormalna(scianka3,0,3,6);
//            gl.glNormal3fv(normalna3,0);
//        gl.glEnd();
//        gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
//        gl.glBegin(GL.GL_TRIANGLES);
//            //sciana 2
//            float[] scianka2 = { 1.0f,-1.0f,-1.0f,
//                                1.0f,-1.0f,1.0f,
//                                0.0f,0.0f,0.0f };
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(-1.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.0f,0.0f,0.0f);
//            float[] normalna2 = WyznaczNormalna(scianka2,0,3,6);
//            gl.glNormal3fv(normalna2,0);
// 
//            //siana 4
//            float[] scianka4 = { -1.0f,-1.0f,1.0f,
//                                -1.0f,-1.0f,1.0f,
//                                -1.0f,-1.0f,-1.0f };
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(-1.0f, 0.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.0f,0.0f,0.0f);
//            float[] normalna4 = WyznaczNormalna(scianka4,0,3,6);
//            gl.glNormal3fv(normalna4,0);
//        gl.glEnd();
//            
//        gl.glBegin(GL.GL_QUADS);
//            //siana dolna
//         float[] scianka5 = { -1.0f,-1.0f,1.0f,
//                               -1.0f,-1.0f,-1.0f,
//                                1.0f,-1.0f,-1.0f,
//                                1.0f,-1.0f,1.0f };
//            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//            float[] normalna5 = WyznaczNormalna(scianka5,0,3,6);
//            gl.glNormal3fv(normalna5,0);
//        gl.glEnd();

//       gl.glTranslatef(-22.0f, -12.0f, 0.0f);
//        
//        gl.glPushMatrix();
//        for (int i=0; i<8; i++) {
//            
//            gl.glPushMatrix();
//            for (int j=0; j<8; j++) {
//                gl.glTranslatef(4.5f, 0.0f, 0.0f);
//                drzewo(gl);
//            }
//            gl.glPopMatrix();
//            
//            gl.glTranslatef(0.0f, 4.0f, 0.0f);
//        }
//        gl.glPopMatrix();
//        
//        gl.glScalef(4.0f, 4.0f, 4.0f);
//        koparka.Rysuj(gl);
        
//        gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
//        gl.glBegin(GL.GL_QUADS);
//            //?ciana przednia
//            gl.glColor3f(1.0f,0.0f,0.0f);
//            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
//
//            gl.glColor3f(0.0f,1.0f,0.0f);
//            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
//            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//
//            gl.glColor3f(0.0f,0.0f,1.0f);
//            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
//        gl.glEnd();
//            //?ciana prawa
//        gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
//        gl.glBegin(GL.GL_QUADS);
//            gl.glColor3f(1.0f,1.0f,0.0f);
//            gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
//            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
//            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//
//            gl.glColor3f(1.0f,0.0f,1.0f);
//            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//
//            gl.glColor3f(0.0f,1.0f,1.0f);
//            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
//            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
//            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
//            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
//        gl.glEnd();
        
        
        scena.Rysuj(gl, t1, t2, t3);
        gl.glPushMatrix();
        gl.glTranslatef(kx,-98.5f,0.0f);
        gl.glRotatef(kkat, 0.0f, 1.0f, 0.0f);
        koparka.Rysuj(gl);
        gl.glPopMatrix();
        
        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }
    
    private float[] WyznaczNormalna(float[] punkty, int ind1, int ind2, int ind3) {
        float[] norm = new float[3];
        float[] wektor0 = new float[3];
        float[] wektor1 = new float[3];

        for(int i=0;i<3;i++) {
            wektor0[i]=punkty[i+ind1]-punkty[i+ind2];
            wektor1[i]=punkty[i+ind2]-punkty[i+ind3];
        }

        norm[0]=wektor0[1]*wektor1[2]-wektor0[2]*wektor1[1];
        norm[1]=wektor0[2]*wektor1[0]-wektor0[0]*wektor1[2];
        norm[2]=wektor0[0]*wektor1[1]-wektor0[1]*wektor1[0];
        
        float d = (float)Math.sqrt((norm[0]*norm[0])+(norm[1]*norm[1])+ (norm[2]*norm[2]) );
        
        if (d==0.0f)
            d=1.0f;
        
        norm[0]/=d;
        norm[1]/=d;
        norm[2]/=d;

        return norm;
    }
    
    void drzewo(GL gl) {
        gl.glPushMatrix();
        
        gl.glColor3f(0.4f,0.9f,0.5f);
        stozek(gl);
        
        gl.glScalef(1.4f, 1.4f, 0.9f);
        gl.glTranslatef(0.0f, 0.0f, 1.2f);
        stozek(gl);
        
        gl.glScalef(1.6f, 1.8f, 1.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.2f);
        stozek(gl);
        
        gl.glColor3f(0.55f,0.27f,0.08f);        
        gl.glTranslatef(0.0f, 0.0f, 2.0f);
        gl.glScalef(0.5f, 0.5f, 2.0f);
        walec(gl);
        
        gl.glPopMatrix();
    }
    
    void walec(GL gl) {
        //wywo�ujemy automatyczne normalizowanie normalnych
        //bo operacja skalowania je zniekszta�ci
        gl.glEnable(GL.GL_NORMALIZE);
        float x,y,kat;
        
        gl.glBegin(GL.GL_QUAD_STRIP);
        for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f)) {
            x = 0.5f*(float)Math.sin(kat);
            y = 0.5f*(float)Math.cos(kat);
            gl.glNormal3f((float)Math.sin(kat),(float)Math.cos(kat),0.0f);
            gl.glVertex3f(x, y, -1.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        
        gl.glNormal3f(0.0f,0.0f,-1.0f);
        x=y=kat=0.0f;
        
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -1.0f); //srodek kola
        for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f)) {
            x = 0.5f*(float)Math.sin(kat);
            y = 0.5f*(float)Math.cos(kat);
            gl.glVertex3f(x, y, -1.0f);
        }
        gl.glEnd();
        
        gl.glNormal3f(0.0f,0.0f,1.0f);
        x=y=kat=0.0f;
        
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for(kat = 2.0f*(float)Math.PI; kat > 0.0f ; kat -= (Math.PI/32.0f)) {
            x = 0.5f*(float)Math.sin(kat);
            y = 0.5f*(float)Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    void stozek(GL gl) {
        //wywo�ujemy automatyczne normalizowanie normalnych
        gl.glEnable(GL.GL_NORMALIZE);
        float x,y,kat;
        
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -2.0f); //wierzcholek stozka
        for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f)) {
            x = (float)Math.sin(kat);
            y = (float)Math.cos(kat);
            gl.glNormal3f((float)Math.sin(kat),(float)Math.cos(kat),-2.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0.0f,0.0f,1.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for(kat = 2.0f*(float)Math.PI; kat > 0.0f; kat -= (Math.PI/32.0f)) {
            x = (float)Math.sin(kat);
            y = (float)Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    
    public void circle(GL gl, float Z, float m) {
        float X, Y;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, Z);
        
        for(float kat = 0.0f; kat < (2.0f*Math.PI); kat+=(Math.PI/32.0f)) {
            X = m*(float)Math.sin(kat);
            Y = m*(float)Math.cos(kat);
            
            gl.glVertex3f(X, Y, Z);
        }
        
        gl.glEnd();
    }
    
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
}

