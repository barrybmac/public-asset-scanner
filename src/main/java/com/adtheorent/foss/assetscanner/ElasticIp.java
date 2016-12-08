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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.model.Address;
import com.amazonaws.services.ec2.model.AmazonEC2Exception;
import com.amazonaws.services.ec2.model.DescribeAddressesResult;

/**
 * Class for fetching IP info from EC2 elastic IP addressing.
 */

public class ElasticIp extends IpData {

    private AWSCredentials _credentials;
    private HashMap<Regions, Ec2> ec2 = new HashMap();

    /**
     * @param credentials
     * @throws IOException
     */
    public ElasticIp(AWSCredentials credentials) throws IOException {
        _credentials = credentials;
        try {
            for (Regions region : Regions.values()) {
                ec2.put(region, new Ec2(region, credentials));
            }
        } catch (IOException ex) {
            throw ex;
        }
    }

    /**
     * Fetch IP addresses for a specific region
     *
     * @param region - Region must be specified.
     * @return Addresses
     */
    private List<Address> getIps(Regions region) {
        return ec2.get(region).describeAddresses().getAddresses();
    }


    /**
     * Scan for all Elastic IP addresses. Scans across all regions.
     *
     * @return A list of lists for iteration to write to CsvWriter.
     * @throws IOException
     */
    public ScanResults scan() throws IOException {

        DescribeAddressesResult acct_elastic_ips;
        //List<ArrayList<String>> addresses = new ArrayList<ArrayList<String>>();
        ScanResults addresses = new ScanResults();

        for (Ec2 account : ec2.values()) {

            try {
                acct_elastic_ips = account.describeAddresses();

                for (Address address : acct_elastic_ips.getAddresses()) {
                    ArrayList<String> temp_list = new ArrayList<String>();
                    temp_list.add(address.getPublicIp());
                    temp_list.add(address.getInstanceId());
                    temp_list.add(account.getRegion().toString());
                    temp_list.add("elastic-ip");
                    addresses.add(temp_list);
                }

                // If an authentication error is encountered keep on moving
            } catch (AmazonEC2Exception ex) {
                System.out.println("ElasticIPScan: Unable to authenticate to " + account.getRegion());
                System.out.println("ElasticIPScan: Ignoring " + account.getRegion());
            }
        }
        return addresses;
    }

    /**
     * Scan for all Elastic IP addresses for a specific region
     *
     * @param region - Specify your region.
     * @return A list of lists for iteration to write to CsvWriter.
     */
    public ScanResults scan(Regions region) {

        //List<ArrayList<String>> addresses = new ArrayList<ArrayList<String>>();
        ScanResults addresses = new ScanResults();
        try {
            for (Address address : getIps(region)) {
                ArrayList<String> temp_list = new ArrayList<String>();
                temp_list.add(address.getPublicIp());
                temp_list.add(address.getInstanceId());
                temp_list.add(region.toString());
                temp_list.add("elastic-ip");
                addresses.add(temp_list);
            }
        } catch (AmazonEC2Exception ex) {
            System.out.println("ElasticIPScan: Unable to authenticate to " + region);
            System.out.println("ElasticIPScan: Ignoring " + region);
        }
        return addresses;
    }
}

