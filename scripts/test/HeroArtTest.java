import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//every hero class needs a sprite sheet that holds the hero select icon (12x15 at 0,90) and a splash picture,
//a missing one shows a blank button and the hero select screen can hang on it
public class HeroArtTest {

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		int failures = 0;
		for (HeroClass cl : HeroClass.values()) {
			String sheet = cl.spritesheet();
			String splash = cl.splashArt();
			String problem = null;
			File f = new File( sheet );
			if (!f.exists()) problem = "sheet missing " + sheet;
			else {
				BufferedImage img = ImageIO.read( f );
				if (img == null) problem = "sheet unreadable " + sheet;
				else {
					//the same cell HeroClass.selectIcon draws
					int ix = 0, iy = 90, iw = 12, ih = 15;
					if (cl.name().startsWith( "RBA_" )) { iy = 0; ih = 17; }
					if (cl == HeroClass.RAT_KING) { iy = 0; iw = 16; ih = 17; }
					boolean any = false;
					if (img.getHeight() < iy + ih || img.getWidth() < ix + iw) problem = "sheet too small for the icon " + sheet + " " + img.getWidth() + "x" + img.getHeight();
					else for (int x = ix; x < ix + iw && !any; x++) for (int y = iy; y < iy + ih && !any; y++) if ((img.getRGB( x, y ) >>> 24) != 0) any = true;
					if (problem == null && !any) problem = "icon cell empty in " + sheet;
				}
			}
			if (splash == null || !new File( splash ).exists()) problem = (problem == null ? "" : problem + ", ") + "splash missing " + splash;
			if (problem != null) {
				failures++;
				System.out.println( "FAIL: " + cl.name() + " " + problem );
			}
		}
		System.out.println( "SUMMARY heroart: " + HeroClass.values().length + " classes, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
