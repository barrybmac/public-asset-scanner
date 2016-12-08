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
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.regions.Region;
import com.amazonaws.services.ec2.model.DescribeAddressesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

/**
 * Ec2 class to handle fetching IPs of all of the instances.
 */
public class Ec2 {

    private AmazonEC2Client ec2;
    private Regions _region;


    public Ec2(Regions region, AWSCredentials credentials) throws IOException {
        _region = region;
        // Build a new client for the region specified
        ec2 = new AmazonEC2Client(credentials);

        // Set to the specified region
        ec2.setRegion(Region.getRegion(region));
    }

    public DescribeAddressesResult describeAddresses() {
        return ec2.describeAddresses();
    }

    public void setRegion(Regions region) {
        ec2.setRegion(Region.getRegion(region));
    }

    public Regions getRegion() {
        return _region;
    }

    public HashMap<String, String> describeInstanceAddresses() {

        // new reservation list
        List<Reservation> this_regions_reservations = ec2.describeInstances().getReservations();
        // new instances list
        ArrayList<Instance> resv_instances = new ArrayList<Instance>();
        // hashmap of instance_ids and their public IPs to return
        HashMap<String, String> instances_and_ips = new HashMap<String, String>();

        for (Reservation this_reservation : this_regions_reservations) {
            resv_instances.addAll(this_reservation.getInstances());
        }

        for (Instance this_instance : resv_instances) {
            String ip_addr = this_instance.getPublicIpAddress();
            String instance_id = this_instance.getInstanceId();
            String region = getRegion().toString();
            instances_and_ips.put(instance_id, ip_addr);

        }

        return instances_and_ips;
    }
}
