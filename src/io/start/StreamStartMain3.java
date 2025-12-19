package io.start;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class StreamStartMain3 {
    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("temp/hello.dat");

        byte[] input = {65, 66, 67, 68};
        fileOutputStream.write(input);
        fileOutputStream.close();

        FileInputStream fileInputStream = new FileInputStream("temp/hello.dat");
        byte[] buffer = new byte[10];
        int readCount = fileInputStream.read(buffer, 0, 10);
        System.out.println("readCount = " + readCount);
        System.out.println("buffer = " + Arrays.toString(buffer));
        fileInputStream.close();

    }
}
