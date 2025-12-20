package io.file;

import java.io.File;
import java.io.IOException;

public class OldFilePath {
    public static void main(String[] args) throws IOException {
        File file = new File("temp/..");
        System.out.println("file.getPath() = " + file.getPath());

        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());

        System.out.println("file.getCanonicalFile() = " + file.getCanonicalFile());

        File[] files = file.listFiles();
        for (File f : files) {
            System.out.println((f.isFile() ? "f" : "d") + " : " + f.getName());
        }
    }
}
