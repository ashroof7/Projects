//675 - Convex Hull of the Polygon
#include <iostream>
#include <complex>
#include <algorithm>
#include <vector>
#include <stack>
#include <cstdio>
using namespace std;

typedef long long type;
typedef complex<type> Point;
typedef vector<Point> vp;
typedef stack<Point> sp;


#define X real()
#define Y imag()
#define vec(a,b)   ((b)-(a))
#define cross(a,b) (((conj(a))*(b)).imag())
#define lengthSqr(a) (((a).X)*((a).X) + ((a).Y)*((a).Y)) //(dot((a),(a)))
#define distSqr(a,b) (lengthSqr(vec((a),(b))))
#define collinear(a,b,c) ((cross( vec((a),(b)) , vec((a),(c)) )) == 0)
#define ccw(a,b,c) (cross(vec((a),(b)),vec((a),(c))) > 0 )


//used for ConvexHull algo
Point pivot;

// Convex Hull
bool compareForConvexHull ( Point a,  Point b){
    if (collinear(pivot,a,b))
        return distSqr(pivot,a) < distSqr(pivot,b);
    return ccw(pivot,a,b);
}

vp convexHull( vp &pts ){
    // Graham's Scan Algorithm
    vp CH ;
    //find the pivot
    int p = 0;
    for (unsigned int i = 1 ; i < pts.size() ;i++)
        if (pts[i].Y < pts[p].Y || (pts[i].Y == pts[p].Y  && pts[i].X > pts[p].X))
            p=i;
            
    pivot = pts[p] ;
    if (p!=0)
    swap(pts[p],pts[0]);
    
    // sort w.r.t pivot
    sort (pts.begin()+1,pts.end(),compareForConvexHull);
    
    // moved after the sorting step
    // to return the pts sorted
    if (pts.size()<=3) {
        CH = vp(pts);
        reverse(CH.begin(), CH.end());
        return CH;
    }
    
    //CCW checks
    sp stack ;
    Point current,prev;
    //do the last check at start
    int i = 1;
    stack.push(pts[pts.size()-1]);
    stack.push(pts[0]);
    
    while (i < (int) pts.size() ){
        
        current = stack.top();
        stack.pop();
        prev = stack.top();
        stack.push(current);
        if (ccw(prev,current,pts[i]))
            stack.push(pts[i++]);
        else
            stack.pop();
    }
    
    while (!stack.empty()){
        CH.push_back(stack.top());
        stack.pop();
    }
    return CH;
    
}

int size,i;
Point p;
vp pts,CH;
bool start = true ;

void solve(){
    CH = convexHull(pts);
    size = (int) CH.size();
         
    if (!start)	cout<<endl;
	start = false ;
         
    for ( i = size-1 ; i >=0 ; i--)
		cout<<CH[i].X<<", "<<CH[i].Y<<endl;				
    pts.clear();
}

int main(){
    string s;
    bool enter = false ;
    
    while (getline(cin,s)){
        if (s == "" || s.empty()){
				if(enter){
					solve();
					enter = false ;
				}
        }else {
            sscanf(s.c_str(),"%lld, %lld",&p.X,&p.Y);            
            pts.push_back(p);
            enter = true ;
        }
    }
    return 0;
}
