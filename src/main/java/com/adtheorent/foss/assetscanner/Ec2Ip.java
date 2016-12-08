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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.model.AmazonEC2Exception;


/**
 * Class for fetching public IP info from EC2 instances.
 */
public class Ec2Ip extends IpData {

    private AWSCredentials _credentials;
    private HashMap<Regions, Ec2> ec2 = new HashMap();

    public Ec2Ip(AWSCredentials credentials) throws IOException {

        _credentials = credentials;
        try {
            for (Regions region : Regions.values()) {
                ec2.put(region, new Ec2(region, credentials));
            }
        } catch (IOException ex) {
            throw ex;
        }
    }

    private HashMap<String, String> getIps(Regions region) {
        return ec2.get(region).describeInstanceAddresses();
    }


    /**
     * Scan for all EC2 public IP addresses. Scans across all regions.
     *
     * @return A list of lists for iteration to write to CsvWriter.
     * @throws IOException
     */
    public ScanResults scan() throws IOException {

        //List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        ScanResults result = new ScanResults();

        for (Ec2 account : ec2.values()) {
            try {

                HashMap<String, String> ec2ips = account.describeInstanceAddresses();

                for (Map.Entry<String, String> instance : ec2ips.entrySet()) {
                    ArrayList<String> temp_list = new ArrayList<String>();
                    temp_list.add(instance.getValue());
                    temp_list.add(instance.getKey());
                    temp_list.add(account.getRegion().toString());
                    temp_list.add("elastic-compute");
                    result.add(temp_list);
                }
            } catch (AmazonEC2Exception ex) {
                System.out.println("EC2IPScan: Unable to authenticate to " + account.getRegion());
                System.out.println("EC2IPScan: Ignoring " + account.getRegion());
            }
        }

        return result;
    }

    /**
     * Scan for all Public IP addresses associated with an EC2 instance for a specific region
     *
     * @param region - Specify your region
     * @return A list of lists for iteration to write to CsvWriter
     */
    public ScanResults scan(Regions region) {

        //List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        ScanResults result = new ScanResults();
        try {


            for (Map.Entry<String, String> instance : getIps(region).entrySet()) {
                ArrayList<String> temp_list = new ArrayList<String>();
                temp_list.add(instance.getValue());
                temp_list.add(instance.getKey());
                temp_list.add(region.toString());
                temp_list.add("elastic-compute");
                result.add(temp_list);
            }

        } catch (AmazonEC2Exception ex) {
            System.out.println("EC2IPScan: Unable to authenticate to " + region.toString());
            System.out.println("EC2IPScan: Ignoring " + region);

        }
        return result;
    }
}
