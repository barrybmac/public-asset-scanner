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
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.AmazonElasticLoadBalancingException;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import com.adtheorent.foss.utils.AddressValidator;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Class to fetch Public IP addresses and AWS FQDN of Elastic Load Balancers
 */
public class ElbIp extends IpData {

    private AWSCredentials credentials;
    private HashMap<Regions, AmazonElasticLoadBalancingClient> elbclient = new HashMap();
    private ArrayList<Regions> keyremovals = new ArrayList<Regions>();
    private HashMap<Regions, List<InetAddress>> elbIp = new HashMap();


    public ElbIp(AWSCredentials credentials) throws IOException {
        this.credentials = credentials;
    }


    public ScanResults scan() {

        ScanResults result = new ScanResults();

        for (Regions this_region : Regions.values()) {
            this.elbclient.put(this_region, new AmazonElasticLoadBalancingClient(credentials));
        }

        for (Regions region : elbclient.keySet()) {
            List<LoadBalancerDescription> res = new ArrayList<LoadBalancerDescription>();
            this.elbclient.get(region).setRegion(Region.getRegion(region));
            try {
                res = this.elbclient.get(region).describeLoadBalancers().getLoadBalancerDescriptions();
            } catch (AmazonElasticLoadBalancingException ex) {
                keyremovals.add(region);
            }

            List<InetAddress> these_balancers = new ArrayList<InetAddress>();
            for ( LoadBalancerDescription this_desc : res ){
                // Build an array of ALL the balancers
                these_balancers.addAll(Arrays.asList(AddressValidator.getIpFromHostname(this_desc.getDNSName())));
            }
            this.elbIp.put(region, these_balancers );
        }
        //Remove the failed region
        for (Regions this_region : keyremovals){
            elbIp.remove(this_region);
            System.out.println("ELBIPScan: Unable to authenticate to " + this_region);
            System.out.println("ELBIPScan: Ignoring " + this_region);
        }
        //List<InetAddress> ip
        //"ip,instance,region,service"
        for (Regions region: elbIp.keySet()) {
            for(InetAddress ip : elbIp.get(region)){
                ArrayList<String> temp_list = new ArrayList<String>();
                temp_list.add(ip.getHostAddress());
                temp_list.add(ip.getHostName());
                temp_list.add(region.toString());
                temp_list.add("elastic-load-balancing");
                result.add(temp_list);
            }
        }

        return result;
    }

    public ScanResults scan(Regions region){
        ScanResults result = new ScanResults();

        List<LoadBalancerDescription> res = new ArrayList<LoadBalancerDescription>();

        AmazonElasticLoadBalancingClient single_region = new AmazonElasticLoadBalancingClient(credentials);
        single_region.setRegion(Region.getRegion(region));
        res.addAll(single_region.describeLoadBalancers().getLoadBalancerDescriptions());

        List<InetAddress> these_balancers = new ArrayList<InetAddress>();
        for ( LoadBalancerDescription this_desc : res ){
            // Build an array of ALL the balancers
            these_balancers.addAll(Arrays.asList(AddressValidator.getIpFromHostname(this_desc.getDNSName())));
        }
           for(InetAddress ip : these_balancers){
                ArrayList<String> temp_list = new ArrayList<String>();
                temp_list.add(ip.getHostAddress());
                temp_list.add(ip.getHostName());
                temp_list.add(region.toString());
                temp_list.add("elastic-load-balancing");
                result.add(temp_list);
            }
        return result;
    }
}