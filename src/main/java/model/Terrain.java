package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Terrain{
    private List<Point> surfacePoints = new ArrayList<>();
    private List<LandingZone> zones = new ArrayList<>();
    private int width;
    private int height;
    private Random rand = new Random();

    public Terrain(int panelWidth, int panelHeight){
        this.width = panelWidth;
        this.height = panelHeight;
        generateTerrain();
        generateLandingZones();
    }

    private void generateTerrain(){
        surfacePoints.clear();
        int segments = width;
        int[] points = new int[segments];

        int minY = height / 3;
        int maxY = height - 80;

        points[0] = rand.nextInt(maxY - minY) + minY;
        points[segments - 1] = rand.nextInt(maxY - minY) + minY;

        midpointDisplacement(points, 0, segments - 1, (maxY - minY) / 2);

        for(int x = 0; x < segments; x++)
            surfacePoints.add(new Point(x, points[x]));


        smoothSurface(3);
    }

    private void midpointDisplacement(int[] points, int start, int end, int displacement){
        if (end - start <= 1) return;

        int mid = (start + end) / 2;
        int avg = (points[start] + points[end]) / 2;
        points[mid] = avg + rand.nextInt(displacement * 2 + 1) - displacement;

        points[mid] = Math.max(height / 4, Math.min(height - 50, points[mid]));

        midpointDisplacement(points, start, mid, displacement / 2);
        midpointDisplacement(points, mid, end, displacement / 2);
    }

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

        for(int i = 0; i < size; i++)
            surfacePoints.get(i).y = smoothed[i];

    }

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

            int avgY = 0;
            for(int x = startX; x < startX + zoneWidth; x++){
                avgY += surfacePoints.get(x).y;
            }
            avgY /= zoneWidth;

            zones.add(new LandingZone(startX, avgY - 5, zoneWidth, 5));

            for(int x = startX; x < startX + zoneWidth; x++){
                surfacePoints.get(x).y = avgY;
            }
        }
    }

    public List<LandingZone> getZones(){
        return zones;
    }

    public boolean isSafeLanding(double landerX, double landerY){
        return zones.stream().anyMatch(zone -> zone.contains(landerX, landerY));
    }

    public void draw(Graphics2D g){
        g.setColor(Color.LIGHT_GRAY);
        for(int i = 0; i < surfacePoints.size() - 1; i++){
            Point p1 = surfacePoints.get(i);
            Point p2 = surfacePoints.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        g.setColor(Color.GREEN);
        for(LandingZone zone : zones){
            Rectangle z = zone.getZone();
            g.fillRect(z.x, z.y, z.width, z.height);
        }
    }

    public int getHeightAt(int x){
        if(x <= 0) return surfacePoints.get(0).y;
        if(x >= surfacePoints.size() - 1) return surfacePoints.get(surfacePoints.size() - 1).y;
        return surfacePoints.get(x).y;
    }
}