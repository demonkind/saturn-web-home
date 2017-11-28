/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.cache.data;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.log4j.Logger;


/**
 * @author su.zhang
 * @since 2007-9-14
 * @version 1.0
 */
public abstract class Convertor {

    private final static Logger          logger             = Logger.getLogger(Convertor.class);

    public static final int              LONG_BITS          = 8;
    public static final int              INT_BITS           = 4;
    public static final int              BYTE_BITS          = 1;

    static boolean                       showDebugCompress  = false;
    static boolean                       showDebugReadBytes = false;
    
    public static final int DATA_HEADER_COMPRESS     = 0x01;
    public static final int DATA_HEADER_SERILIZABLED = 0x02;
    public static final int DATA_HEADER_NORMAL       = 0x00;
    public static final int DATA_HEADER_STRING       = 0x04;
    public static final int DATA_HEADER_BYTE_ARRAY   = 0x08;
    public static final int    CACHE_DATA_MAX_LEN                                  = 1000000;
    /**
     */
    public static final int    CACHE_KEY_MAX_LEN                                   = 1024;
    /**
     */
    public static final int DATA_COMPRESS_THRESHOLD      = 1024*4;
    
    public  void show(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            System.out.println(i + ":" + bytes[i]);
        }
    }

    public  byte[] convertTwoBytes(int i) {
        byte[] b = new byte[2];

        b[0] = (byte) (i & 0xff);
        b[1] = (byte) ((i & 0xff00) >> 8);
        return b;
    }

    /**
     * Build a 4-byte array from a int. No check is performed on the array
     * length.
     * 
     * @param n
     *            The number to convert.
     * @param b
     *            The array to fill.
     * @return A byte[].
     */

    public  byte[] convertFourBytes(int i) {
        byte[] b = new byte[4];

        b[0] = (byte) (i & 0xff);
        b[1] = (byte) ((i & 0xff00) >> 8);
        b[2] = (byte) ((i & 0xff0000) >> 16);
        b[3] = (byte) ((i & 0xff000000) >> 24);
        return b;
    }

    /**
     * Build a 8-byte array from a long. No check is performed on the array
     * length.
     * 
     * @param n
     *            The number to convert.
     * @param b
     *            The array to fill.
     * @return A byte[].
     */

    public  byte[] convert8Bytes(long i) {
        byte[] b = new byte[8];
        b[0] = (byte) (i & 0xff);
        b[1] = (byte) ((i & 0xff00) >> 8);
        b[2] = (byte) ((i & 0xff0000) >> 16);
        b[3] = (byte) ((i & 0xff000000) >> 24);
        b[4] = (byte) ((i & 0xff00000000L) >> 32);
        b[5] = (byte) ((i & 0xff0000000000L) >> 40);
        b[6] = (byte) ((i & 0xff000000000000L) >> 48);
        b[7] = (byte) ((i & 0xff00000000000000L) >> 56);
        return b;
    }

    public  int toInt(byte[] b) {
        return toInt(b, 0);
    }

    public  int toInt(byte[] b, int pos) {

        return ((((int) b[pos + 0]) & 0xFF) + ((((int) b[pos + 1]) & 0xFF) << 8)
                + ((((int) b[pos + 2]) & 0xFF) << 16) + ((((int) b[pos + 3]) & 0xFF) << 24));
    }

    /**
     * Build a long from first 8 bytes of the array.
     * 
     * @param b
     *            The byte[] to convert.
     * @return A long.
     */

    public  long toLong(byte[] b) {
        return ((((long) b[0]) & 0xFF) + ((((long) b[1]) & 0xFF) << 8)
                + ((((long) b[2]) & 0xFF) << 16) + ((((long) b[3]) & 0xFF) << 24)
                + ((((long) b[4]) & 0xFF) << 32) + ((((long) b[5]) & 0xFF) << 40)
                + ((((long) b[6]) & 0xFF) << 48) + ((((long) b[7]) & 0xFF) << 56));
    }

    /**
     * Appends two bytes array into one.
     * 
     * @param a
     *            A byte[].
     * @param b
     *            A byte[].
     * @return A byte[].
     */
    public  byte[] append(byte[] a, byte[] b) {
        byte[] z = new byte[a.length + b.length];
        System.arraycopy(a, 0, z, 0, a.length);
        System.arraycopy(b, 0, z, a.length, b.length);
        return z;
    }

    /**
     * read eight bytes from inputstream and convertor it to long meanwhile
     * @param ins
     * @return
     * @throws ReceiveDataException
     * @throws IOException
     */
    public  long readLong(InputStream ins) throws CacheDataException, IOException {
        int readExpected = LONG_BITS;
        byte[] bytes = new byte[readExpected];
        int read = readBytes(ins, bytes);
        if (read < readExpected)
            throw new CacheDataException("read error.");
        return toLong(bytes);
    }

    /**
     * read four bytes from inputstream and convert it int
     * @param ins
     * @return
     * @throws ReceiveDataException
     * @throws IOException
     */
    public  int readInt(InputStream ins) throws CacheDataException, IOException {
        int readExpected = INT_BITS;
        byte[] bytes = new byte[readExpected];
        int read = readBytes(ins, bytes);
        if (read < readExpected)
            throw new CacheDataException("read error.");
        return toInt(bytes);
    }

    /**
     * read one byte from inputstream ins
     * @param ins
     * @return
     * @throws ReceiveDataException
     * @throws IOException
     */
    public  byte readByte(InputStream ins) throws CacheDataException, IOException {
        int readExpected = BYTE_BITS;
        byte[] bytes = new byte[readExpected];
        int read = readBytes(ins, bytes);
        if (read < readExpected)
            throw new CacheDataException("read error.");
        return bytes[0];
    }

    /**
     * convertor object to byte[]
     * @param o
     * @return
     * @throws IOException
     */
    public  byte[] convertData(Object object) throws CacheDataException {

        return convertData(object, new byte[] { DATA_HEADER_NORMAL });
    }

    /**
     * convertor object to byte[]
     * @param o
     * @return
     * @throws IOException
     */
    public  byte[] convertData(Object data, byte[] header) throws CacheDataException {

        if (data == null) {//code defence
            throw new CacheDataException("data in  can not be null");
        }

        byte[] bytes = null;

        if (data instanceof String) {

            header[0] += DATA_HEADER_STRING;
            bytes = ((String) data).getBytes();

        } else if (data instanceof byte[]) {
            header[0] += DATA_HEADER_BYTE_ARRAY;
            bytes = (byte[]) data;
        } else {
            header[0] += DATA_HEADER_SERILIZABLED;
            
            
            bytes = writeObject(data);
        }
        if (bytes.length > DATA_COMPRESS_THRESHOLD) {
            if (logger.isDebugEnabled()) {
                if (showDebugCompress) {
                    logger.debug("data length before compressing:" + bytes.length);
                }
            }
            double sizeOld = bytes.length;

            bytes = compress(bytes);
            header[0] += DATA_HEADER_COMPRESS;
            if (logger.isDebugEnabled()) {
                if (showDebugCompress) {
                    logger.debug("data length after compressing:" + bytes.length);
                    logger.debug("compress rate:" + (sizeOld - bytes.length) / sizeOld);
                }
            }
        }

        byte[] resBytes = append(header, bytes);

        if (resBytes.length > CACHE_DATA_MAX_LEN) {
            throw new CacheDataException("data is too long for caching.");
        }

        return resBytes;

    }

    /**
     * Convert byte[] to Object
     * 
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public  Object bytes2Object(byte[] bytes) throws CacheDataException {

        try {
            byte dataHeader = bytes[0];
            boolean compressed = false;
            byte[] realData = null;

            if ((dataHeader & DATA_HEADER_COMPRESS) == DATA_HEADER_COMPRESS) {
                realData = decompress(bytes, 1, bytes.length - 1);
                compressed = true;
            }

            //String
            if ((dataHeader & DATA_HEADER_STRING) == DATA_HEADER_STRING) {
                if (compressed) {
                    return new String(realData);
                } else {
                    return new String(bytes, 1, bytes.length - 1);
                }
            }
            //byte[]
            if ((dataHeader & DATA_HEADER_BYTE_ARRAY) == DATA_HEADER_BYTE_ARRAY) {
                if (compressed) {
                    return realData;
                } else {
                    byte[] bytes2 = new byte[bytes.length - 1];
                    System.arraycopy(bytes, 1, bytes2, 0, bytes2.length);
                    return bytes2;
                }
            }
            byte[] finalBytes;
            if (compressed) {
                finalBytes = realData;
            } else {
                finalBytes = new byte[bytes.length - 1];
                System.arraycopy(bytes, 1, finalBytes, 0, bytes.length - 1);
            }

            return readObject(finalBytes);

        } catch (DataFormatException e) {
            throw new CacheDataException(e);
        } catch (IOException e) {
            throw new CacheDataException(e);
        }
    }

    /**
     * @param input
     * @return
     * @throws IOException
     */

    public  byte[] compress(byte[] input) {

        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);
        compressor.setInput(input);
        compressor.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        byte[] buf = new byte[8192];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        compressor.end();
        return bos.toByteArray();

    }

    /**
     * @param input
     * @param offset
     * @param len
     * @return
     * @throws IOException
     * @throws DataFormatException
     */

    public  byte[] decompress(byte[] input, int offset, int len) throws IOException,
                                                                      DataFormatException {
        int processLen = 8192;
        Inflater decompressor = new Inflater();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(processLen);
        byte[] buf = new byte[processLen];
        decompressor.setInput(input, offset, len);
        while (!decompressor.finished()) {
            int count = decompressor.inflate(buf);
            if (count <= 0) {
                break;
            }
            bos.write(buf, 0, count);
        }
        decompressor.end();
        return bos.toByteArray();
    }

    public  int readBytes(InputStream ins, byte[] b) throws IOException {
        int len = b.length;
        int nleft = len;
        int offset = 0;
        int i = 0;
        while (nleft > 0) {
            i = ins.read(b, offset, nleft);
            if (i == -1) {
                break;
            }
            nleft -= i;
            offset += i;
            if (logger.isDebugEnabled()) {
                if (showDebugReadBytes) {
                    logger.debug("readBytes len:" + len + "\t nleft:" + nleft + "\t read:" + i);
                }
            }
        }

        return (len - nleft);
    }

    public  byte[] convertKey(Object key) throws CacheDataException {

        if (key == null) {//code defence
            throw new CacheDataException("key in  can not be null");
        }

        boolean converted = false;
        byte[] keyBytes = null;
        if (key instanceof Byte) {
            keyBytes = new byte[] { ((Byte) key).byteValue() };
            converted = true;
        } else if (key instanceof byte[]) {
            keyBytes = (byte[]) key;
            converted = true;
        } else if (key instanceof Integer) {
            keyBytes = convertFourBytes((Integer) key);
            converted = true;
        } else if (key instanceof Long) {
            keyBytes = convert8Bytes((Long) key);
            converted = true;
        } else if (key instanceof String) {
            keyBytes = ((String) key).getBytes();
            converted = true;
        }

        //			Class clazz = key.getClass();
        //			Method[] methods = clazz.getMethods();
        //			
        //			for(Method m:methods){
        //				if("getBytes".equals(m.getName())&& m.getParameterTypes().length==0){
        //					try{
        //					m.invoke(clazz, new Object[0]);
        //					}catch(IllegalAccessException e){
        //						throw new CacheDataException(e);
        //					}catch( InvocationTargetException e){
        //						throw new CacheDataException(e);
        //					}
        //				}
        //			}

        if (converted == false) {
            throw new CacheDataException(
                "unkown key type,the supported key type is [byte,byte[],int ,long,String] and anything that can convert to byte[] and it's length less than CACHE_KEY_MAX_LEN");
        }
        if (keyBytes.length > CACHE_KEY_MAX_LEN) {
            throw new CacheDataException("key is too long for caching.");
        }
        return keyBytes;
    }
    
    /**
     * Convert byte[] to Object
     * 
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public  Object keybytes2Object(byte[] bytes) throws CacheDataException {
    	return new String(bytes, 0, bytes.length);
    	
    }

    /**
     * @param obj
     * @return
     */
    abstract byte[] writeObject(Object obj)  throws CacheDataException;
    /**
     * @param bytes
     * @return
     * @throws IOException 
     */
    abstract Object readObject(byte[]  bytes) throws CacheDataException;

    
}
