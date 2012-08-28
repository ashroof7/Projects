/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package findthebomb;

import com.sun.javafx.tools.fxd.LoadContext;
import java.applet.AudioClip;
import java.awt.FileDialog;
import java.awt.Frame;

/**
 *
 * @author Saleh
 */

class A{ public synchronized void st(){}}
class B extends A{

    @Override
    public synchronized void st() {
        super.st();
    }

 }
public class sound extends Thread {

    AudioClip clip;
    public sound() {
        super();
        clip = getClipByUrl("C:\\clip.mp3");
    }


@Override
    public synchronized void start() {
        super.start();
        clip.play();
    }



    private AudioClip getClipByUrl(String url) {
        return null;
    }


}
