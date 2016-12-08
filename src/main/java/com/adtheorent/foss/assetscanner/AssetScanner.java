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

import com.adtheorent.foss.utils.PropertiesUtils;
import com.adtheorent.foss.utils.WriterManager;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AssetScanner {

    public static void main(String[] args) {
        try {
            // -------INPUTS--------
            // TODO handle multiple acct/cred scanning
            // See the README in src/main/java/resources
            // for credentials info
            AWSCredentials credentials = PropertiesUtils.getAwsCreds("example.awscredentials.properties");

            // -------OUTPUTS-------
            // The writer manager is crucial
            // Make a new writer manager for our scan
            WriterManager writerMgr = new WriterManager();

            // WriterManager provides standard 4 column output
            // in RFC4180 CSV (ip,id,region,service)
            File elasticIPs = new File("/tmp/elastic-ips.csv");
            File loadBalancers = new File("/tmp/loadBalancers.csv");
            File useast1file = new File("/tmp/US-EAST1.csv");

            // Add all the files you'd like to the writer manager
            writerMgr.addWriter(elasticIPs);
            writerMgr.addWriter(loadBalancers);
            writerMgr.addWriter(useast1file);

            // ----------EXAMPLES-----------------
            // Simple scanning of a service across all
            // regions can be achieved by utilizing the scan() method.

            // Available services to scan, at this time, are:
            // ElasticIP - Amazon Elastic IPs
            // Ec2IP - Amazon EC2 instances with public IPs assigned.
            // ElbIP - All public IPs associated with an ELB endpoint.
            // Redshift - All redshift clusters and node members.

            // Example 1:
            // Scan ALL ElasticIPs across all regions,
            // write the output to the elasticIPs file using the WriterManager
            // Light is green, trap is clean.
            new ElasticIp(credentials).write(elasticIPs, writerMgr);

            // Example 2:
            // Scan ElasticIPs in US_EAST1 and write to the US-EAST1.csv file:
            new ElasticIp(credentials).scan(Regions.US_EAST_1).write(useast1file, writerMgr);
            // Scan EC2 instance IPs in US_EAST1 and write them to the US-EAST1.csv file:
            new Ec2Ip(credentials).scan(Regions.US_EAST_1).write(useast1file, writerMgr);
            // Scan all ELB IPs in US_EAST1 and write them to the US-EAST1.csv file:
            new ElbIp(credentials).scan(Regions.US_EAST_1).write(useast1file, writerMgr);

            // Example 3:
            // Work directly with ScanResults if you'd like
            // In this example we just output the array string to System.out
            ScanResults elbIPs = new ElbIp(credentials).scan(Regions.US_WEST_2);

           for (List<String> this_result : elbIPs) {
               System.out.println(this_result.toString());
           }

            // Example Redshift write to the same output file used
            // in example 2
            new Redshift(credentials).write(useast1file, writerMgr);

            // Clean up after yourself. Don't go leavin' open file handles.
            writerMgr.closeAll();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
