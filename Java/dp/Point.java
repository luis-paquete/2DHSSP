package dp;

public class Point implements Comparable<Point>{
	public double x, y;
	public Point(){

	}
	public Point(double x, double y){	
		this.x = x;
		this.y = y;
	}
	public int compareTo(Point p){
		if(this.y > p.y)
			return -1;
		else if(this.y < p.y)
			return 1;
		else
			return (int)(this.x-this.y);
	}
}
