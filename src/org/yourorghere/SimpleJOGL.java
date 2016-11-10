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



/**
 * SimpleJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL implements GLEventListener {
    
    static Koparka koparka;
    //warto?ci sk³adowe oœwietlenia i koordynaty Ÿród³a œwiat³a
    static float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };//swiat³o otaczajšce
    static float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };//œwiat³o rozproszone
    static float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f}; //œwiat³o odbite
    static float lightPos[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja œwiat³a
    static float lightPos1[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja œwiat³a
        
    //statyczne pola okreœlaj¹ce rotacjê wokó³ osi X i Y
    private static float xrot = 0.0f, yrot = 0.0f;

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new SimpleJOGL());
        frame.add(canvas);
        frame.setSize(640, 480);
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
        
         //Obs³uga klawiszy strza³ek
         frame.addKeyListener(new KeyListener() {
             public void keyPressed(KeyEvent e) {
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
                 if(e.getKeyCode() == KeyEvent.VK_C)
                         lightPos[1] += 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_V)
                         lightPos[1] -= 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_D)
                     for (int i=0; i<=2; i++)
                         specular[i] += 0.1f;
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
                 if(e.getKeyCode() == KeyEvent.VK_UP)
                     xrot -= 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_DOWN)
                     xrot +=1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                     yrot += 1.0f;
                 if(e.getKeyCode() == KeyEvent.VK_LEFT)
                     yrot -=1.0f;
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

        //(czwarty parametr okreœla odleg³oœæ œród³a:
        //0.0f-nieskoñczona; 1.0f-okreœlona przez pozosta³e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie oœwietlenia
        //ustawienie parametrów ?ród³a ?wiat³a nr. 0
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat³o otaczajšce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //œwiat³o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //œwiat³o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat³a
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie Ÿród³a œwiat³a nr. 0
            lightPos1[3] = -10.0f;
        gl.glLightfv(GL.GL_LIGHT1,GL.GL_AMBIENT,ambientLight,0); //swiat³o otaczajšce
        gl.glLightfv(GL.GL_LIGHT1,GL.GL_DIFFUSE,diffuseLight,0); //œwiat³o rozproszone
        gl.glLightfv(GL.GL_LIGHT1,GL.GL_SPECULAR,specular,0); //œwiat³o odbite

        gl.glLightfv(GL.GL_LIGHT1,GL.GL_POSITION,lightPos1,0); //pozycja ?wiat³a
        gl.glEnable(GL.GL_LIGHT1); //uaktywnienie Ÿród³a œwiat³a nr. 0
        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie œledzenia kolorów
        //kolory bêd¹ ustalane za pomoc¹ glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasnoœci i odblaskowo?ci obiektów
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
   
         //wy³¹czenie wewnêtrzych stron prymitywów
         // gl.glEnable(GL.GL_CULL_FACE);
        
        koparka = new Koparka();
        
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
        
        glu.gluPerspective(100.0f, h, 0.1f, 100.0f);
//        glu.gluPerspective(90.0f, h, 1.0, 20.0);
        
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
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat³o otaczajšce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //œwiat³o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //œwiat³o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat³a
        gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuniêcie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wokó³ osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wokó³ osi Y

        
//        gl.glColor3f(1.0f,0.0f,0.0f);
//        gl.glBegin(GL.GL_TRIANGLES);
//            //siana 1
//            float[] scianka1 = { 1.0f,-1.0f,1.0f,
//                               -1.0f,-1.0f,1.0f,
//                                0.0f,0.0f,0.0f };
//            gl.glVertex3f(1.0f,-1.0f,1.0f);
//            gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glVertex3f(0.0f,0.0f,0.0f);
//            float[] normalna1 = WyznaczNormalna(scianka1,0,3,6);
//            gl.glNormal3fv(normalna1,0);
//            //sciana 2
//            float[] scianka2 = { 1.0f,-1.0f,-1.0f,
//                                1.0f,-1.0f,1.0f,
//                                0.0f,0.0f,0.0f };
//            gl.glVertex3f(1.0f,-1.0f,-1.0f);
//            gl.glVertex3f(1.0f,-1.0f,1.0f);
//            gl.glVertex3f(0.0f,0.0f,0.0f);
//            float[] normalna2 = WyznaczNormalna(scianka2,0,3,6);
//            gl.glNormal3fv(normalna2,0);
//            //siana 3
//            float[] scianka3 = { -1.0f,-1.0f,-1.0f,
//                                 1.0f,-1.0f,-1.0f,
//                                 0.0f,0.0f,0.0f };
//            gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//            gl.glVertex3f(1.0f,-1.0f,-1.0f);
//            gl.glVertex3f(0.0f,0.0f,0.0f);
//            float[] normalna3 = WyznaczNormalna(scianka3,0,3,6);
//            gl.glNormal3fv(normalna3,0);
//            //siana 4
//            float[] scianka4 = { -1.0f,-1.0f,1.0f,
//                                -1.0f,-1.0f,1.0f,
//                                -1.0f,-1.0f,-1.0f };
//            gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//            gl.glVertex3f(0.0f,0.0f,0.0f);
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
//            gl.glVertex3f(-1.0f,-1.0f,1.0f);
//            gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//            gl.glVertex3f(1.0f,-1.0f,-1.0f);
//            gl.glVertex3f(1.0f,-1.0f,1.0f);
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
        koparka.Rysuj(gl);

        
        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }
    
    public class Koparka {

 public void Rysuj(GL gl) {
    //ciagnik
    gl.glColor3f(1.0f,1.0f,0.0f);
    Prostopadloscian(gl,-2.0f,-1.0f,-1.0f,4.0f,1.0f,2.0f);
    gl.glColor3f(0.15f,0.15f,0.15f);
    Walec(gl,0.5f,0.5f,-1.5f,-1.0f,-1.25f);
    Walec(gl,0.5f,0.5f,-1.5f,-1.0f,0.75f);
    Walec(gl,0.5f,0.5f,1.5f,-1.0f,-1.25f);
    Walec(gl,0.5f,0.5f,1.5f,-1.0f,0.75f);
    gl.glColor3f(1.0f,1.0f,0.0f);
    Prostopadloscian(gl,-0.5f,0.0f,-1.0f,2.0f,0.5f,2.0f);
    Prostopadloscian(gl,-0.5f,0.5f,-1.0f,0.1f,1.0f,0.1f);
    Prostopadloscian(gl,-0.5f,0.5f,0.9f,0.1f,1.0f,0.1f);
    Prostopadloscian(gl,1.4f,0.5f,-1.0f,0.1f,1.0f,0.1f);
    Prostopadloscian(gl,1.4f,0.5f,0.9f,0.1f,1.0f,0.1f);
    Prostopadloscian(gl,-0.5f,1.5f,-1.0f,2.0f,0.1f,2.0f);
    //ramie 1
    gl.glTranslatef(1.5f,0.0f,0.0f);
    gl.glRotatef(45.0f,0.0f,0.0f,1.0f);
    Prostopadloscian(gl,0.0f,0.0f,0.0f,3.0f,0.3f,0.3f);
    //ramie 2
    gl.glTranslatef(2.7f,0.0f,0.0f);
    gl.glRotatef(-45.0f,0.0f,0.0f,1.0f);
    Prostopadloscian(gl,0.0f,0.0f,0.0f,1.5f,0.3f,0.3f);
    //lyzka
    gl.glTranslatef(1.2f,0.1f,0.0f);
    gl.glRotatef(-45.0f,0.0f,0.0f,1.0f);
    Lyzka(gl);
} 
private void Prostopadloscian(GL gl, float x0, float y0, float z0, float dx, float dy, float dz) {
    float x1=x0+dx;
    float y1=y0+dy;
    float z1=z0+dz;
    gl.glBegin(GL.GL_QUADS);
    //sciana przednia
    gl.glNormal3f(0.0f,0.0f,1.0f);
    gl.glVertex3f(x0,y0,z1);
    gl.glVertex3f(x1,y0,z1);
    gl.glVertex3f(x1,y1,z1);
    gl.glVertex3f(x0,y1,z1);
    //sciana tylnia
    gl.glNormal3f(0.0f,0.0f,-1.0f);
    gl.glVertex3f(x0,y1,z0);
    gl.glVertex3f(x1,y1,z0);
    gl.glVertex3f(x1,y0,z0);
    gl.glVertex3f(x0,y0,z0);
    //sciana lewa
    gl.glNormal3f(-1.0f,0.0f,0.0f);
    gl.glVertex3f(x0,y0,z0);
    gl.glVertex3f(x0,y0,z1);
    gl.glVertex3f(x0,y1,z1);
    gl.glVertex3f(x0,y1,z0);
    //sciana prawa
    gl.glNormal3f(1.0f,0.0f,0.0f);
    gl.glVertex3f(x1,y1,z0);
    gl.glVertex3f(x1,y1,z1);
    gl.glVertex3f(x1,y0,z1);
    gl.glVertex3f(x1,y0,z0);
    //sciana dolna
    gl.glNormal3f(0.0f,-1.0f,0.0f);
    gl.glVertex3f(x0,y0,z1);
    gl.glVertex3f(x0,y0,z0);
    gl.glVertex3f(x1,y0,z0);
    gl.glVertex3f(x1,y0,z1);
    //sciana gorna
    gl.glNormal3f(0.0f,1.0f,0.0f);
    gl.glVertex3f(x1,y1,z1);
    gl.glVertex3f(x1,y1,z0);
    gl.glVertex3f(x0,y1,z0);
    gl.glVertex3f(x0,y1,z1);
    gl.glEnd();
 }

private void Walec(GL gl, float promien, float dlugosc,
    float px, float py, float pz) {
    float x=0.0f,y=0.0f,kat=0.0f;
    gl.glBegin(GL.GL_QUAD_STRIP);
    for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f)) {
        x = px + promien*(float)Math.sin(kat);
        y = py + promien*(float)Math.cos(kat);
        gl.glNormal3f((float)Math.sin(kat),(float)Math.cos(kat),0.0f);
        gl.glVertex3f(x, y, pz);
        gl.glVertex3f(x, y, pz+dlugosc);
    }
    gl.glEnd();
    gl.glNormal3f(0.0f,0.0f,-1.0f);
    x=y=kat=0.0f;
    gl.glBegin(GL.GL_TRIANGLE_FAN);
    gl.glVertex3f(px, py, pz); //srodek kola
    for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f)) {
        x = px + promien*(float)Math.sin(kat);
        y = py + promien*(float)Math.cos(kat);
        gl.glVertex3f(x, y, pz);
    }
    gl.glEnd();
    gl.glNormal3f(0.0f,0.0f,1.0f);
    x=y=kat=0.0f;
    gl.glBegin(GL.GL_TRIANGLE_FAN);
    gl.glVertex3f(px, py, pz+dlugosc); //srodek kola
    for(kat = 2.0f*(float)Math.PI; kat > 0.0f ; kat -= (Math.PI/32.0f)) {
        x = px + promien*(float)Math.sin(kat);
        y = py + promien*(float)Math.cos(kat);
        gl.glVertex3f(x, y, pz+dlugosc);
    }
    gl.glEnd();
}

 private void Lyzka(GL gl) {
    gl.glDisable(GL.GL_CULL_FACE);
    gl.glBegin(GL.GL_TRIANGLES);
    //prawa
    gl.glNormal3f(0.0f,0.0f,1.0f);
    gl.glVertex3f(0.0f,0.0f,0.5f);
    gl.glVertex3f(0.5f,0.5f,0.5f);
    gl.glVertex3f(0.0f,0.5f,0.5f);
    //lewa
    gl.glNormal3f(0.0f,0.0f,-1.0f);
    gl.glVertex3f(0.0f,0.0f,-0.2f);
    gl.glVertex3f(0.0f,0.5f,-0.2f);
    gl.glVertex3f(0.5f,0.5f,-0.2f);
    gl.glEnd();
    gl.glDisable(GL.GL_CULL_FACE);
    gl.glBegin(GL.GL_QUADS);
    gl.glNormal3f(-1.0f,0.0f,0.0f);
    gl.glVertex3f(0.0f,0.0f,0.5f);
    gl.glVertex3f(0.0f,0.5f,0.5f);
    gl.glVertex3f(0.0f,0.5f,-0.2f);
    gl.glVertex3f(0.0f,0.0f,-0.2f);
    gl.glNormal3f(0.0f,1.0f,0.0f);
    gl.glVertex3f(0.0f,0.5f,0.5f);
    gl.glVertex3f(0.5f,0.5f,0.5f);
    gl.glVertex3f(0.5f,0.5f,-0.2f);
    gl.glVertex3f(0.0f,0.5f,-0.2f);
    gl.glEnd();
    gl.glEnable(GL.GL_CULL_FACE);
 }
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
        //wywo³ujemy automatyczne normalizowanie normalnych
        //bo operacja skalowania je zniekszta³ci
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
        //wywo³ujemy automatyczne normalizowanie normalnych
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

