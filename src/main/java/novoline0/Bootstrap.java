package novoline0;

import net.minecraft.client.main.Main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Bootstrap {
   public static void main(String[] texts) {
      Path projectDirectory = findProjectDirectory();
      Path runDirectory = projectDirectory.resolve("run");
      Path nativesDirectory = projectDirectory.resolve("test_natives").resolve(nativePlatform());
      String nativesPath = nativesDirectory.toString();

      System.setProperty("org.lwjgl.librarypath", nativesPath);
      System.setProperty("java.library.path", nativesPath);

      Main.main(concat(new String[]{
         "--version", "MCP",
         "--accessToken", "0",
         "--gameDir", runDirectory.toString(),
         "--assetsDir", runDirectory.resolve("assets").toString(),
         "--resourcePackDir", runDirectory.resolve("resourcepacks").toString(),
         "--assetIndex", "1.8",
         "--userProperties", "{}"
      }, texts));
   }

   private static Path findProjectDirectory() {
      Path workingDirectory = Paths.get("").toAbsolutePath().normalize();
      if (Files.isDirectory(workingDirectory.resolve("test_natives"))) {
         return workingDirectory;
      }

      Path parent = workingDirectory.getParent();
      if (parent != null && Files.isDirectory(parent.resolve("test_natives"))) {
         return parent;
      }

      throw new IllegalStateException("Unable to find the OpenNovoline project directory from " + workingDirectory);
   }

   private static String nativePlatform() {
      String osName = System.getProperty("os.name");
      if (osName.startsWith("Windows")) {
         return "windows";
      }
      if (osName.startsWith("Linux")) {
         return "linux";
      }
      throw new IllegalStateException("Unsupported native platform: " + osName);
   }

   public static <T> T[] concat(T[] first, T[] second) {
      T[] result = Arrays.copyOf(first, first.length + second.length);
      System.arraycopy(second, 0, result, first.length, second.length);
      return result;
   }
}
