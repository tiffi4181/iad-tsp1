import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.math.*;

public class readFile {
    
    public static float[] x = null;
    public static float[] y = null;
    
    public static void main(String[] args) {
        System.out.print("Enter name of data set: ");
        String tspfile = Keyboard.readString();
        String line = null;
        try {
            FileReader fileReader = new FileReader(tspfile + ".tsp");
            Scanner s = new Scanner(new File(tspfile + ".tsp"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (!((line = bufferedReader.readLine()).contains("DIMENSION"))) { }
            int dim = Integer.parseInt((line.split(": "))[1]);
            x = new float[dim];
            y = new float[dim];
            System.out.println("This data set should contain " + dim + " data points. ");
            while (!((line = bufferedReader.readLine()).contains("EDGE_WEIGHT_TYPE"))) { }
            String type = (line.split(": "))[1];
            System.out.println("This data set contains information of " + type + " type. ");
            while (!((line = bufferedReader.readLine()).contains("NODE_COORD_SECTION"))) { }
            int n = 0;
            while(!(s.nextLine().contains("NODE_COORD_SECTION"))) { }
            while (!(s.next().contains("EOF"))) {
                x[n] = Float.parseFloat(s.next());
                y[n] = Float.parseFloat(s.next());
                s.nextLine();
                n++;
            }
            System.out.println("This data set contains " + n + " data points. ");
            if (n == dim) {
                System.out.println("The data set seems to be complete");
            } else {
                System.out.println("The data set seems to be incomplete");
            }
            bufferedReader.close();
            s.close();
            System.out.println("Data saved. What do you want to do?");
            System.out.print("Return 0 for calculation of optimum path, or 1 for generation of random paths: ");
            int choice = Keyboard.readInt();
            if (choice == 0) {
                Scanner t = new Scanner(new File(tspfile + ".opt.tour"));
                while(!(t.next().contains("TOUR_SECTION"))) { }
                int optdistance = 0;
                int r = t.nextInt();
                int start = r;
                while(t.hasNextInt()) {
                	int q = t.nextInt();
                	if(q > 0) {
                		optdistance += getDistance(type, (r - 1), (q - 1));
                		r = q;
                	}
                }
                optdistance += getDistance(type, (r - 1), (start - 1));
                System.out.println("The optimal distance as of the given file is " + optdistance + ".");
                t.close();
                
            } else if (choice == 1) {
                System.out.print("How many paths to generate? ");
                int numberofpaths = Keyboard.readInt();
                System.out.println("Generating random paths...");
                int[][] distance = new int[n][n];
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < i; j++) {
                        distance[i][j] = getDistance(type, i, j);
                    }
                }
                for(int i = 0; i < n; i++) {
                    for(int j = i+1; j < n; j++) {
                        distance[i][j] = distance[j][i];
                    }
                }
                PrintWriter writer = new PrintWriter("random-lenghts.dat", "UTF-8");
                int[] verylongarray = new int[numberofpaths];
                for(int uptoamillion = 0; uptoamillion < numberofpaths; uptoamillion++) {
                    int start = (int)(Math.random() * n);
                    int l = 0;
                    ArrayList<Integer> tourshuffled = new ArrayList<Integer>();
                    for(int i = 0; i < n; i++) {
                        if(i != start) {
                            tourshuffled.add(i);
                        }
                    }
                    Collections.shuffle(tourshuffled);
                    boolean firstnode = true;
                    int lastone = -1;
                    for(int index : tourshuffled) {
                        if(firstnode) {
                            firstnode = false;
                            l += distance[start][index];
                        } else {
                            l += distance[lastone][index];
                        }
                        lastone = index;
                    }
                    l += distance[lastone][start];
                    writer.println(l);
                    verylongarray[uptoamillion] = l;
                }
                writer.close();
                int max = 0;
                int min = 2147483647;
                for(int i = 0; i < numberofpaths; i++) {
                    if(verylongarray[i] > max) {
                        max = verylongarray[i];
                    } else if(verylongarray[i] < min) {
                        min = verylongarray[i];
                    }
                }
                System.out.println("Min: " + min + ", Max: " + max);
            } else {
                System.out.println("Incorrect input, terminating...");
            }
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file'" + tspfile + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '"+ tspfile + "'");
        }
        
    }

    public static int getDistance(String type, int i, int j){
    	if(type.contains("GEO")) {    		
            double latitude1 = Math.PI * ((int)(x[i]) + 5.0 * (x[i] - (int)(x[i])) / 3.0) / 180.0;
            double longitude1 = Math.PI * ((int)(y[i]) + 5.0 * (y[i] - (int)(y[i])) / 3.0) / 180.0;
            double latitude2 = Math.PI * ((int)(x[j]) + 5.0 * (x[j] - (int)(x[j])) / 3.0) / 180.0;
            double longitude2 = Math.PI * ((int)(y[j]) + 5.0 * (y[j] - (int)(y[j])) / 3.0) / 180.0;
            double q = Math.cos(longitude1 - longitude2);
            return (int)(6378.388 * Math.acos(0.5 * ((1.0 + q) * Math.cos(latitude1 - latitude2) - (1.0 - q) * Math.cos(latitude1 + latitude2))) + 1.0);
    	} else {
            float xd = x[i] - x[j];
            float yd = y[i] - y[j];
            return (int)(Math.round(Math.sqrt(xd * xd + yd * yd)));
    	}
    }
}
