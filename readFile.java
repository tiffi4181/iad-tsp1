import java.io.*;
import java.util.Scanner;
import java.math.*;

public class readFile {
    public static void main(String[] args) {
        System.out.print("Enter file name of data set: ");
        String tspfile = Keyboard.readString();
        String line = null;
        double[] x=null;
        double[] y=null;
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
            String type = (line.split(" : "))[1];
            System.out.println("This data set contains information of " + type + " type. ");
            while (!((line = bufferedReader.readLine()).contains("NODE_COORD_SECTION"))) {
                System.out.println(line);
            }
            int n = 0;
            while(!(s.nextLine().contains("NODE_COORD_SECTION"))) { }
            while (!(s.next().contains("EOF"))) {
                x[n] = s.nextDouble();
                y[n] = s.nextDouble();
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
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + tspfile + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '"+ tspfile + "'");
        }
        
        System.out.println(euc_2d(0, 1, x, y));
        
    }
    
    public static int euc_2d(int i, int j, double x[], double y[]){
    	double xd=x[i]-x[j];
    	double yd=y[i]-y[j];
    	int dij=(int)Math.round(Math.sqrt(xd*xd+ yd*yd));
    	return dij;
    }
}
