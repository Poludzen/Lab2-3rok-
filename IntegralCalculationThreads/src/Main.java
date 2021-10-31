class IntegralCalculationsMethod{
    public static double f(double x){
        return x*x;
    }
    public static double left_rectangles(double[] points){
        double result_value = 0;
        for(int i = 0; i<points.length-1;i++){
            result_value += f(points[i])*(points[i+1] - points[i]);
        }
        return result_value;
    }
    public static double simpson(double[] points){
        double result_value = 0;
        for(int i = 0; i<points.length-1;i++){
            result_value += (f(points[i])+4*f((points[i+1]+points[i])/2)+f(points[i+1]))*(points[i+1] - points[i])/6;
        }
        return result_value;
    }
    public static double trapezoid(double [] points){
        double result_value = 0;
        for(int i = 0; i<points.length-1;i++){
            result_value += (f(points[i])+f(points[i+1]))/2*(points[i+1] - points[i]);
        }
        return result_value;
    }
}

class MyThreadForIntegral extends Thread{
    // method = 0 - left_rectangles, 1 - simpson, 2 - trapezoid
    private int method;
    private double[] points;
    private double result = 0;
    public MyThreadForIntegral(int method,double[]points ){
        this.method = method;
        this.points = points.clone();
    }
    // we think that we use it correctly(after join)
    public double get_result(){
        return result;
    }
    @Override
    public void run(){
        if(!Thread.interrupted()){
            if(method==0){
                result = IntegralCalculationsMethod.left_rectangles(points);
            }else if(method==1){
                result = IntegralCalculationsMethod.simpson(points);
            }else {
                result = IntegralCalculationsMethod.trapezoid(points);
            }
        }
    }


}


public class Main {
    public static void main(String[] args) {
        double[] points_for_rectangles = new double[5];
        double[] points_for_simpsons = new double[5];
        double[] points_for_trapezoid = new double[5];
        // interval from 0 to 3, rectangles : 0 to 1; simpson from 1 to 2 and trapezoid from 2 to 3
        for(int i = 0; i<5;i++){
            points_for_rectangles[i] = 0+0.25*i;
            points_for_simpsons[i] = 1+0.25*i;
            points_for_trapezoid[i] = 2+0.25*i;
        }
        int trapezoid_method = 2;
        int simpson_method = 1;
        int rectangles_method = 0;
        MyThreadForIntegral M_prostokatow = new MyThreadForIntegral(rectangles_method,points_for_rectangles);
        MyThreadForIntegral M_simpson = new MyThreadForIntegral(simpson_method,points_for_simpsons);
        MyThreadForIntegral M_trapezow = new MyThreadForIntegral(trapezoid_method,points_for_trapezoid);

        M_prostokatow.start();
        M_simpson.start();
        M_trapezow.start();
        try{
            M_prostokatow.join();
            M_simpson.join();
            M_trapezow.join();
            double total_result = M_prostokatow.get_result()+M_simpson.get_result()+M_trapezow.get_result();
            System.out.println("Integral from 0 to 3 of x*2  calculated with 3 method is equal to: "
                    + total_result);
        }catch(InterruptedException e){
            System.err.println("Something went wrong");
        }


    }
}
