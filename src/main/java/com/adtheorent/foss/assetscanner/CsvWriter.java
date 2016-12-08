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


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * CsvWriter class handles CSV output of assets
 */
public class CsvWriter {

    private CSVFormat fileFormat;
    private FileWriter fileWriter;
    private CSVPrinter csvFileWriter;
    private String header = "ip,id,region,service";
    private String newlineseparator = "\n";

    public CsvWriter(File filename) throws IOException {
        setNewLineSeparator(newlineseparator);
        try {
            fileWriter = new FileWriter(filename, true);
        } catch (IOException ex) {
            throw ex;
        }
        try {
            csvFileWriter = new CSVPrinter(fileWriter, fileFormat);
        } catch (IOException e) {
            throw e;
        } finally {
            csvFileWriter.printRecord(header);
        }
    }

    public void writeRecord(List<String> record) throws IOException {
        try {
            csvFileWriter.printRecord(record);
        } catch (IOException ex) {
            throw ex;
        }
    }

    private void setNewLineSeparator(String separator) {
        fileFormat = CSVFormat.RFC4180.withRecordSeparator(separator);
    }

    public void flush() throws IOException{
        try {
            fileWriter.flush();
        } catch (IOException ex){
            throw ex;
        }
    }

    public void close() throws IOException{
        try{
            fileWriter.close();
        } catch (IOException ex){
            throw ex;
        }
    }

}
