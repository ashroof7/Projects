package findthebomb;


public class point {
	
	int level;
	int index1;
	int index2;
	point next; 
	
	public point ()
	{
		level=0;
		index1=0;
		index2=0;
		next=null;
	}
	public point(int x,int y ,int z )
	{
		level=x;
		index1=y;
		index2=z;
		next=null;
	}
	public int getlevel()
	{
		return level;
	}
	public int getindex1()
	{
		return index1;
	}
	public int getindex2()
	{
		return index2;
	}
	public String getdata()
	{
		String s=""+level+"      "+index1+"      "+index2;
		return s;
	}
	public void setdata(int x,int y ,int z)
	{
		level=x;
		index1=y;
		index2=z;
		next=null;
	}
	public void setnext(point x)
	{
		next=x;
	}
	public point getnext()
	{
		return next;
	}
	
}