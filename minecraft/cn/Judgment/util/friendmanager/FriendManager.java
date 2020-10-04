package cn.Judgment.util.friendmanager;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class FriendManager {
   private static ArrayList friends = new ArrayList();

   public static ArrayList getFriends() {
      return friends;
   }

   public static boolean isFriend(EntityPlayer player) {
      Iterator var2 = friends.iterator();

      while(var2.hasNext()) {
         Friend friend = (Friend)var2.next();
         if (friend.getName().equalsIgnoreCase(player.getName())) {
            return true;
         }
      }

      return false;
   }

   public static boolean isFriend(String player) {
      Iterator var2 = friends.iterator();

      while(var2.hasNext()) {
         Friend friend = (Friend)var2.next();
         if (friend.getName().equalsIgnoreCase(player)) {
            return true;
         }
      }

      return false;
   }

   public static Friend getFriend(String name) {
      Iterator var2 = friends.iterator();

      while(var2.hasNext()) {
         Friend friend = (Friend)var2.next();
         if (friend.getName().equalsIgnoreCase(name)) {
            return friend;
         }
      }

      return null;
   }

  

   
}

