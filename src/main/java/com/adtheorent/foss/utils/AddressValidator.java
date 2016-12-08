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

import org.apache.commons.net.util.SubnetUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class AddressValidator {

    public static SubnetUtils validateCidr(String ip_addr) {
        try {
            SubnetUtils clean_ip = new SubnetUtils(ip_addr + "/32");
            return clean_ip;

        } catch (IllegalArgumentException ex) {
            System.err.println("Address " + ip_addr + " was invalid: " + ex.getMessage());
            return null;
        }
    }

    public static InetAddress[] getIpFromHostname(String fqdn){
        try {
            return InetAddress.getAllByName(fqdn);
        } catch (UnknownHostException ex){
            System.out.println(ex);
            return null;
        }
    }


}
