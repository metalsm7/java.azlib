package com.mparang.azlib.file;


import java.io.File;

/**
 * Created by leeyonghun on 15. 6. 13..
 */
public class AZFile {
    private File file_this;

    /**
     * Created in 2015-06-13, leeyonghun
     */
    public AZFile() {
    }

    /**
     *
     * @param p_path
     * Created in 2015-06-13, leeyonghun
     */
    public AZFile(String p_path) {
        setPath(p_path);
    }

    /**
     *
     * @param p_path
     * @return
     * Created in 2015-06-13, leeyonghun
     */
    public boolean setPath(String p_path) {
        boolean rtn_value = true;

        String path = p_path;

        File file = new File(path);
        if (!file.exists()) {
            rtn_value = false;
        }
        if (rtn_value && !file.isDirectory()) {
            rtn_value = false;
        }
        if (rtn_value) {
            this.file_this = file;
        }
        file = null;

        return rtn_value;
    }

    /**
     *
     * @param p_name
     * @return
     * Created in 2015-06-13, leeyonghun
     */
    public boolean createDirectory(String p_name) {
        boolean rtn_value = true;
        File file = new File(file_this.getAbsolutePath() + "/" + p_name);
        if (file.exists()) {
            rtn_value = false;
        }
        if (rtn_value) {
            file.mkdir();
        }
        file = null;
        return rtn_value;
    }

    /**
     *
     * @return
     * Created in 2015-06-14, leeyonghun
     */
    public File[] getList() {
        return file_this.listFiles();
    }

    /**
     *
     * @param p_filename
     * @return
     * Created in 2015-06-14, leeyonghun
     */
    public boolean exists(String p_filename) {
        boolean rtn_value;
        File file = new File(file_this.getAbsolutePath() + "/" + p_filename);
        rtn_value = file.exists();
        file = null;
        return rtn_value;
    }

    /**
     *
     * @param p_filename
     * @return
     * Created in 2015-06-14, leeyonghun
     */
    public boolean isDirectory(String p_filename) {
        boolean rtn_value;
        File file = new File(file_this.getAbsolutePath() + "/" + p_filename);
        rtn_value = file.isDirectory();
        file = null;
        return rtn_value;
    }

    /**
     *
     * @return
     * Created in 2015-06-14, leeyonghun
     */
    public int deleteAll() {
        int rtn_value = 0;
        File[] files = getList();
        int file_count = files.length;
        for (int cnti=0; cnti<file_count; cnti++) {
            rtn_value += delete(files[cnti].getName());
        }
        return rtn_value;
    }

    /**
     *
     * @param p_filename
     * @return
     * Created in 2015-06-14, leeyonghun
     */
    public int delete (String p_filename) {
        int rtn_value = 0;
        if (exists(p_filename)) {
            if (isDirectory(p_filename)) {
                AZFile azfile = new AZFile(file_this.getAbsolutePath() + "/" + p_filename);
                int file_count = azfile.getList().length;
                rtn_value = azfile.deleteAll();
                if (rtn_value >= file_count) {
                    azfile = null;

                    File file = new File(file_this.getAbsolutePath() + "/" + p_filename);
                    if (file.delete()) {
                        rtn_value++;
                    }
                    file = null;
                }
            }
            else {
                File file = new File(file_this.getAbsolutePath() + "/" + p_filename);
                if (file.delete()) {
                    rtn_value++;
                }
                file = null;
            }
        }
        return rtn_value;
    }

    /*
    public boolean save(String p_name, Bitmap p_bitmap) {
        boolean rtn_value = true;
        File file = new File(file_this.getAbsolutePath() + "/" + p_name);
        if (file.exists()) {
            rtn_value = false;
        }
        if (rtn_value) {
            FileOutputStream fileStream = null;
            try {
                file.createNewFile();

                fileStream = new FileOutputStream(file);
                p_bitmap.com
            }
            catch (Exception ex) {

            }
        }
        file = null;
        return rtn_value;
    }
    */
}
