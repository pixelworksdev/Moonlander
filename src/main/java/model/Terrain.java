package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Terrain{
    // -------------------------------
    // Terrain data
    // -------------------------------
    private List<Point> surfacePoints = new ArrayList<>(); // Points defining the terrain surface
    private List<LandingZone> zones = new ArrayList<>();   // Flat landing zones
    private int width;
    private int height;
    private Random rand = new Random();

    // -------------------------------
    // Constructor: generate terrain and landing zones
    // -------------------------------
    public Terrain(int panelWidth, int panelHeight){
        this.width = panelWidth;
        this.height = panelHeight;
        generateTerrain();        // Generate the moon surface
        generateLandingZones();   // Create safe landing areas
    }

    // -------------------------------
    // Generate terrain using midpoint displacement
    // -------------------------------
    private void generateTerrain(){
        surfacePoints.clear();
        int segments = width;
        int[] points = new int[segments];

        int minY = height / 3;
        int maxY = height - 80;

        // Start and end points
        points[0] = rand.nextInt(maxY - minY) + minY;
        points[segments - 1] = rand.nextInt(maxY - minY) + minY;

        // Recursively generate terrain
        midpointDisplacement(points, 0, segments - 1, (maxY - minY) / 2);

        // Store points in surfacePoints list
        for(int x = 0; x < segments; x++)
            surfacePoints.add(new Point(x, points[x]));

        // Smooth the terrain for more natural appearance
        smoothSurface(3);
    }

    // -------------------------------
    // Midpoint displacement algorithm for terrain generation
    // -------------------------------
    private void midpointDisplacement(int[] points, int start, int end, int displacement){
        if (end - start <= 1) return;

        int mid = (start + end) / 2;
        int avg = (points[start] + points[end]) / 2;
        points[mid] = avg + rand.nextInt(displacement * 2 + 1) - displacement;

        // Clamp to screen bounds
        points[mid] = Math.max(height / 4, Math.min(height - 50, points[mid]));

        // Recursive subdivision
        midpointDisplacement(points, start, mid, displacement / 2);
        midpointDisplacement(points, mid, end, displacement / 2);
    }

    // -------------------------------
    // Smooth terrain by averaging neighbor points
    // -------------------------------
    private void smoothSurface(int window){
        int size = surfacePoints.size();
        int[] smoothed = new int[size];

        for(int i = 0; i < size; i++){
            int sum = 0, count = 0;
            for(int j = -window; j <= window; j++){
                int idx = i + j;
                if(idx >= 0 && idx < size){
                    sum += surfacePoints.get(idx).y;
                    count++;
                }
            }
            smoothed[i] = sum / count;
        }

        // Apply smoothed values
        for(int i = 0; i < size; i++)
            surfacePoints.get(i).y = smoothed[i];
    }

    // -------------------------------
    // Generate multiple landing zones
    // -------------------------------
    private void generateLandingZones(){
        zones.clear();
        int zoneCount = 3;
        int zoneWidth = 40;
        int minSpacing = 50;

        for(int i = 0; i < zoneCount; i++){
            int startX;
            boolean valid;
            do{
                startX = rand.nextInt(width - zoneWidth - 10) + 5;
                final int testX = startX;
                valid = zones.stream().noneMatch(z -> Math.abs(z.getZone().x - testX) < minSpacing);
            }while(!valid);

            // Calculate average Y to flatten terrain
            int avgY = 0;
            for(int x = startX; x < startX + zoneWidth; x++){
                avgY += surfacePoints.get(x).y;
            }
            avgY /= zoneWidth;

            // Create and store the landing zone
            zones.add(new LandingZone(startX, avgY - 5, zoneWidth, 5));

            // Flatten terrain for this zone
            for(int x = startX; x < startX + zoneWidth; x++){
                surfacePoints.get(x).y = avgY;
            }
        }
    }

    // -------------------------------
    // Get list of landing zones
    // -------------------------------
    public List<LandingZone> getZones(){
        return zones;
    }

    // -------------------------------
    // Check if a point is inside any landing zone
    // -------------------------------
    public boolean isSafeLanding(double landerX, double landerY){
        return zones.stream().anyMatch(zone -> zone.contains(landerX, landerY));
    }

    // -------------------------------
    // Draw terrain and landing zones
    // -------------------------------
    public void draw(Graphics2D g){
        // Draw terrain surface
        g.setColor(Color.LIGHT_GRAY);
        for(int i = 0; i < surfacePoints.size() - 1; i++){
            Point p1 = surfacePoints.get(i);
            Point p2 = surfacePoints.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Draw landing zones
        g.setColor(Color.GREEN);
        for(LandingZone zone : zones){
            Rectangle z = zone.getZone();
            g.fillRect(z.x, z.y, z.width, z.height);
        }
    }

    // -------------------------------
    // Get terrain height at specific X position
    // -------------------------------
    public int getHeightAt(int x){
        if(x <= 0) return surfacePoints.get(0).y;
        if(x >= surfacePoints.size() - 1) return surfacePoints.get(surfacePoints.size() - 1).y;
        return surfacePoints.get(x).y;
    }
}