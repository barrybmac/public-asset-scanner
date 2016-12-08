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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScanResults implements Iterable<List<String>>
{
    private List<List<String>> results;

    public ScanResults()
    {
        this.results = new ArrayList<List<String>>();

    }

    public void add(List<String> scanResult)
    {
        results.add(scanResult);
    }

    public void add(ScanResults scanResult)
    {
        results.addAll(scanResult.results);
    }

    public void write(File filename, WriterManager manager) throws IOException
    {
            for (List<String> record : results) {
                manager.write(filename, record);
            }
        manager.flush(filename);
    }

    public Iterator<List<String>> iterator() {
        Iterator<List<String>> results_iterator = results.iterator();
        return results_iterator;
    }
}