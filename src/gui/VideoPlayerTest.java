package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VideoPlayerTest {

    private final JFrame frame;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        /*NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "lib/vlc-win32");
        NativeLibrary.addSearchPath(RuntimeUtil.getPluginsDirectoryName(), "lib/vlc-win32/plugins");
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcCoreName(), "lib/vlc-win32");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);*/
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VideoPlayerTest(args);
            }
        });
    }

    public VideoPlayerTest(String[] args) {
        frame = new JFrame("My First Media Player");
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().playMedia("Pauliandergarten.mp4");
    }
}