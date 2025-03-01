package  me.nutilsv3.utils;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CS {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder().build();
    public static String translate(String string) {
        // Sostituzione colori HEX
        Matcher matcher = HEX_PATTERN.matcher(string);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            String replacement = new StringBuilder("§x")
                    .append("§").append(hex.charAt(0)).append("§").append(hex.charAt(1))
                    .append("§").append(hex.charAt(2)).append("§").append(hex.charAt(3))
                    .append("§").append(hex.charAt(4)).append("§").append(hex.charAt(5))
                    .toString();
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);

        // Sostituzione colori classici e newline
        return buffer.toString().replace("&", "§").replace("%n", "\n");
    }

    public static @NotNull ComponentLike gradientText(String msg, TextColor stcolor, TextColor endcolor){
        return  MINI_MESSAGE.deserialize("<gradient:" + stcolor.asHexString() + ":" + endcolor.asHexString() + ">" + msg + "</gradient>");
    }
}
