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
package com.adtheorent.foss.utils;

import com.adtheorent.foss.assetscanner.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages multiple writers for the abstract class
 */
public class WriterManager {

    public Map<File, CsvWriter> mgrMap;

    public WriterManager() {
        mgrMap = new HashMap<File, CsvWriter>();
    }

    public void addWriter(File filename) {
        if (mgrMap.containsKey(filename)) {
            return;
        }

        try {
            mgrMap.put(filename, (new CsvWriter(filename)));
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void write(File filename, List<String> record) throws IOException {
        if(!mgrMap.containsKey(filename) || filename == null){
            System.out.println("ERROR: Filename was null or didn't exist in the WriterManager. Did you invoke addWriter after your file instantiation?");
            System.exit(1);
        }
        mgrMap.get(filename).writeRecord(record);
    }

    public void flush(File filename) throws IOException {
        if (!mgrMap.containsKey(filename)) {
            return;
        }
        mgrMap.get(filename).flush();
    }

    public void close(File filename) throws IOException {
        if (!mgrMap.containsKey(filename)) {
            return;
        }
        mgrMap.get(filename).close();
    }

    public void closeAll() throws IOException {
        for (File key : mgrMap.keySet()) {
            mgrMap.get(key).close();
        }
    }


}
