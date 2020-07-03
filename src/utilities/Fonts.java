package utilities;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import panes.ArchivePane;

import java.io.IOException;

public class Fonts {

    static String res = "resources/";

    public static PdfFont getRegularFont() throws IOException {
        /*String REGULAR = ArchivePane.class.getResource("/") + "AbhayaLibre-Regular.ttf";
        REGULAR = REGULAR.substring(4);*/
        String REGULAR = res + "AbhayaLibre-Regular.ttf";
        FontProgram fontProgram = FontProgramFactory.createFont(REGULAR);
        PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, true);

        return font;
    }

    public static PdfFont getBoldFont() throws IOException {
        /*String REGULAR = ArchivePane.class.getResource("/") + "AbhayaLibre-Bold.ttf";
        REGULAR = REGULAR.substring(4);*/
        String REGULAR = res + "AbhayaLibre-Bold.ttf";
        FontProgram fontProgram = FontProgramFactory.createFont(REGULAR);
        PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, true);

        return font;
    }

    public static PdfFont getSemiBoldFont() throws IOException {
        /*String REGULAR = ArchivePane.class.getResource("/") + "AbhayaLibre-SemiBold.ttf";
        REGULAR = REGULAR.substring(4);*/
        String REGULAR = res + "AbhayaLibre-SemiBold.ttf";
        FontProgram fontProgram = FontProgramFactory.createFont(REGULAR);
        PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, true);

        return font;
    }

    public static PdfFont getExtraBoldFont() throws IOException {
        /*String REGULAR = ArchivePane.class.getResource("/") + "AbhayaLibre-ExtraBold.ttf";
        REGULAR = REGULAR.substring(4);*/
        String REGULAR = res + "AbhayaLibre-ExtraBold.ttf";
        FontProgram fontProgram = FontProgramFactory.createFont(REGULAR);
        PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, true);

        return font;
    }

}
