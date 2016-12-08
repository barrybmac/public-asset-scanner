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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.redshift.AmazonRedshiftClient;
import com.amazonaws.services.redshift.model.AmazonRedshiftException;
import com.amazonaws.services.redshift.model.Cluster;
import com.amazonaws.services.redshift.model.ClusterNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Redshift extends IpData {

    private AWSCredentials credentials;

    public Redshift(AWSCredentials creds) {
        credentials = creds;
    }

    public ScanResults scan() throws IOException {

        ScanResults result = new ScanResults();

        for (Regions this_region : Regions.values()) {
            result.add(scan(this_region));
        }

        return result;
    }

    public ScanResults scan(Regions region) throws IOException {
        ScanResults subresult = new ScanResults();
        try {
            AmazonRedshiftClient client = new AmazonRedshiftClient(credentials);
            client.setRegion(Region.getRegion(region));
            List<Cluster> cluster_list = client.describeClusters().getClusters();

            if (!cluster_list.isEmpty()) {
                for (Cluster cluster : cluster_list) {

                    List<ClusterNode> thisClusterNodeList = cluster.getClusterNodes();

                    for (ClusterNode thisNode : thisClusterNodeList) {
                        ArrayList<String> temp_list = new ArrayList<String>();
                        temp_list.add(thisNode.getPublicIPAddress());
                        temp_list.add(cluster.getClusterIdentifier() + ":" + thisNode.getNodeRole());
                        temp_list.add(region.toString());
                        temp_list.add("redshift");
                        subresult.add(temp_list);
                    }
                }
            }
        } catch (AmazonRedshiftException ex) {
            System.out.println("RedshiftScan: Unable to authenticate to " + region);
            System.out.println("RedshiftScan: Ignoring " + region);
        }
        return subresult;
    }
}
