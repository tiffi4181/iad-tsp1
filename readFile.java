import java.io.*;
import java.util.Scanner;
import java.math.*;

public class readFile {
    public static void main(String[] args) {
        System.out.print("Enter file name of data set: ");
        String tspfile = Keyboard.readString();
        String line = null;
        double[] x = null;
        double[] y = null;
        try {
            FileReader fileReader = new FileReader(tspfile);
            Scanner s = new Scanner(new File(tspfile));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (!((line = bufferedReader.readLine()).contains("DIMENSION"))) {
                System.out.println(line);
            }
            int dim = Integer.parseInt((line.split(": "))[1]);
            x = new double[dim];
            y = new double[dim];
            System.out.println("This data set should contain " + dim + " data points. ");
            while (!((line = bufferedReader.readLine()).contains("EDGE_WEIGHT_TYPE"))) {
                System.out.println(line);
            }
            String type = (line.split(": "))[1];
            System.out.println("This data set contains information of " + type + " type. ");
            while (!((line = bufferedReader.readLine()).contains("NODE_COORD_SECTION"))) {
                System.out.println(line);
            }
            int n = 0;
            while(!(s.nextLine().contains("NODE_COORD_SECTION"))) { }
            while (!(s.next().contains("EOF"))) {
                x[n] = Double.parseDouble(s.next());
                y[n] = Double.parseDouble(s.next());
                s.nextLine();
                n++;
            }
            System.out.println("This data set contains " + n + " data points. ");
            if (n == dim) {
                System.out.println("The data set seems to be complete");
            } else {
                System.out.println("The data set seems to be incomplete");
            }
            System.out.println("#\tx\ty");
            for (int i = 0; i<n; i++) {
                System.out.println(i + "\t" + x[i] + "\t" + y[i]);
            }
            bufferedReader.close();
            s.close();
            PrintWriter writer = new PrintWriter(tspfile + "-random-lenghts.txt", "UTF-8");
            long[] verylongarray = new long[1000000];
            for(int uptoamillion = 0; uptoamillion < 1000000; uptoamillion++) {
                int[] tour = new int[n];
                int start = (int)Math.floor(Math.random() * n);
                tour[0] = start;
                for(int i = 1; i < n; i++) {
                    tour[i] = -1;
                }
                int i = 1;
                long l = 0;
                while(i < n) {
                    int random = (int)Math.floor(Math.random() * n);
                    boolean found = false;
                    for (int j = 0; j < i; j++) {
                        if (random == tour[j]) {
                            found = true;
                        }
                    }
                    if(!found) {
                        tour[i] = random;
                        if(type.contains("GEO")) {
                            l = l + geo(tour[i-1],tour[i],x,y);
                        } else {
                            l = l + euc_2d(tour[i-1],tour[i],x,y);
                        }
                        i++;
                    }
                }
                if(type.contains("GEO")) {
                    l = l + geo(tour[n-1],tour[0],x,y);
                } else {
                    l = l + euc_2d(tour[n-1],tour[0],x,y);
                }
                writer.println(l);
                verylongarray[uptoamillion] = l;
            }
            writer.close();
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file'" + tspfile + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '"+ tspfile + "'");
        }
        
    }
    
    public static int euc_2d(int i, int j, double[] x, double[] y){
    	double xd = x[i] - x[j];
    	double yd = y[i] - y[j];
    	return (int)Math.round(Math.sqrt(xd * xd + yd * yd));
    }
    
    public static int geo( int i, int j, double[] x, double[] y ) {
        int deg = (int)Math.floor(x[i]);
        double min = x[i] - deg;
        double latitude1 = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
        deg = (int)Math.floor(y[i]);
        min = y[i] - deg;
        double longitude1 = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
        deg = (int)Math.floor(x[j]);
        min = x[j] - deg;
        double latitude2 = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
        deg = (int)Math.floor(y[j]);
        min = y[j] - deg;
        double longitude2 = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
        double RRR = 6378.388;
        double q1 = Math.cos(longitude1 - longitude2);
        double q2 = Math.cos(latitude1 - latitude2);
        double q3 = Math.cos(latitude1 + latitude2);
        return (int)(RRR * Math.acos(0.5 * ((1.0 + q1) * q2 - (1.0 - q1) * q3)) + 1.0);
    }
}
