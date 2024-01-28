package scene;

import data.Centroid;
import data.Site;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MapViewPanel extends JPanel {

    private final JXMapViewer mapViewer;

    public MapViewPanel() {
        setLayout(new BorderLayout());

        mapViewer = new JXMapViewer();
        initMap();
        add(new JScrollPane(mapViewer), BorderLayout.CENTER);
    }

    public void initMap() {
        //set up initial location and zoom of the map
        mapViewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo("", "https://tile.openstreetmap.de")));
        mapViewer.setAddressLocation(new GeoPosition(50.132298, 8.679860));
        mapViewer.setZoom(13);

        //set up mouse listener so we can move the map
        MouseInputListener mouseListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mouseListener);
        mapViewer.addMouseMotionListener(mouseListener);
        //set up scroll listener for zoom/un-zoom map
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
    }


    public void addDots(List<Site> sites, int colorNumber, List<Centroid> centroids) {
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        Map<Integer, Color> colorMap = generateUniqueColors(colorNumber);
        Set<Waypoint> waypoints = new LinkedHashSet<>();

        for (Site site: sites) {
            waypoints.add(new CustomWaypoint(new GeoPosition(site.getLatitude(), site.getLongitude()), colorMap.get(site.getClusterNo()), false));
        }

        for (int i = 0; i < centroids.size(); i++) {
            waypoints.add(new CustomWaypoint(new GeoPosition(centroids.get(i).getLatitude(), centroids.get(i).getLongitude()), colorMap.get(i), true));
        }

        waypointPainter.setRenderer(new DotWaypointRenderer());
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);
    }


    // Method to generate n unique random colors
    public static Map<Integer, Color> generateUniqueColors(int n) {
        Map<Integer, Color> colorMap = new HashMap<>();
        Set<Color> colorSet = new HashSet<>();
        Random random = new Random();

        while (colorMap.size() < n) {
            // Generate random color
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

            // Check for uniqueness
            if (!colorSet.contains(color)) {
                colorMap.put(colorMap.size(), color);
                colorSet.add(color);
            }
        }

        return colorMap;
    }

}


