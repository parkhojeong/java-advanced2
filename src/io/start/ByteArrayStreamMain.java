package io.start;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteArrayStreamMain {
    public static void main(String[] args) throws IOException {
        byte[] input = {1,2,3};

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(input);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byte[] bytes = byteArrayInputStream.readAllBytes();
        System.out.println(Arrays.toString(bytes));
    }
}
