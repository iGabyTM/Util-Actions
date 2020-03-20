package me.gabytm.util.actions.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author ConnorLinfoot
 */
public class ActionBarUtil {
    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
    private static boolean useOldMethods = false;

    static {
        if (VERSION.equalsIgnoreCase("v1_8_R1") || VERSION.startsWith("v1_7_")) {
            useOldMethods = true;
        }
    }

    public static void sendActionBar(final Player player, final String message) {
        final String BUKKIT = "org.bukkit.craftbukkit." + VERSION;
        final String NMS = "net.minecraft.server." + VERSION;

        try {
            Class<?> craftPlayerClass = Class.forName(BUKKIT + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object packet;
            Class<?> packetPlayOutChatClass = Class.forName(NMS + ".PacketPlayOutChat");
            Class<?> packetClass = Class.forName(NMS + ".Packet");

            if (useOldMethods) {
                Class<?> chatSerializerClass = Class.forName(NMS + ".ChatSerializer");
                Class<?> iChatBaseComponentClass = Class.forName(NMS + ".IChatBaseComponent");
                Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
                Object cbc = iChatBaseComponentClass.cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + message + "\"}"));
                packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> chatComponentTextClass = Class.forName(NMS + ".ChatComponentText");
                Class<?> iChatBaseComponentClass = Class.forName(NMS + ".IChatBaseComponent");

                try {
                    Class<?> chatMessageTypeClass = Class.forName(NMS + ".ChatMessageType");
                    Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                    Object chatMessageType = Arrays.stream(chatMessageTypes).filter(obj -> obj.toString().equals("GAME_INFO")).findFirst().orElse(null);
                    Object chatComponentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                    packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, chatMessageTypeClass}).newInstance(chatComponentText, chatMessageType);
                } catch (ClassNotFoundException ignored) {
                    Object chatComponentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                    packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(chatComponentText, (byte) 2);
                }
            }

            Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
            Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
            Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
            Object playerConnection = playerConnectionField.get(craftPlayerHandle);
            Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
