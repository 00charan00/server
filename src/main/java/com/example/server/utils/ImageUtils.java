package com.example.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {

    public static final int BIT_SIZE = 4 *1024;

    public static byte[] compressImage(byte[] pic) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(pic);
        deflater.finish();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(pic.length);
        byte[] tmp = new byte[BIT_SIZE];

        while (!deflater.finished()){
            int size = deflater.deflate(tmp);
            byteArrayOutputStream.write(tmp,0,size);
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }


    public static byte[] decompressImage(byte[] compressedPic) throws DataFormatException, IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedPic);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(compressedPic.length);
        byte[] tmp = new byte[BIT_SIZE];

        while(!inflater.finished()){
            int count = inflater.inflate(tmp);
            byteArrayOutputStream.write(tmp,0,count);
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

}