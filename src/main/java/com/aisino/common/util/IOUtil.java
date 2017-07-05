package com.aisino.common.util;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 为 on 2017/3/31.
 */
public class IOUtil {
    /**
     * 从URL 读取文件流
     *
     * @param path
     * @return
     */
    public static byte[] httpConverBytes(String path) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        HttpURLConnection conn;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection)url.openConnection();
            if(conn.getResponseCode() != HttpStatus.OK.value())
                throw new RuntimeException("无效链接");
            in = new BufferedInputStream(conn.getInputStream());
            out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            byte[] content = out.toByteArray();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 从URL 读取文件流
     */
    public static byte[] fileConverBytes(File file) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;

        try {
            if (file != null) {
                InputStream inputStream = new FileInputStream(file);
                in = new BufferedInputStream(inputStream);
                out = new ByteArrayOutputStream(1024);
                byte[] temp = new byte[1024];
                int size;
                while ((size = in.read(temp)) != -1) {
                    out.write(temp, 0, size);
                }
                byte[] content = out.toByteArray();
                return content;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 从URL 读取文件流
     */
    public static byte[] fileConverBytes(InputStream inputStream) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;

        try {
            if (inputStream != null) {
                in = new BufferedInputStream(inputStream);
                out = new ByteArrayOutputStream(1024);
                byte[] temp = new byte[1024];
                int size;
                while ((size = in.read(temp)) != -1) {
                    out.write(temp, 0, size);
                }
                byte[] content = out.toByteArray();
                return content;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void downLoadByUrl(String url, HttpServletResponse response,String fileName){
        BufferedOutputStream bf = null;
        try {
            response.addHeader("Content-Disposition", "attachment;fileName="+fileName);
            bf = new BufferedOutputStream(response.getOutputStream());
            bf.write(IOUtil.httpConverBytes(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bf != null)
                    bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void downLoadFile(File file, HttpServletResponse response){
        BufferedOutputStream bf = null;
        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
            bf = new BufferedOutputStream(response.getOutputStream());
            bf.write(IOUtil.fileConverBytes(file));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bf != null)
                    bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void downLoadFile(InputStream inputStream,String fileName, HttpServletResponse response){
        BufferedOutputStream bf = null;
        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            bf = new BufferedOutputStream(response.getOutputStream());
            bf.write(IOUtil.fileConverBytes(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bf != null)
                    bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
