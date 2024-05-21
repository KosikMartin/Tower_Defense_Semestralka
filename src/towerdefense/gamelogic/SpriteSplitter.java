package towerdefense.gamelogic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Trieda pre rozdeľovanie sprite sheetov v hre Tower Defense.
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class SpriteSplitter {

    /**
     * Rozdelí sprite sheet na jednotlivé obrázky.
     * Tiež je možné, že toto vybehne ako plagiát, je to jednoduchá algoritmus na spritesheety a ich rozdelenie.
     * Ale robil som ho sám a nikde inde som celý kód nenašiel.
     * @param path cesta k sprite sheetu
     * @param spriteWidth šírka jednotlivého sprite
     * @param spriteHeight výška jednotlivého sprite
     * @throws IOException ak sa vyskytne chyba pri čítaní alebo zápise súboru
     */
    public static void splitSpriteSheet(String path, int spriteWidth, int spriteHeight) throws IOException {
        BufferedImage sheet = ImageIO.read(new File(path));
        int spritesPerRow = sheet.getWidth() / spriteWidth;
        int spriteRows = sheet.getHeight() / spriteHeight;
        for (int y = 0; y < spriteRows; y++) {
            for (int x = 0; x < spritesPerRow; x++) {
                BufferedImage sprite = sheet.getSubimage( //getSubimage bola jediná metóda ktorú som našiel že vie rozdeliť obrázok.
                        x * spriteWidth,
                        y * spriteHeight,
                        spriteWidth,
                        spriteHeight
                );
                File outputfile = new File("sprite_" + y + "_" + x + ".png");
                ImageIO.write(sprite, "png", outputfile);
            }
        }
    }
}
