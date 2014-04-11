/*
 * Copyright 2014 FlatCap Studios.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package launcher;

/**
 *
 * @author Halcyonix <halcyonix@hotmail.com>
 * @author FranklinL <franko@franklinl.com>
 */ 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;



public class Download {
    static double filesize;
    private static class ProgressListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // e.getSource() gives you the object of DownloadCountingOutputStream
            // because you set it in the overriden method, afterWrite().
            double d  = ((DownloadCountingOutputStream) e.getSource()).getByteCount();
            double dpercent = (d / filesize) * 100;
            int percent = (int) dpercent;
            System.out.println(percent);
            mainFrame.instaBar.setValue(percent);
            if(percent == 100) {
                mainFrame.instaBar.setString("Download Complete");
                mainFrame.instaBar.setValue(0);
            }
        }
    }
    /**
     * This method downloads a file from the URL specified by calling fileDownload and updates the GUI in a new thread
     * @param fileurl The URL that the download needs to occur
     * @see fileDownload
     * 
     */
    public static void fileGUIdownload(final String fileurl) {
        Runnable runner = new Runnable() {
            public void run() {
                try {
                    Download.fileDownload(fileurl);
                    }  catch (IOException ex) {
                        Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            Thread t = new Thread(runner, "Code Executer");
            t.start();
    }
    /**
     * This method is for downloading a file from a URL. 
     * @param fileurl The URL that the download needs to occur
     * @throws IOException 
     */
    public static void fileDownload(String fileurl) throws IOException {
        //init var
        URL dl = null;
        File fl = null;
        String x = null;
        OutputStream os = null;
        InputStream is = null;
        ProgressListener progressListener = new ProgressListener();
        try {
            
            //string to the URL
            dl = new URL(fileurl);
            //grab file name
            String fileName = FilenameUtils.getBaseName(fileurl);
            String extension = FilenameUtils.getExtension(fileurl);
            //storage location
            fl = new File(System.getProperty("user.home").replace("\\", "/") + "/Desktop/"+ fileName + "." + extension);
            //file stream output at storage location
            os = new FileOutputStream(fl);
            is = dl.openStream();
            //new instance of DownloadCountingOutputStream
            DownloadCountingOutputStream dcount = new DownloadCountingOutputStream(os);
            dcount.setListener(progressListener);

            // this line give you the total length of source stream as a double.
            filesize = Double.parseDouble(dl.openConnection().getHeaderField("Content-Length"));

            // begin transfer by writing to dcount for download progress count. *not os.*
            IOUtils.copy(is, dcount);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (os != null) { 
                os.close(); 
            }
            if (is != null) { 
                is.close(); 
            }
        }
    }
}
