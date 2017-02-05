package com.c.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.c.domain.Address;
import com.c.domain.GeoLocation;
import com.c.exceptions.AddressValidationException;

@Service
public class AddressLookupService {

	@Autowired
	private ResourceLoader resourceLoader;

	private ZipFile usWestAddresses;
	private Map<String, HashSet<ZipEntry>> postCodeZipEntryMapping = new HashMap<String, HashSet<ZipEntry>>();

	@PostConstruct
	public void init() throws IOException {
		usWestAddresses = new ZipFile(
				resourceLoader.getResource("file:/tmp/addresses/openaddr-collected-us_west.zip").getFile());
		Enumeration<? extends ZipEntry> entries = usWestAddresses.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (!entry.getName().contains("summary") && entry.getName().endsWith("csv")) {
				BufferedReader br = null;
				String line;
				try {
					br = new BufferedReader(new InputStreamReader(usWestAddresses.getInputStream(entry)));
					while ((line = br.readLine()) != null) {
						String[] parts = line.split(",");
						if (parts.length != 11) {
							continue;
						}
						HashSet<ZipEntry> zipEntriesForPostCode = postCodeZipEntryMapping.get(parts[8]);
						if(zipEntriesForPostCode == null) {
							zipEntriesForPostCode = new HashSet<ZipEntry>();
						}
						zipEntriesForPostCode.add(entry);
						postCodeZipEntryMapping.put(parts[8], zipEntriesForPostCode);
					}
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public GeoLocation getGeoLocationByAddress(Address address) throws AddressValidationException {
		Set<ZipEntry> zipEntriesToScan = postCodeZipEntryMapping.get(address.getPostCode());
		for(ZipEntry entry : zipEntriesToScan) {			
			BufferedReader br = null;
			String line;
			try {
				br = new BufferedReader(new InputStreamReader(usWestAddresses.getInputStream(entry)));
				while ((line = br.readLine()) != null) {
					String[] parts = line.split(",");
					if (parts.length != 11) {
						continue;
					}
					if (parts[8].equals(address.getPostCode())) {
						String numberAndStreetDatabase = parts[2] + parts[3].split(" ")[0];
						String[] inputParts = address.getLine1().split(" ");
						String numberAndStreetInput = inputParts[0] + inputParts[1];
						if(numberAndStreetDatabase.equalsIgnoreCase(numberAndStreetInput)) {
							GeoLocation ret = new GeoLocation();
							ret.setLongitude(parts[0]);
							ret.setLatitude(parts[1]);
							return ret;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		throw new AddressValidationException("Invalid address: address=" + address.toString());
		
	}	
}
