/*      AWS Asset Scanner - Scan ALL the Services to see if they are public facing.
        Copyright (C) 2016  Barry McCall AdTheorent LLC

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package com.adtheorent.foss.assetscanner;

import com.adtheorent.foss.utils.WriterManager;
import com.amazonaws.regions.Regions;

import java.io.File;
import java.io.IOException;

/**
 * Created by bmccall on 11/11/16.
 */
public abstract class IpData {

        public abstract ScanResults scan() throws IOException;
    public abstract ScanResults scan(Regions region) throws IOException;

    public final void write(File filename, WriterManager writer) throws IOException {
        ScanResults ips = this.scan();
        ips.write(filename, writer);
    }
}
