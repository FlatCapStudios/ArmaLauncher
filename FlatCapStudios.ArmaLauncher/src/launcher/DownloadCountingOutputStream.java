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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.CountingOutputStream;

/**
 *
 * @author FranklinL <franko@franklinl.com>
 */
public class DownloadCountingOutputStream extends CountingOutputStream {

    private ActionListener listener = null;

    public DownloadCountingOutputStream(OutputStream out) {
        super(out);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    protected void afterWrite(int n) throws IOException {
        super.afterWrite(n);
        if (listener != null) {
            listener.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

}