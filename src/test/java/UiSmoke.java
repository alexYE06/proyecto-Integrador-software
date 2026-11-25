import vista.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
public class UiSmoke {
 public static void main(String[] args)throws Exception{
  SwingUtilities.invokeAndWait(()->{
   try{
    MainCentralFrame frame=new MainCentralFrame();frame.setExtendedState(JFrame.NORMAL);frame.setSize(1366,768);frame.setVisible(true);
    for(String name:new String[]{"Reportes","Gestion"}){frame.cambiarPantalla(name);frame.validate();BufferedImage im=new BufferedImage(frame.getWidth(),frame.getHeight(),BufferedImage.TYPE_INT_RGB);Graphics2D g=im.createGraphics();frame.printAll(g);g.dispose();ImageIO.write(im,"png",new File("target/evidencias/UI-"+name+".png"));}
    for(int i=0;i<frame.getJMenuBar().getMenuCount();i++){JMenu menu=frame.getJMenuBar().getMenu(i);for(int j=0;j<menu.getItemCount();j++){JMenuItem item=menu.getItem(j);if(item!=null){item.doClick();System.out.println("PASS menu: "+item.getText());}}}
    for(Window w:Window.getWindows())w.dispose();
   }catch(Exception e){throw new RuntimeException(e);}
  });
  System.exit(0);
 }
}
