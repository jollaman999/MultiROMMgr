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

package com.jollakernel.recoveryinstaller;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jollakernel.recoveryinstaller.BuildConfig;

import eu.chainfire.libsuperuser.Application;

public class MgrApp extends Application {
    private static Context m_context;
    private static boolean m_needPkgNameFixup;

    public void onCreate(){
        super.onCreate();
        m_context = getApplicationContext();
        m_needPkgNameFixup = BuildConfig.DEBUG && m_context.getPackageName().endsWith(".debug");
    }

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(m_context);
    }

    public static ContentResolver getCntnResolver() {
        return m_context.getContentResolver();
    }

    public static Context getAppContext() {
        return m_context;
    }

    public static String replaceDebugPkgName(String str, boolean dbgToRelease) {
        if(!m_needPkgNameFixup)
            return str;

        final int suffix_len = ".debug".length();
        final String pkgName = m_context.getPackageName();
        final String pkgNameFixed = pkgName.substring(0, pkgName.length() - suffix_len);
        if(dbgToRelease)
            return str.replace(pkgName, pkgNameFixed);
        else
            return str.replace(pkgNameFixed, pkgName);
    }
}
