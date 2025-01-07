package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.Config;
import me.fuzzi.dot.launcher.classes.util.Folder;
import me.fuzzi.dot.launcher.classes.util.general.Delete;
import me.fuzzi.dot.launcher.classes.util.general.Download;
import me.fuzzi.dot.launcher.classes.util.general.Link;
import me.fuzzi.dot.launcher.classes.util.general.Zip;

import java.io.File;

public class JDK {

    // Метод для скачивания JDK
    public void download() {
        Folder folder = new Folder();
        Link link = new Link();

        String path = folder.getInit() + folder.getSeparator() + "launcher" + folder.getSeparator() + "jdk";
        String jdk = path + folder.getSeparator() + "jdk-21_windows-x64_bin";
        String archive = jdk + ".zip";

        File jdkF = new File(jdk);
        File archiveF = new File(archive);

        if (jdkF.exists() && !archiveF.exists()) {
            System.out.println(link.extractFileName("jdk.already"));
        } else {
            String url = "https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.zip";

            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            Download download = new Download();
            download.fromUrl(url, path, "jdk-21_windows-x64_bin.zip");

            Zip zip = new Zip();
            zip.unzip(archive, jdk);

            Delete delete = new Delete();
            delete.delete(archive);
        }
    }

    // Получить путь до JDK
    public String getJdk() {
        Config config = new Config();
        Folder folder = new Folder();
        if (config.search("java").equals("dot")) {
            return folder.getInit() + folder.getSeparator() + "launcher" + folder.getSeparator() + "jdk" + folder.getSeparator() + "jdk-21_windows-x64_bin" + folder.getSeparator() + "jdk-21.0.5" + folder.getSeparator() + "bin" + folder.getSeparator() + "java.exe";
        } else {
            return config.search("java");
        }
    }
}