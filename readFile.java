import java.io.*;
public class readFile {
    public static void main(String[] args) {
        System.out.print("Enter file name of data set: ");
        String tspfile = Keyboard.readString();
        String line = null;
        double[] x;
        double[] y;
        try {
            FileReader fileReader = new FileReader(tspfile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (!((line = bufferedReader.readLine()).contains("DIMENSION"))) {
                System.out.println(line);
            }
            int dim = Integer.parseInt((line.split(" : "))[1]);
            x = new double[dim];
            y = new double[dim];
            System.out.println("This data set should contain " + dim + " data points. ");
            while (!((line = bufferedReader.readLine()).contains("NODE_COORD_SECTION"))) {
                System.out.println(line);
            }
            int n = 0;
            while (!((line = bufferedReader.readLine()).contains("EOF"))) {
                String[] currentDataPoint = line.split(" ");
                x[n] = Double.parseDouble(currentDataPoint[1]);
                y[n] = Double.parseDouble(currentDataPoint[2]);
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
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + tspfile + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '"+ tspfile + "'");
        }
    }
}
// hallo