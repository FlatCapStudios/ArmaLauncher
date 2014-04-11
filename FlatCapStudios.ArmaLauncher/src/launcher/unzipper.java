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
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;
/**
 *
 * @author a01pasto <a01.pastoukhovi@hotmail.com>
 * @author FranklinL <franko@franklinl.com>
 */
public class unzipper {
    public static void fileGUIunzip(final String source, final String destination, final String password) {
        Runnable runner = new Runnable() {
            public void run() {
                unzip(source, destination, password);
                }
            };
            Thread t = new Thread(runner, "Code Executer");
            t.start();
    }
    public static void unzip(String source, String destination, String password){
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.setRunInThread(true);
             
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(destination);
            ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
            while(progressMonitor.getState() == ProgressMonitor.STATE_BUSY){
                int percent = progressMonitor.getPercentDone();
                String filename = progressMonitor.getFileName();
                mainFrame.instaBar.setString("Unziping " + filename);
                mainFrame.instaBar.setValue(percent);
                System.out.println(percent);
            }
            if(progressMonitor.getState() == ProgressMonitor.RESULT_SUCCESS) {
                    mainFrame.instaBar.setString("Unzip Complete");
                    mainFrame.instaBar.setValue(0);
                    mainFrame.outputBox.append("\nDEBUG: DONE UNZIPPING");
                }
        } catch (ZipException e) {}
    }
}
