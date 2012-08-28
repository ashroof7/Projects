% n = 4;
% a = [ 0   1   1   1;
%       3   0   3  -4;
%       1   1   1   2;
%       2   3   1   3];
% 
% b = [0;7;6;6];

n = 3;
a =[25 5  1;
     64  8  1;
    144  12 1]

b = [106.8;177.2;279.2]

    for (i=1 : n-1)
        index = i;
        max = a(i,i);
    for (j = i+1 : n)
            if (a(j,i)>max)
                index = j;
               max = a(j,i) ;
            end;
    end;
     
    a([i index],:) = a([index i],:);
    b([i index],:) = b([index i],:);

    b
  
    for (j = i+1 :  n)   
    factor = a(j,i)/a(i,i);
  
    a(j,:) = a(j,:) - factor*a(i,:);
    b(j,:) = b(j,:) - factor*b(i,:);
    end;
     a
     b
    end;
    a
    b
    x = b;
          for(i = n : -1 :1 )
                  x(i) = b(i)
                
                for(j = i+1 :n)
                    x(i) = x(i) - a(i,j)*x(j);
                end;
                x(i) = x(i)/a(i,i);
            end;
    
    x