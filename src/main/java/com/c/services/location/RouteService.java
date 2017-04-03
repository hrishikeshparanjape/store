package com.c.services.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.springframework.stereotype.Service;

import com.c.domain.location.ZipCode;

@Service
public class RouteService {

	public List<ZipCode> findShortestRoute(ZipCode start, ZipCode end) {
		return doBFSShortestPath(start, end);
	}

	public List<ZipCode> doBFSShortestPath(ZipCode source, ZipCode dest) {
		ArrayList<ZipCode> shortestPathList = new ArrayList<ZipCode>();
		HashMap<ZipCode, Boolean> visited = new HashMap<ZipCode, Boolean>();

		if (source.equals(dest)) {
			return null;
		}

		Queue<ZipCode> queue = new LinkedList<ZipCode>();
		Stack<ZipCode> pathStack = new Stack<ZipCode>();

		queue.add(source);
		pathStack.add(source);
		visited.put(source, true);

		while (!queue.isEmpty()) {
			ZipCode u = queue.poll();
			Set<ZipCode> adjList = u.getNeighbors();

			for (ZipCode v : adjList) {
				if (!visited.containsKey(v)) {
					queue.add(v);
					visited.put(v, true);
					pathStack.add(v);
					if (u.equals(dest)) {
						break;
					}
				}
			}
		}

		// To find the path
		ZipCode node, currentSrc = dest;
		shortestPathList.add(dest);
		while (!pathStack.isEmpty()) {
			node = pathStack.pop();

			if (node.getNeighbors().contains(currentSrc)) {
				shortestPathList.add(node);
				currentSrc = node;
				if (node == source)
					break;
			}
		}

		return shortestPathList;
	}

}
