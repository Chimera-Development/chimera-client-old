package dev.chimera.Utils;

import dev.chimera.ChimeraClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;

import static dev.chimera.ChimeraClient.mc;

public class ChimeraLogger {
    public static int INFO_COLOR = 0x64b9fa;
    public static int WARN_COLOR = Formatting.YELLOW.getColorValue();
    public static int ERROR_COLOR = Formatting.RED.getColorValue();
    public static int OTHER_COLOR = Formatting.DARK_PURPLE.getColorValue();
    public static void info(String s) {
        info(Text.literal(s));
    }

    public static void info(Text t) {
        try {
            mc.inGameHud.getChatHud()
                    .addMessage(getChimeraText(OTHER_COLOR)
                            //.append("\u00a73\u00a7lINFO: \u00a73")
                            .append(((MutableText) t).styled(s -> s.withColor(INFO_COLOR))));
        } catch (Exception e) {
            ChimeraClient.LOGGER.info(t.getString());
        }
    }

    public static void warn(String s) {
        warn(Text.literal(s));
    }

    public static void warn(Text t) {
        try {
            mc.inGameHud.getChatHud()
                    .addMessage(getChimeraText(WARN_COLOR)
                            //.append("\u00a7e\u00a7lWARN: \u00a7e")
                            .append(((MutableText) t).styled(s -> s.withColor(WARN_COLOR))));
        } catch (Exception e) {
            ChimeraClient.LOGGER.warn(t.getString());
        }
    }

    public static void error(String s) {
        error(Text.literal(s));
    }

    public static void error(Text t) {
        try {
            mc.inGameHud.getChatHud()
                    .addMessage(getChimeraText(ERROR_COLOR)
                            //.append("\u00a7c\u00a7lERROR: \u00a7c")
                            .append(((MutableText) t).styled(s -> s.withColor(ERROR_COLOR))));
        } catch (Exception e) {
            ChimeraClient.LOGGER.error(t.getString());
        }
    }

    public static void noPrefix(String s) {
        noPrefix(Text.literal(s));
    }

    public static void noPrefix(Text text) {
        try {
            mc.inGameHud.getChatHud().addMessage(text);
        } catch (Exception e) {
            ChimeraClient.LOGGER.info(text.getString());
        }
    }

    private static MutableText getChimeraText(int color) {
        return Text.literal("[").styled(s -> s.withColor(color))
                .append("Chimera Client")
                .append(Text.literal("] ").styled(s -> s.withColor(color)));
    }
}
