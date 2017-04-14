package com.c.services.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.c.domain.location.ZipCode;

@Service
public class RouteService {
	
	private static final Logger log = LoggerFactory.getLogger(RouteService.class);

	public List<ZipCode> findShortestRoute(ZipCode start, ZipCode end) {
		List<ZipCode> ret = getDirections(start, end, new HashMap<ZipCode, Boolean>(), new HashMap<ZipCode, ZipCode>());
		log.info("shortest route: start=" + start.getCode() + ", end=" + end.getCode() + ", route=" + ret);
		return ret;
	}
	
	public List<ZipCode> getDirections(ZipCode start, ZipCode finish, Map<ZipCode, Boolean> vis, Map<ZipCode, ZipCode> prev){
	    List<ZipCode> directions = new LinkedList<ZipCode>();
	    Queue<ZipCode> q = new LinkedList<ZipCode>();
	    ZipCode current = start;
	    q.add(current);
	    vis.put(current, true);
	    while(!q.isEmpty()){
	        current = q.remove();
	        if (current.equals(finish)){
	            break;
	        }else{
	            for(ZipCode node : current.getNeighbors()){
	                if(!vis.containsKey(node)) {
	                    q.add(node);
	                    vis.put(node, true);
	                    prev.put(node, current);
	                }
	            }
	        }
	    }
	    if (!current.equals(finish)){
	    	log.error("can't reach destination");
	    }
	    for(ZipCode node = finish; node != null; node = prev.get(node)) {
	        directions.add(node);
	    }
	    Collections.reverse(directions);
	    return directions;
	}
}
