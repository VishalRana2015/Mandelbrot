# Mandelbrot Visualization Tool

## Introduction
Welcome to the Mandelbrot Drawing Tool, a <b>Java Swing</b> application desgined to make Mandelbrot set expoloration easy and interactive. This tools allows users to generate intricate Mandelbrot fractals with a user-friendly interface.

### Key Features
- Real-time Mandelbrot set rendering.
- Zooming and panning functionality for detailed exploration.
- Customizable color rendering

### Technologies Used
- Java Programming Language
- Swing Framework for the Graphical User Interface

### Motivation
I created this tool out of passion for exploring fractals.

### Demonstration
<img width="957" alt="image" src="https://github.com/VishalRana2015/Mandelbrot/assets/69715143/a049a6f4-008d-4d5a-a4d4-95a49a828111">

### What is Mandelbrot?
Mandelbrot is a set of complex numbers $c$ for which function $z_{n+1} = z_n ^ 2 + c$ remains bounded, regardless of how many times the process is repeated.  
#### The Iteration Formula
The iterative formula is expressed as:   
$z_{n+1}=z_n ^ 2 + c$
where $z_n$ represents the complex number at the n-th itertion, and c is a constant number from the complex plane which is being evaluated whether it belongs to mandelbrot set or not.  
In the first iteration $z1$, we choose $z0$ as 0.  

#### Boundedness and Membership:
A complex number c is considered a member of the Mandelbrot set if, after an infinite number of iterations, the sequence ${z_n}$ remains bounded. In contranst, if the sequences diverges to infinity, the complex number c is located outside the mandelbrot set.

#### Visualization
The real beauty of the Mandelbrot set lies in its visualization. Each point in the complex plane corresponds to a different c, and the color of that points reflects how quickly the mandelbrot iteration escapes to infinity or remains bounded even after all the doing all the iterations program is set to do.  
<img width="385" alt="image" src="https://github.com/VishalRana2015/Mandelbrot/assets/69715143/76e11b33-fa95-4b62-92bf-2f0b7c136c8c">  
The above image is taken from Mandelbrot set created by this tool. The tool has assinged color to all the numbers in this grid based upon the number of iterations they took to escape.   
##### But how can we test whether a number escapes to infinity or not?
We can use the fact that, if at any point in time the magnitude of $|z_n|$ becomes greater than 2, then the Complex Number always escapes to infinity. There are mathematical proofs about this.  
In the above image, some of the numbers didn't escape to infinity even so they got color **BLACk**.  
The number of iterations each point in this grid is taking to get its magnitude bigger than 2 can be visualized through colors.  


Iteration count is in decreasing order for these colors  
**BLACK** The points belongs to mandelbrot set, as they haven't escaped to infinity.
**DARK RED** - Took more than 320 iterations to escape   
**RED** - Took More than 275 but lesser than 320   
**DARK BROWN** -   
**LIGHT BROWN**   
**LIGHT GREEN**   

I hope by now, you can see through how we are coloring different point on the complex plane. 

### Infinite Intricacy:
This visualization through colors reveal infinity intricacy of self-similarity and complexity at varying levels of magnification. As you zoom into different regions of the Mandelbrot set, you continue to discover intricate patterns and strucutres. This property is a hallmark of fractals.   
##### Self Similarity: 
At different scales, the Mandelbrot Set exhibits self-similarity, meaning that smaller portions of the set resemble the large whole.
<img width="585" alt="image" src="https://github.com/VishalRana2015/Mandelbrot/assets/69715143/a2de9e52-c297-4272-8e00-806e2f2e1182">

##### Fractal Nature:
A classis example of Fractal - a geometric shape that exhibits self-replication and detail at all scales. 
##### Limitless Detail: 
No matter how much zoom into the Mandelbrot set, new details continue to emerge. It is possible that someone can find a unique pattern in the mandelbrot set that no one has seen yet.
##### Computational Limit:
First time I realized limits of **double** data type while exploring infinities of Mandelbrot Set.

### How to use
Most of the control button are self explanatory:   
**Set Iterations** Maximum value of n in $z_{n+1}=z_n^2+c$. Even after applying these many iterations the number doesn't escape to infinity, it is considered to belonging Mandelbrot set and the point is colored BLACK.  
**z0**: In the very first iteration $n=0$ to generate Mandelbrot set we choose $z_0 = 0$. We can also provide different value, based upon different values, it will generate different images.   
**Zoom In:** and **Zoom out** in/from the center of image.
**Up**: Drags the image down to make it its upper region visible. Similarly other buttons works.   
**Palette Length**: 


