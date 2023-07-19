package com.poster.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {
    //TODO: check exception

    public static byte[] compressImage(byte[] data) {
        var deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        byte[] tmp = new byte[4 * 1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        try {
            while (!inflater.finished()) {
                int size = inflater.inflate(tmp);
                outputStream.write(tmp, 0, size);
            }

            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }


}
