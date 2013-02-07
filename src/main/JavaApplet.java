package main;

import javax.swing.*;
import java.awt.*;
/**
 * This class derives from JApplet and is used to create the requested Applet. <p>
 * @author Juan R. Da Costa
 */
public class JavaApplet extends JApplet{
    /**
     * Overriding the init() method.
     */
    public void init(){
        this.setSize(605, 592);
        MainPanel myMainPanel=new MainPanel();
        Container contentPane=this.getContentPane();
        //contentPane.setSize(570, 592);
        contentPane.add(myMainPanel);
    }
}
