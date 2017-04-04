package com.c.services.location;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.c.domain.location.GeoLocation;
import com.c.exceptions.AddressValidationException;

@Service
@Profile({"aws","local"})
public class AddressLookupService implements IAddressLookupService {

	@Autowired
	private ResourceLoader resourceLoader;

	private ZipFile usWestAddresses;
	private Map<String, HashSet<ZipEntry>> postCodeZipEntryMapping = new HashMap<String, HashSet<ZipEntry>>();

	@PostConstruct
	public void init() throws IOException {
		URL website = new URL("https://s3-us-west-1.amazonaws.com/storeuser-publicbucket/addresses/openaddr-collected-us_west.zip");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("/tmp/openaddr-collected-us_west.zip");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		
		usWestAddresses = new ZipFile(
				resourceLoader.getResource("file:/tmp/openaddr-collected-us_west.zip").getFile());
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

	public GeoLocation getGeoLocationByAddress(String postCode, String addressLine1) throws AddressValidationException {
		Set<ZipEntry> zipEntriesToScan = postCodeZipEntryMapping.get(postCode);
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
					if (parts[8].equals(postCode)) {
						String numberAndStreetDatabase = parts[2] + parts[3].split(" ")[0];
						String[] inputParts = addressLine1.split(" ");
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
		
		throw new AddressValidationException("Invalid address: address=" + addressLine1 + ", zip=" + postCode);
		
	}	
}
