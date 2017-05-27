package com.aisino.common.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

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
        URLConnection conn;
        try {
            URL url = new URL(path);
            conn = url.openConnection();

            if (conn != null) {
                InputStream inputStream = conn.getInputStream();
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
        } catch (Exception e) {
            throw new RuntimeException("无效链接");
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


    public static void downLoadByUrl(String url, HttpServletResponse response) {
        BufferedOutputStream bf = null;
        try {
            response.addHeader("Content-Disposition", "attachment");
            bf = new BufferedOutputStream(response.getOutputStream());
            bf.write(IOUtil.httpConverBytes(url));
        } catch (Exception e) {
            throw new RuntimeException("下载失败,资源失效!");
        } finally {
            try {
                if (bf != null)
                    bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void downLoadFile(File file, HttpServletResponse response) {
        BufferedOutputStream bf = null;
        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
            bf = new BufferedOutputStream(response.getOutputStream());
            bf.write(IOUtil.fileConverBytes(file));
        } catch (Exception e) {
            throw new RuntimeException("下载失败,资源失效!");
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
