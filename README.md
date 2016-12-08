# AWS Public IP Acces Scanner

[![N|Solid](http://adtheorent.com/v2/wp-content/uploads/2016/12/adtLogoSmall.png )](http://www.adtheorent.com)

The public-asset-scanner is a tool to help you audit and track public IPs utilized within your Amazon Web Services acount(s). It's output consists of public IP addresses, identifier strings and regions and the service used. It's output could be used for:
  - Your favorite IPAM (IP Address Management) tool
  - AWS Security Group Auditing
  - Pass IPs to security scan tools such as:
  -- nmap
  -- Other pen testing tools

You can also impliment these classes into your own project or work with the CSV output provided.

### Use (main class)
```java
// -------INPUTS--------
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
```

### Todos

 - Continue adding AWS services
 - Use of multiple credentials files
 - Maybe if it takes off, an IRC channel for help

License
----

GNU GPL

Disclaimer
----
I do not work for Amazon Web Services. While this tool does utilize AWS java libs, please do not contact amazon regarding support regarding use and/or support of this tool.
