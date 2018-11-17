/*
 * This file is part of MultiROM Manager.
 *
 * MultiROM Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MultiROM Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MultiROM Manager. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jollakernel.recoveryinstaller.installfragment;

import android.os.AsyncTask;

import com.jollakernel.recoveryinstaller.Device;
import com.jollakernel.recoveryinstaller.MultiROM;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class UbuntuManifestAsyncTask extends AsyncTask<Object, Void, UbuntuManifestAsyncTask.Result> {

    static final int RES_OK                 = 0x00;
    static final int RES_CHANNELS_FAIL      = 0x01;

    public interface UbuntuManifestAsyncTaskListener {
        public void onUbuntuTaskFinished(Result res);
    }

    private static UbuntuManifestAsyncTask instance = null;
    public static UbuntuManifestAsyncTask instance() {
        if(instance == null)
            instance = new UbuntuManifestAsyncTask();
        return instance;
    }

    public static void destroy() {
        instance = null;
    }

    private UbuntuManifestAsyncTask() {
        super();
        m_card = new WeakReference<UbuntuCard>(null);
        m_res = null;
        m_listener = null;
    }

    public void setListener(UbuntuManifestAsyncTaskListener listener) {
        m_listener = listener;
        if(m_listener != null && m_res != null)
            m_listener.onUbuntuTaskFinished(m_res);
    }

    public void setCard(UbuntuCard card) {
        m_card = new WeakReference<UbuntuCard>(card);
        applyResult();
    }

    public void executeTask(Device dev, MultiROM multirom) {
        if(this.getStatus() == Status.PENDING)
            this.execute(dev, multirom);
    }

    @Override
    protected UbuntuManifestAsyncTask.Result doInBackground(Object... args) {
        Device dev = (Device)args[0];
        MultiROM multirom = (MultiROM)args[1];
        Result res = new Result();

        UbuntuManifest man = new UbuntuManifest();
        if(!man.downloadAndParse(dev)) {
            res.code = RES_CHANNELS_FAIL;
            return res;
        }

        res.manifest = man;
        res.freeSpace = multirom.getFreeSpaceMB();

        // TODO: code to find USB partitions
        return res;
    }

    protected void onPostExecute(Result res) {
        m_res = res;
        applyResult();
        if(m_listener != null && m_res != null)
            m_listener.onUbuntuTaskFinished(res);
    }

    private void applyResult() {
        UbuntuCard card = m_card.get();

        if(card == null || m_res == null)
            return;

        card.applyResult(m_res);
    }

    public void putInstallInfo(UbuntuInstallInfo i) {
        m_installInfo = i;
    }

    public UbuntuInstallInfo getInstallInfo() {
        return m_installInfo;
    }

    public class Result {
        public int code = RES_OK;
        public UbuntuManifest manifest = null;
        public ArrayList<String> m_destinations = new ArrayList<String>();
        public int freeSpace;
    }

    private WeakReference<UbuntuCard> m_card;
    private UbuntuManifestAsyncTaskListener m_listener;
    private Result m_res;
    private UbuntuInstallInfo m_installInfo;
}
