/*
 * This file is part of Volume Shortcut.
 *
 * Volume Shortcut is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Volume Shortcut is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sethgnavo.volumeshortcut

import android.app.StatusBarManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.provider.Settings
import android.service.quicksettings.TileService

class VolumeTileService : TileService() {

    override fun onClick() {
        super.onClick()
        showVolumePanel()
    }

    //Opens volume options in system settings
    private fun triggerVolumeControl() {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        if (Settings.System.canWrite(this)) {
            // Intent to open sound settings
            val volumeIntent = Intent(Settings.ACTION_SOUND_SETTINGS)
            volumeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(volumeIntent)
        } else {
            // Request permission from the user
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

    //Opens sytem volume slider
    private fun showVolumePanel() {
        //collapse the quick settings drawer
        val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        applicationContext.sendBroadcast(closeIntent)

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // Trigger the system volume control to be shown
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI
        )
    }
}