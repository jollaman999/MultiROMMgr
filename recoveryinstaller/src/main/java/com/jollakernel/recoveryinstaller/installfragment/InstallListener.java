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

public interface InstallListener {
    void onInstallLog(String str);
    void onInstallComplete(boolean success);
    void onProgressUpdate(int val, int max, boolean indeterminate, String text);
    void enableCancel(boolean enabled);
    void requestRecovery(boolean force);
}
